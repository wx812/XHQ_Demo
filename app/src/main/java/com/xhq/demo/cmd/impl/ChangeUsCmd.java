package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseCmd;

/**
 * Created by zyj on 2017/1/9.
 * 更改用户在线状态
 */
public class ChangeUsCmd extends BaseCmd {

    /**
     * 对方id
     */
    public String getUid() {
        return getStr("uid");
    }

    /**
     * 对方id
     */
    public void setUid(String uid) {
        put("uid", uid);
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
