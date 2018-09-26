package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseAnswerCmd;

/**
 * Created by zyj on 2017/4/17.
 * 消息类父类
 */

public class SendMsgBaseCmd extends BaseAnswerCmd {

    /**
     * 对方id
     */
    public String getUserId() {
        return getStr("uid");
    }

    /**
     * 对方id
     */
    public void setUserId(String uid) {
        put("uid", uid);
    }


    /**
     * 群id
     */
    public String getGroupId() {
        return getStr("gid");
    }

    /**
     * 群id
     */
    public void setGroupId(String gid) {
        put("gid", gid);
    }

    /**
     * 消息
     */
    public String getMsg() {
        return getStr("msg");
    }

    /**
     * 消息
     */
    public void setMsg(String msg) {
        put("msg", msg);
    }


}
