package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseAnswerCmd;

/**
 * Created by zyj on 2017/1/13.
 */
public class AddFriendCmd extends BaseAnswerCmd{

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
     * 消息内容
     */
    public String getMsg() {
        return getStr("msg");
    }

    /**
     * 消息内容
     */
    public void setMsg(String msg) {
        put("msg", msg);
    }

    /**
     * 姓名
     */
    public String getGname() {
        return getStr("gname");
    }

    /**
     * 姓名
     */
    public void setGname(String gname) {
        put("gname", gname);
    }

    /**
     * 姓名
     */
    public String getName() {
        return getStr("name");
    }

    /**
     * 姓名
     */
    public void setName(String name) {
        put("name", name);
    }

    /**
     * 居家号
     */
    public String getUc() {
        return getStr("uc");
    }

    /**
     * 居家号
     */
    public void setUc(String uc) {
        put("uc", uc);
    }

    /**
     * 头像
     */
    public String getHid() {
        return getStr("hid");
    }

    /**
     * 头像
     */
    public void setHid(String hid) {
        put("hid", hid);
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
