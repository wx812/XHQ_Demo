package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseMsgCmd;

/**
 * Created by zyj on 2017/2/9.
 * 同意管理员邀请进群
 */
public class AgrAdminCmd extends BaseMsgCmd {

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
     * 添加结果
     */
    public Integer getRet() {
        return getInt("ret");
    }

    /**
     * 添加结果
     */
    public void setRet(Integer ret) {
        put("ret", ret);
    }

    /**
     * 同意人姓名
     */
    public String getName() {
        return getStr("name");
    }

    /**
     * 同意人姓名
     */
    public void setName(String name) {
        put("name", name);
    }

    /**
     * 群名
     */
    public String getGname() {
        return getStr("gname");
    }

    /**
     * 群名
     */
    public void setGname(String gname) {
        put("gname", gname);
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
