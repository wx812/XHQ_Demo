package com.xhq.demo.cmd.impl;

/**
 * Created by zyj on 2017/1/12.
 * 发送文本消息
 */
public class SendMsgCmd extends SendMsgBaseCmd {
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

    /**
     * 客户端登录类型
     */
    public int getCt() {
        return getInt("ct");
    }

    /**
     * 客户端登录类型
     */
    public void setCt(int ct) {
        put("ct", ct);
    }

    public String getName() {
        return getStr("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getHid() {
        return getStr("hid");
    }

    public void setHid(String hid) {
        put("hid", hid);
    }

    /**
     * 群成員類別
     */
    public Integer getGmt() {
        return getInt("gmt");
    }

    /**
     * 群成員類別
     */
    public void setGmt(Integer gmt) {
        put("gmt", gmt);
    }

    /**
     * 联系人备注
     */
    public String getCnote() {
        return getStr("cnote");
    }

    /**
     * 联系人备注
     */
    public void setCnote(String cnote) {
        put("cnote", cnote);
    }

    /**
     * 群昵称
     */
    public String getGnote() {
        return getStr("gnote");
    }

    /**
     * 群昵称
     */
    public void setGnote(String gnote) {
        put("gnote", gnote);
    }

    /**
     //     * 对方id
     //     */
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
     //     * 登录状态
     //     */
    public int getUs() {
        return getInt("us");
    }

    /**
     * 登录状态
     */
    public void setUs(int us) {
        put("us", us);
    }
}
