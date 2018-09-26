package com.xhq.demo.cmd.impl;

/**
 * Created by zyj on 2017/1/19.
 * 修改个人信息,继承changeus是为了发相同的eventype
 */
public class UpdateSelfNotifyFricmd extends ChangeUsCmd {

    /**
     * 昵称
     */
    public String getName() {
        return getStr("name");
    }

    /**
     * 昵称
     */
    public void setName(String name) {
        put("name", name);
    }

    /**
     * 联系人头像id
     */
    public String getHid() {
        return getStr("hid");
    }

    /**
     * 联系人头像id
     */
    public void setHid(String hid) {
        put("hid", hid);
    }

    /**
     * 个性签名
     */
    public String getSignature() {
        return getStr("pl");
    }

    /**
     * 个性签名
     */
    public void setSignature(String pl) {
        put("pl", pl);
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


}
