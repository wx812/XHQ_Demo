package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseMsgCmd;

/**
 * Created by zyj on 2017/1/19.
 * 邀请群成员
 */
public class InviteGroMemberCmd extends BaseMsgCmd {

    /**
     * 群成员类别
     */
    public Integer getGmt() {
        return getInt("gmt");
    }

    /**
     * 群成员类别
     */
    public void setGmt(Integer gmt) {
        put("gmt", gmt);
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
     * 头像id
     */
    public String getHid() {
        return getStr("hid");
    }

    /**
     * 头像id
     */
    public void setHid(String hid) {
        put("hid", hid);
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
