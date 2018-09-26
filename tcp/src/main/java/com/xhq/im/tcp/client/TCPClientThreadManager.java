package com.xhq.im.tcp.client;


import com.xhq.im.tcp.listener.TCPListener;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 此类封装了TCP的线程控制，并提供了错误的时候的接口回调
 * 并且以抛异常为标识来确认链接是否有效
 *
 * @author Hskun
 */
public class TCPClientThreadManager {
    private ExecutorService connectThreadPool = Executors.newSingleThreadExecutor();
    private ExecutorService getThreadPool = Executors.newSingleThreadExecutor();
    private ScheduledExecutorService setThreadPool = Executors.newScheduledThreadPool(1);
    private ReceiveRunnable receiveRunnable = new ReceiveRunnable();// 接收线程不存在设置问题，并且单线程，所以他不存线程问题
    private TCPClientCore tcpCore = new TCPClientCore();
    private final Object lockConnectAndSet = new Object();
    private final Object lockConnectAndGet = new Object();
    private boolean isConnect = false;// 任何异常网络异常，断开连接应该都赋值false
    private TCPListener listener;
    private static TCPClientThreadManager manager;

    private TCPClientThreadManager() {
    }

    public static synchronized TCPClientThreadManager getInstance() {
        if (manager == null) {
            manager = new TCPClientThreadManager();
        }
        return manager;
    }

    /**
     * 链接是否有效
     *
     * @return isConnect
     */
    public boolean isConnect() {
        return isConnect;
    }

    /**
     * 添加TCPListener
     *
     * @param listener TCPListener
     */
    public void setListener(TCPListener listener) {
        this.listener = listener;
    }

    /**
     * 这个必须第一个用
     *
     * @param host           ip字符串
     * @param port           端口号
     * @param onPartListener OnPartListener每次回调的字符串会增加1024个字节， 开发人员可以用此字符串做任何你想做的处理
     */
    public void connectAndReceive(String host, int port, TCPListener onPartListener) {
        tcpCore.setTcpListener(onPartListener);
        connectThreadPool.submit(new ConnectAndReceiveRunnable(host, port));
        // 用对象来保证没有共同操作的数据来避免多线程问题，如果用同步来处理多线程问题那么主线程会卡主
        // 所以我们牺牲内存，来保证用户体验以及解决多线程问题
    }

    /**
     * 向服务器发送消息
     *
     * @param bytes 发送的数据
     */
    public void send(byte[] bytes) {
        if (setThreadPool.isShutdown())
            setThreadPool = Executors.newScheduledThreadPool(1);
        setThreadPool.submit(new SendRunnable(bytes));
    }

    /**
     * 心跳.只需要调用一次这个方法
     *
     * @param bytes        心跳数据包
     * @param initialDelay 初始延迟
     * @param period       周期
     */
    public void heart(byte[] bytes, long initialDelay, long period) {
        if (setThreadPool.isShutdown())
            setThreadPool = Executors.newScheduledThreadPool(1);
        setThreadPool.scheduleAtFixedRate(new SendRunnable(bytes), initialDelay, period, TimeUnit.SECONDS);
    }

    /**
     * 结束发送线程池
     */
    public TCPClientThreadManager stopHeart() {
        if (!setThreadPool.isShutdown()) {
            setThreadPool.shutdownNow();
        }
        return this;
    }

    public void disconnect() {
        tcpCore.disconnect();
        isConnect = false;
    }

    private class SendRunnable implements Runnable {

        private byte[] bytes;

        SendRunnable(byte[] bytes) {
            super();
            this.bytes = bytes;
        }

        @Override
        public void run() {
            synchronized (lockConnectAndSet) {
                if (!isConnect) {
                    try {
                        lockConnectAndSet.wait();
                    } catch (InterruptedException e) {
                        // 这里只是中断了，并没有断开连接，中断之后并不应该执行下面的代码这样会产生线程问题
                        // 这里应该告诉程序员这里被打断了，需要重新发送该消息
                        if (listener != null)
                            listener.onInterrupted(e);
                        return;
                    }
                }
                try {
                    tcpCore.send(bytes);
                } catch (IOException e) {
                    isConnect = false;
                    if (listener != null)
                        listener.onSendIOException(e);
                }
                lockConnectAndSet.notify();
            }
        }

    }

    private class ReceiveRunnable implements Runnable {
        public void run() {
            synchronized (lockConnectAndGet) {
                if (!isConnect) {
                    try {
                        lockConnectAndGet.wait();
                    } catch (InterruptedException e) {
                        // 这里只是中断了，并没有断开连接，中断之后并不应该执行下面的代码这样会产生线程问题
                        // 这里应该告诉程序员这里被打断了，需要重新发送该消息
                        if (listener != null)
                            listener.onInterrupted(e);
                        return;
                    }
                }
                try {
                    tcpCore.getData();
                } catch (Exception e) {
                    isConnect = false;
                    if (listener != null)
                        listener.onReceiveIOException(e);
                }
                lockConnectAndGet.notify();
            }
        }
    }

    private class ConnectAndReceiveRunnable implements Runnable {

        private String host;
        private int port;

        ConnectAndReceiveRunnable(String host, int port) {
            super();
            this.host = host;
            this.port = port;
        }

        @Override
        public void run() {

            synchronized (lockConnectAndGet) {
                if (isConnect) {
                    return;
                }
                synchronized (lockConnectAndSet) {
                    if (isConnect) {// 如果已经链接，那么直接用原来的链接
                        return;
                    }
                    try {
                        // 链接不成功回调onReConnect尝试400毫秒后重连，重连四次不成功回调onError
                        tcpCore.connect(host, port);
                        getThreadPool.submit(receiveRunnable);// 上面的没有抛异常就可以开始接收了，为了分类异常
                        isConnect = true;
                    } catch (IOException e) {
                        isConnect = false;
                        if (listener != null)
                            listener.onConnectIOException(e);
                        //多次尝试重连没有任何作用，不如监听网络状态
                    }
                }
            }
        }
    }

}