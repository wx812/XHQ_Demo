package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseMsgCmd;

/**
 * Created by zyj on 2017/1/19.
 * 取消管理员
 */
public class CancelGroAdminCmd extends BaseMsgCmd {

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
