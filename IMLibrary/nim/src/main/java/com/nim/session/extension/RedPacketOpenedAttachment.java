package com.nim.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.nim.DemoCache;

/**
 * Administrator
 * 2018/1/5
 * 16:05
 */
public class RedPacketOpenedAttachment extends CustomAttachment {
    private String sendAccount; //发送红包id
    private String openAccount; //打开红包id
    private String redPacketId; //红包id
    private boolean isRpGetDone; //红包是否被领完
    private String packetType;//红包类型

    private static final String KEY_SEND = "sendPacketId";
    private static final String KEY_OPEN = "openPacketId";
    private static final String KEY_RP_ID = "redPacketId";
    private static final String KEY_DONE = "isRpGetDone";
    private static final String KEY_TYPE = "packetType";

    public RedPacketOpenedAttachment() {
        super(CustomAttachmentType.OpenedRedPacket);
    }

    @Override
    protected void parseData(JSONObject data) {
        sendAccount = data.getString(KEY_SEND);
        openAccount = data.getString(KEY_OPEN);
        redPacketId = data.getString(KEY_RP_ID);
        isRpGetDone = data.getBoolean(KEY_DONE);
        packetType = data.getString(KEY_TYPE);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_SEND, sendAccount);
        data.put(KEY_OPEN, openAccount);
        data.put(KEY_RP_ID, redPacketId);
        data.put(KEY_DONE, isRpGetDone);
        data.put(KEY_TYPE, packetType);
        return data;
    }

    public String getSendNickName(SessionTypeEnum sessionTypeEnum, String targetId) {
        if (DemoCache.getAccount().equals(sendAccount) && DemoCache.getAccount().equals(openAccount)) {
            return "自己";
        }
        return getDisplayName(sessionTypeEnum, targetId, sendAccount);
    }

    public String getOpenNickName(SessionTypeEnum sessionTypeEnum, String targetId) {
        return getDisplayName(sessionTypeEnum, targetId, openAccount);
    }

    // 我发的红包或者是我打开的红包
    public boolean belongTo(String account) {
        if (openAccount == null || sendAccount == null || account == null) {
            return false;
        }
        return openAccount.equals(account) || sendAccount.equals(account);
    }

    private String getDisplayName(SessionTypeEnum sessionTypeEnum, String targetId, String account) {
        if (sessionTypeEnum == SessionTypeEnum.Team) {
            return TeamHelper.getTeamMemberDisplayNameYou(targetId, account);
        } else if (sessionTypeEnum == SessionTypeEnum.P2P) {
            return UserInfoHelper.getUserDisplayNameEx(account, "你");
        } else {
            return "";
        }
    }

    public String getDesc(SessionTypeEnum sessionTypeEnum, String targetId) {
        String sender = getSendNickName(sessionTypeEnum, targetId);
        String opened = getOpenNickName(sessionTypeEnum, targetId);
        return String.format("%s领取了%s的红包", opened, sender);
    }

    public String getSendAccount() {
        return sendAccount;
    }

    public void setSendAccount(String sendAccount) {
        this.sendAccount = sendAccount;
    }

    public String getOpenAccount() {
        return openAccount;
    }

    public void setOpenAccount(String openAccount) {
        this.openAccount = openAccount;
    }

    public String getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(String redPacketId) {
        this.redPacketId = redPacketId;
    }

    public boolean isRpGetDone() {
        return isRpGetDone;
    }

    public void setRpGetDone(boolean rpGetDone) {
        isRpGetDone = rpGetDone;
    }

    public String getPacketType() {
        return packetType;
    }

    public void setPacketType(String packetType) {
        this.packetType = packetType;
    }

    public static RedPacketOpenedAttachment obtain(SessionTypeEnum sessionTypeEnum, String sendPacketId, String openPacketId, String packetId, boolean isGetDone) {
        RedPacketOpenedAttachment model = new RedPacketOpenedAttachment();
        model.setRedPacketId(packetId);
        model.setSendAccount(sendPacketId);
        model.setOpenAccount(openPacketId);
        if (sessionTypeEnum == SessionTypeEnum.P2P) {
            model.setPacketType("0");
        } else if (sessionTypeEnum == SessionTypeEnum.Team) {
            model.setPacketType("1");
        }
        model.setRpGetDone(isGetDone);
        return model;
    }
}
