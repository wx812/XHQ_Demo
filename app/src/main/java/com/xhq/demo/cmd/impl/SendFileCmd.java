package com.xhq.demo.cmd.impl;

/**
 * Created by zyj on 2017/3/10.
 * 发送文件消息
 */
public class SendFileCmd extends SendMsgBaseCmd {

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
     * 文件大小
     */
    public Long getSize() {
        return getLong("size");
    }

    /**
     * 文件大小
     */
    public void setSize(Long size) {
        put("size", size);
    }

    /**
     * 文件名字
     */
    public String getFn() {
        return getStr("fn");
    }


    /**
     * 文件名字
     */
    public void setFn(String fn) {
        put("fn", fn);
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
