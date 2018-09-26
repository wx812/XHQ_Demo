package com.nim.session.action;

import android.app.Activity;
import android.content.Intent;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.impl.cache.NimUserInfoCache;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.nim.R;
import com.nim.main.model.Extras;
import com.nim.session.extension.TransferAccountAttachment;
import com.nim.transferaccount.EnvelopeTransferBean;
import com.nim.transferaccount.TransferClient;

/**
 * Administrator
 * 2017/9/18
 * 14:13
 */
public class TransferAccountAction extends BaseAction {
    private static final String SINGLE_TRANSFER_ACCOUNT = "com.chat.activity.P2pTransferAccountActivity";
    private static final String GROUP_TRANSFER_ACCOUNT = "com.chat.activity.TeamTransferAccountActivity";

    private static final int CREATE_SINGLE_TRANSFER_ACCOUNT = 11;
    private static final int CREATE_GROUP_TRANSFER_ACCOUNT = 52;

    public TransferAccountAction() {
        super(R.drawable.message_plus_transfer_selector, R.string.input_panel_transfer);
    }

    @Override
    public void onClick() {
        int requestCode;
        String action;
        if (getContainer().sessionType == SessionTypeEnum.P2P) {
            requestCode = makeRequestCode(CREATE_SINGLE_TRANSFER_ACCOUNT);
            action = SINGLE_TRANSFER_ACCOUNT;
        } else if (getContainer().sessionType == SessionTypeEnum.Team) {
            requestCode = makeRequestCode(CREATE_GROUP_TRANSFER_ACCOUNT);
            action = GROUP_TRANSFER_ACCOUNT;
        } else {
            return;
        }
        startTransferAccount(action, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        sendTransferMessage(data);
    }

    private void sendTransferMessage(Intent intent) {
        EnvelopeTransferBean bean = TransferClient.getEnvelopeTransferInfo(intent);
        if (bean == null) {
            return;
        }
        TransferAccountAttachment attachment = new TransferAccountAttachment();
        attachment.setId(bean.getTransferId());
        attachment.setAmount(bean.getTransferAmount());
        attachment.setName(NimUserInfoCache.getInstance().getUserName(getAccount()));
        attachment.setIntroduce(bean.getTransferMessage());
        attachment.setTransferType(bean.getTransferType());
        String content = null;
        if (getSessionType() == SessionTypeEnum.P2P) {
            content = getActivity().getResources().getString(R.string.p2p_transfer_push_content);
        } else if (getSessionType() == SessionTypeEnum.Team) {
            content = getActivity().getResources().getString(R.string.team_transfer_push_content);
        }
        // 不存云消息历史记录
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableHistory = false;
        IMMessage message = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), content, attachment, config);
        sendMessage(message);
    }

    private void startTransferAccount(String action, int requestCode) {
        Intent intent = new Intent(action);
        intent.putExtra(Extras.EXTRA_ACCOUNT, getAccount());
        getActivity().startActivityForResult(intent, requestCode);
    }

}
