package com.xhq.im.tcp.listener;

import java.io.IOException;

/**
 * 次接口所有方法全部不在主线程中执行
 *
 * @author Administrator
 */
public interface TCPListener {
    /*数据监听*/

    /**
     * 发送消息监听（为了确保消息正确发送）
     *
     * @param bys 发送的内容
     */
    void onSend(byte[] bys);

    /**
     * TCP核心每次接收一部分的byte数组就会调用此方法
     *
     * @param bys 数据数组
     * @param len 数组中有效长度
     */
    void onPart(byte[] bys, int len) throws Exception;
    /*异常处理*/

    /**
     * 线程被打断异常
     *
     * @param e wait被打断时的异常
     */
    void onInterrupted(InterruptedException e);

    /**
     * 连接时socket抛出的异常
     *
     * @param e IO类型的异常
     */
    void onConnectIOException(IOException e);

    /**
     * 接收线程抛出的socket异常
     *
     * @param e 异常
     */
    void onReceiveIOException(Exception e);

    /**
     * 发送的时候sockect抛出的异常
     *
     * @param e IO类型的异常
     */
    void onSendIOException(IOException e);

}