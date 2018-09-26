package com.xhq.demo.cmd.impl;

/**
 * Created by zyj on 2017/3/10.
 * 发送图片消息
 */
public class SendPicCmd extends SendMsgBaseCmd {

    /**
     * t图片id
     */
    public String getId() {
        return getStr("id");
    }

    /**
     * t图片id
     */
    public void setId(String id) {
        put("id", id);
    }

    /**
     * 略图id
     */
    public String getIds() {
        return getStr("ids");
    }

    /**
     * 略图地址
     */
    public void setIds(String ids) {
        put("ids", ids);
    }

    /**
     * 图片大小
     */
    public Long getSize() {
        return getLong("size");
    }

    /**
     * 图片大小
     */
    public void setSize(Long size) {
        put("size", size);
    }

    /**
     * 对方id
     */
    public String getName() {
        return getStr("name");
    }

    /**
     * 对方id
     */
    public void setName(String name) {
        put("name", name);
    }

    /**
     * t图片id
     */
    public String getHid() {
        return getStr("hid");
    }

    /**
     * t图片id
     */
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
