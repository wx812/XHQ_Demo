package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseMsgCmd;

/**
 * Created by zyj on 2017/2/9.
 */
public class ChangeGroupSocketCmd extends BaseMsgCmd {

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
     * 群昵称
     */
    public String getGname() {
        return getStr("gname");
    }

    /**
     * 群昵称
     */
    public void setGname(String gname) {
        put("gname", gname);
    }

    /**
     * 群头像id
     */
    public String getHid() {
        return getStr("hid");
    }

    /**
     * 群头像id
     */
    public void setHid(String hid) {
        put("hid", hid);
    }


    /**
     * 群简介
     */
    public String getGdesc() {
        return getStr("gdesc");
    }

    /**
     * 群简介
     */
    public void setGdesc(String gdesc) {
        put("gdesc", gdesc);
    }

    /**
     * 群名片
     */
    public String getGc() {
        return getStr("gc");
    }

    /**
     * 群名片
     */
    public void setGc(String gc) {
        put("gc", gc);
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

}
