package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseAnswerCmd;

/**
 * Created by Akmm at 2016-02-23 10:33
 * 注销指令                                                                                                                     c
 */
public class LogoutCmd extends BaseAnswerCmd {

//    /**
//     * 登录客户端地址
//     */
//    public String getClientAdd() {
//        return getStr("cl");
//    }
//
//    /**
//     * 登录客户端地址
//     */
//    public void setClientAdd(String ClientAdd) {
//        put("cl", ClientAdd);
//    }

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
