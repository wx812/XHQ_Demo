package com.xhq.im.tcp.client;

import com.orhanobut.logger.Logger;
import com.xhq.im.tcp.listener.TCPListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class TCPClientCore {

    private Socket socket;

    private TCPListener tcpListener;

    void setTcpListener(TCPListener tcpListener) {
        this.tcpListener = tcpListener;
    }

    /**
     * 这个必须第一个用
     *
     * @param host ip字符串
     * @param port 端口号
     * @throws IOException 抛出的IO异常
     */
    void connect(String host, int port) throws IOException {
        if (socket != null) {
            if (!socket.isClosed())
                socket.close();
            // socket.connect(new InetSocketAddress(host, port));
        }
        socket = new Socket(host, port);
    }

    /**
     * 下行，一个socket只能被调用一次 [base64]
     *
     * @throws Exception 抛出的IO异常
     */
    public void getData() throws Exception {
        InputStream is = socket.getInputStream();
        byte[] bys = new byte[8192];
        int len;
        while ((len = is.read(bys)) != -1) {
            if (tcpListener != null)
                tcpListener.onPart(bys, len);
        }
    }

    /**
     * 上行方法，这里我们应该不做任何封装，可以给上层更多的操作方式
     * 没有链接的时候send是不会抛异常的
     *
     * @param bytes byte数组
     * @throws IOException 抛出的IO异常
     */
    void send(byte[] bytes) throws IOException {
        Logger.d("socket:send---- " + "time" + System.currentTimeMillis() + new String(bytes));
        OutputStream os = socket.getOutputStream();
        if (tcpListener != null) {
            tcpListener.onSend(bytes);
        }
        os.write(bytes);
        //os.flush();//强制刷新会Broken pipe
    }

    /**
     * 客户端主动关闭socket
     */
    void disconnect() {
        if (null != socket) {
            if (!socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}