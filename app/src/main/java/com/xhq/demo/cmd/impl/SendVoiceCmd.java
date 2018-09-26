package com.xhq.demo.cmd.impl;

/**
 * Created by zyj on 2017/2/12.
 * 发送语音消息
 */
public class SendVoiceCmd extends SendMsgBaseCmd  {

    /**
     * 文件id
     */
    public String getId() {
        return getStr("id");
    }

    /**
     * 文件id
     */
    public void setId(String id) {
        put("id", id);
    }

    /**
     * 语音时长
     */
    public Integer getLen() {
        return getInt("len");
    }

    /**
     * 语音时长
     */
    public void setLen(Integer len) {
        put("len", len);
    }

    /**
     * 语音大小
     */
    public Integer getSize() {
        return getInt("size");
    }

    /**
     * 语音大小
     */
    public void setSize(Integer size) {
        put("size", size);
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
    public int getGmt() {
        return getInt("gmt");
    }

    /**
     * 群成員類別
     */
    public void setGmt(int gmt) {
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
     * //     * 对方id
     * //
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
     * 登录状态
     */
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
