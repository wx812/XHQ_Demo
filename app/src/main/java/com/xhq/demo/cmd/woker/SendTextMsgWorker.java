package com.xhq.demo.cmd.woker;


import com.xhq.demo.cmd.impl.SendMsgCmd;

/**
 * Created by zyj on 2017/4/17.
 * 发送文本消息
 */

public class SendTextMsgWorker extends SendMsgWorker<SendMsgCmd> {
    @Override
    public SendMsgCmd newCmdInstance() {
        return new SendMsgCmd();
    }

    public void localDealCmd(final SendMsgCmd cmd) throws Exception{
        super.localDealCmd(cmd);
    }
}
