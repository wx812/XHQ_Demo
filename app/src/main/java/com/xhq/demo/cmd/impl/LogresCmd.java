package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseAnswerCmd;

/**
 * Created by zyj on 2017/1/10.
 * 登录确认指令
 */
public class LogresCmd extends BaseAnswerCmd {
    /**
     * sk
     */
    public String getSk() {
        return getStr("sk");
    }

    /**
     * sk
     */
    public void setSk(String sk) {
        put("sk", sk);
    }

    /**
     * 离线消息条数
     */
    public Integer getNum() {
        return getInt("num");
    }

    /**
     * 离线消息条数
     */
    public void setNum(Integer num) {
        put("num", num);
    }

    /**
     * 居家号
     */
    public String getUserCode() {
        return getStr("uc");
    }

    /**
     * 居家号
     */
    public void setUserCode(String userCode) {
        put("uc", userCode);
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
     * 联系人id
     */
    public String getUid() {
        return getStr("uid");
    }

    /**
     * 联系人id
     */
    public void setUid(String uid) {
        put("uid", uid);
    }

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
     * 联系人个性签名
     */
    public String getPl() {
        return getStr("pl");
    }

    /**
     * 联系人个性签名
     */
    public void setPl(String pl) {
        put("pl", pl);
    }


    /**
     * 用户类别
     */
    public int getUt() {
        return getInt("ut");
    }

    /**
     * 用户类别
     */
    public void setUt(int ut) {
        put("ut", ut);
    }

    /**
     * 手机号
     */
    public String getTel() {
        return getStr("tel");
    }

    /**
     * 手机号
     */
    public void setTel(String tel) {
        put("tel", tel);
    }
}
