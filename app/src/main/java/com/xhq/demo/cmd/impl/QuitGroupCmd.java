package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseCmd;

/**
 * Created by zyj on 2017/2/4.
 * 退群
 */
public class QuitGroupCmd extends BaseCmd {

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

}
