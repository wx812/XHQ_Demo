package com.xhq.demo.cmd;

/**
 * Created by Akmm at 2016-01-15 15:21
 * 指令基类
 */
public class BaseUserCmd extends BaseCmd {

    /**
     * 对方id
     */
    public String getUid() {
        return getStr("uid");
    }

    /**
     * 对方id
     */
    public void setUid(String uid) {
        put("uid", uid);
    }

    /**
     * 发送方状态
     */
    public int getStatu() {
        return getInt("statu");
    }

    /**
     * 发送方状态
     */
    public void setStatu(int statu) {
        put("statu", statu);
    }
}