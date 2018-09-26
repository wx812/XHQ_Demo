package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseMsgCmd;

/**
 * Created by zyj on 2017/1/19.
 * 发布群公告
 */
public class SendGroNoticeCmd extends BaseMsgCmd {

    /**
     * 公告id
     */
    public String getNid() {
        return getStr("nid");
    }

    /**
     * 公告id
     */
    public void setNid(String nid) {
        put("nid", nid);
    }

    /**
     * 公告标题
     */
    public String getTl() {
        return getStr("tl");
    }

    /**
     * 公告标题
     */
    public void setTl(String tl) {
        put("tl", tl);
    }

    /**
     * 公告内容
     */
    public String getCt() {
        return getStr("ct");
    }

    /**
     * 公告内容
     */
    public void setCt(String ct) {
        put("ct", ct);
    }

    public String getName() {
        return getStr("name");
    }

    public void setName(String name) {
        put("name", name);
    }

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


}
