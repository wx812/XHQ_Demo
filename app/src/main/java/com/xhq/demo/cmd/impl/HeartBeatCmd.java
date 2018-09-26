package com.xhq.demo.cmd.impl;


import com.xhq.demo.cmd.BaseCmd;
import com.xhq.demo.constant.apiconfig.ApiEnum;

/**
 * Created by Akmm at 2016-02-23 10:33
 * 心跳指令
 */
public class HeartBeatCmd extends BaseCmd {

    public HeartBeatCmd() {
        this.setMsgType(ApiEnum.CmdType.PING.value);
//        this.setTime(Utils.getCurTime());
    }

}
