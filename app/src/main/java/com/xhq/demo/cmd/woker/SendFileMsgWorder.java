package com.xhq.demo.cmd.woker;


import com.xhq.demo.cmd.impl.SendFileCmd;
import com.xhq.demo.tools.StringUtils;

/**
 * Created by zyj on 2017/4/20.
 * 发送文本消息
 */

public class SendFileMsgWorder extends SendMsgWorker<SendFileCmd>{
    @Override
    protected void localDealCmd(SendFileCmd cmd) throws Exception{
        super.localDealCmd(cmd);
        String uid = cmd.getUserId();
        String gid = cmd.getGroupId();
        String mid = StringUtils.isEmptyId(gid) ? gid : uid;//消息id，取群id或用户id

//        //更新最近联系人缓存msg
//        RecentContactsEntity recentContactsEntity = RecentContactsBuffer.getInstance().get(mid);
//        String msg = UtilEncode.base64EncodeEx(ApiConfig.ParamsKey.FILE_MSG);
//        recentContactsEntity.setMsg(msg);
//        RecentContactsBuffer.getInstance().put(mid,recentContactsEntity);
    }

    @Override
    public SendFileCmd newCmdInstance() {
        return new SendFileCmd();
    }
}
