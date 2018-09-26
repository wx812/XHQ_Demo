package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseMsgCmd;

/**
 * Created by zyj on 2017/1/17.
 * 新群员加入
 */
public class NewGroMemCmd extends BaseMsgCmd {


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
