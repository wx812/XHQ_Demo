package com.xhq.demo.socket;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author user
 * @date: 2017/6/8
 * 包名必须与客户端一直,否则无法反序列化
 */

public class SocketHead implements Serializable {
    /**
     * 常量，用于校验 Header
     */
    public int flag;

    /**
     * 操作码，表示本次操作的意图，比如获取数据库文件，获取日志文件
     */
    public int cmd;

    /**
     * 数据长度
     */
    public long dataLen;

    /**
     * 时间戳
     */
    public long timestamp;

    public SocketHead(int cmd, long dataLen) {
        this.cmd = cmd;
        this.dataLen = dataLen;
        this.timestamp = new Date().getTime();
    }

    public SocketHead(int cmd, long dataLen, long timestamp) {
        this.cmd = cmd;
        this.dataLen = dataLen;
        this.timestamp = timestamp;
    }
}