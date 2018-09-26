package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseMsgCmd;

/**
 * Created by zyj on 2017/1/17.
 * 解散群
 */
public class DelGroupCmd extends BaseMsgCmd {

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
     * 群昵称
     */
    public String getGname() {
        return getStr("gname");
    }

    /**
     * 群昵称
     */
    public void setGname(String gname) {
        put("gname", gname);
    }

}
