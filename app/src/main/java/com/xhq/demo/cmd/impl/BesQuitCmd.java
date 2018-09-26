package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseCmd;

/**
 * Created by zyj on 2017/4/7.
 * 登陆被挤下线
 */
public class BesQuitCmd extends BaseCmd {
    /**
     * 登录客户端信息
     */
    public void setClientInfo(String ClientInfo) {
        put("ci", ClientInfo);
    }

    /**
     * 登录客户端信息
     */
    public String getClientInfo() {
        return getStr("ci");
    }
}
