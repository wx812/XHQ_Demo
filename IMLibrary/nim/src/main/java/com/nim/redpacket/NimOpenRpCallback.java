package com.nim.redpacket;


import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.module.ModuleProxy;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.nim.DemoCache;
import com.nim.session.extension.RedPacketOpenedAttachment;


/**
 * Administrator
 * 2018/1/24
 * 14:01
 */
public class NimOpenRpCallback {
    private String sendUserAccount;
    private String sessionId;
    private SessionTypeEnum sessionType;
    private ModuleProxy proxy;

    public NimOpenRpCallback(String sendUserAccount, String sessionId, SessionTypeEnum sessionType, ModuleProxy proxy) {
        this.sendUserAccount = sendUserAccount;
        this.sessionId = sessionId;
        this.sessionType = sessionType;
        this.proxy = proxy;
    }

    public void sendMessage(String openAccount, String envelopeId, boolean getDone) {
        if (proxy == null) {
            return;
        }
        IMMessage imMessage;
        final NimUserInfo selfInfo = (NimUserInfo) NimUIKit.getUserInfoProvider().getUserInfo(DemoCache.getAccount());
        if (selfInfo == null) {
            return;
        }
        RedPacketOpenedAttachment redPacketOpenedMessage;
        if (openAccount.equals(sendUserAccount)) {
            redPacketOpenedMessage = RedPacketOpenedAttachment.obtain(sessionType, selfInfo.getAccount(), selfInfo.getAccount(), envelopeId, getDone);
        } else {
            redPacketOpenedMessage = RedPacketOpenedAttachment.obtain(sessionType, sendUserAccount, selfInfo.getAccount(), envelopeId, getDone);
        }
        String content = redPacketOpenedMessage.getDesc(sessionType, sessionId);
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableHistory = false;
        config.enablePush = false;
        config.enableUnreadCount = false;
        imMessage = MessageBuilder.createCustomMessage(sessionId, sessionType, content, redPacketOpenedMessage, config);
        proxy.sendMessage(imMessage);
    }

}
