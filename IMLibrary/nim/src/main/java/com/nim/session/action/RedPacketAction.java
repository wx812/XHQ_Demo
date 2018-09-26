package com.nim.session.action;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.nim.R;
import com.nim.main.model.Extras;
import com.nim.redpacket.bean.EnvelopeRpBean;
import com.nim.redpacket.constant.RedPacketStatus;
import com.nim.redpacket.helper.RedPacketClient;
import com.nim.session.extension.RedPacketAttachment;

import java.util.HashMap;
import java.util.Map;

/**
 * Administrator
 * 2018/1/5
 * 11:23
 */
public class RedPacketAction extends BaseAction {
    private static final String SINGLE_RED_PACKET = "P2pRedPacketActivity";
    private static final String GROUP_RED_PACKET = "TeamRedPacketActivity";

    private static final int CREATE_GROUP_RED_PACKET = 51;
    private static final int CREATE_SINGLE_RED_PACKET = 10;

    public RedPacketAction() {
        super(R.drawable.message_plus_rp_selector, R.string.input_panel_rep);
    }

    @Override
    public void onClick() {
        int requestCode;
        String action;
        if (getContainer().sessionType == SessionTypeEnum.P2P) {
            requestCode = makeRequestCode(CREATE_SINGLE_RED_PACKET);
            action = SINGLE_RED_PACKET;
        } else if (getContainer().sessionType == SessionTypeEnum.Team) {
            requestCode = makeRequestCode(CREATE_GROUP_RED_PACKET);
            action = GROUP_RED_PACKET;
        } else {
            return;
        }
        startSendRpActivity(action, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        sendRpMessage(data);
    }

    private void sendRpMessage(Intent intent) {
        EnvelopeRpBean bean = RedPacketClient.getEnvelopeRpInfo(intent);
        if (bean == null) {
            return;
        }
        RedPacketAttachment attachment = new RedPacketAttachment();
        // 红包id，红包信息，红包名称
        attachment.setRedPacketId(bean.getEnvelopesID());
        attachment.setContent(bean.getEnvelopeMessage());
        attachment.setTitle(bean.getEnvelopeName());
        String content = String.format(getActivity().getString(R.string.rp_push_content), !TextUtils.isEmpty(bean.getEnvelopeMessage()) ? bean.getEnvelopeMessage() : getActivity().getString(R.string.rp_wishes_str));
        // 不存云消息历史记录
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableHistory = false;
        IMMessage message = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), content, attachment, config);
        Map<String, Object> data = new HashMap<>();
        data.put("RedPacketStatus", RedPacketStatus.RP_UNOPEN);//初始状态 可领取
        message.setLocalExtension(data);
        sendMessage(message);
    }

    private void startSendRpActivity(String action, int requestCode) {
        Intent intent = new Intent(action);
        intent.putExtra(Extras.EXTRA_ACCOUNT, getAccount());
        getActivity().startActivityForResult(intent, requestCode);
    }
}
