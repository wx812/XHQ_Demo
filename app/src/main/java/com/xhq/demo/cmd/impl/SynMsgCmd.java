package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseCmd;

/**
 * Created by zyj on 2017/2/10.
 */
public class SynMsgCmd extends BaseCmd {

    /**
     * Data里指令类型 参见ApiEnum.CmdType
     */
    public int getOldMsgType() {
        return getInt("omtype");
    }

    /**
     * Data里指令类型 参见ApiEnum.CmdType
     */
    public void setOldMsgType(int omtype) {
        put("omtype", omtype);
    }

    /**
     * base64
     */
    public String getSynData() {
        return getStr("data");
    }

    /**
     * base64
     */
    public void setSynData(String data) {
        put("data", data);
    }

}
