package com.xhq.demo.cmd.impl;

/**
 * Created by zyj on 2017/1/13.
 */
public class ConFirAddCmd extends AddFriendCmd {

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
     * 分组id
     */
    public String getCid() {
        return getStr("cid");
    }

    /**
     * 分组id
     */
    public void setCid(String cid) {
        put("cid", cid);
    }

//    /**
//     * 联系人类型0商户1个人
//     */
//    public Integer getCtype() {
//        return getInt("ctype", -1);
//    }
//
//    /**
//     * 联系人类型0商户1个人
//     */
//    public void setCtype(String ctype) {
//        put("ctype", ctype);
//    }

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
     *
     */
    public String getName() {
        return getStr("name");
    }

    /**
     * 联系人备注
     */
    public void setName(String name) {
        put("name", name);
    }

    /**
     * 联系人备注
     */
    public String getGname() {
        return getStr("gname");
    }

    /**
     * 联系人备注
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
