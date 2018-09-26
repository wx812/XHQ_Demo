package com.xhq.demo.cmd;

/**
 * Created by Akmm at 2016-01-15 15:21
 * 应答指令基类
 */
public class BaseAnswerCmd extends BaseCmd {
    public BaseAnswerCmd() {
    }

    public BaseAnswerCmd(BaseCmd orgCmd) {
        if (orgCmd == null) return;
        this.setOrgMsgId(orgCmd.getMsgId());
        this.setOrgMsgType(orgCmd.getMsgType());
    }

    /**
     * 错误码
     */
    public Integer getStatu() {
        return getInt("ec");
    }

    /**
     * 错误码
     */
    public void setStatu(int statu) {
        put("ec", statu);
    }

    /**
     * 附加信息
     */
    public String getInfo() {
        return getStr("et");
    }

    /**
     * 附加信息
     */
    public void setInfo(String info) {
        put("et", info);
    }

    /**
     * 原消息id，客户端来的消息id
     */
    public String getOrgMsgId() {
        return getStr("omid");
    }

    /**
     * 原消息id，客户端来的消息id
     */
    public void setOrgMsgId(String omid) {
        put("omid", omid);
    }

    /**
     * 原消息类型 参见ApiEnum.CmdType
     */
    public int getOrgMsgType() {
        return getInt("omtype");
    }

    /**
     * 原消息类型 参见ApiEnum.CmdType
     */
    public void setOrgMsgType(int type) {
        put("omtype", type);
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
     * 消息
     */
    public String getMsg() {
        return getStr("msg");
    }

    /**
     * 消息
     */
    public void setMsg(String msg) {
        put("msg", msg);
    }


}