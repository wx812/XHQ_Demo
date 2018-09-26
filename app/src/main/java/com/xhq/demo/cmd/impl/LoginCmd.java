package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseCmd;

/**
 * Created by Akmm at 2016-02-23 10:33
 * 登录指令                                                                                                                     c
 */
public class LoginCmd extends BaseCmd {

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
     * 登陆密码
     */
    public String getPassword() {
        return getStr("up");
    }

    /**
     * 登陆密码
     */
    public void setPassword(String password) {
        put("up", password);
    }

    /**
     * 发送者客户端类别 参见ApiEnum.ClientType
     */
    public int getClientType() {
        return getInt("ct");
    }

    /**
     * 发送者客户端类别 参见ApiEnum.ClientType
     */
    public void setClientType(int clientType) {
        put("ct", clientType);
    }

    /**
     * 登录客户端信息
     */
    public String getClientInfo() {
        return getStr("ci");
    }

    /**
     * 登录客户端信息
     */
    public void setClientInfo(String clientType) {
        put("ci", clientType);
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
     //     * 登录状态
     //     */
    public String getImei() {
        return getStr("imei");
    }

    /**
     * 登录状态
     */
    public void setImei(String imei) {
        put("imei", imei);
    }

}
