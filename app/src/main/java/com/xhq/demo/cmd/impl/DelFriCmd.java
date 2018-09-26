package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseAnswerCmd;

/**
 * Created by zyj on 2017/1/16.
 */
public class DelFriCmd extends BaseAnswerCmd {
    public String getUserId() {
        return getStr("uid");
    }

    /**
     * 对方id
     */
    public void setUserId(String uid) {
        put("uid", uid);
    }

}
