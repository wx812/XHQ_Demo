package com.xhq.demo.cmd.woker;


import com.xhq.demo.cmd.impl.SendVoiceCmd;
import com.xhq.demo.tools.StringUtils;

/**
 * Created by zyj on 2017/4/20.
 * 发送语音消息
 */

public class SendVoiceMsgWorker extends SendMsgWorker<SendVoiceCmd>{

    @Override
    public SendVoiceCmd newCmdInstance() {
        return new SendVoiceCmd();
    }

    public void localDealCmd(final SendVoiceCmd cmd) throws Exception{
        super.localDealCmd(cmd);
        String uid = cmd.getUserId();
        String gid = cmd.getGroupId();
        String mid = StringUtils.isEmptyId(gid) ? gid : uid;//消息id，取群id或用户id

//        //更新最近联系人缓存msg
//        RecentContactsEntity recentContactsEntity = RecentContactsBuffer.getInstance().get(mid);
//        String msg = UtilEncode.base64EncodeEx(ApiConfig.ParamsKey.YY_MSG);
//        recentContactsEntity.setMsg(msg);
//        RecentContactsBuffer.getInstance().put(mid,recentContactsEntity);
    }
}
