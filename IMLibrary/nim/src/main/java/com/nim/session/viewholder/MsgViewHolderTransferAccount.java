package com.nim.session.viewholder;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.nim.R;
import com.nim.session.extension.TransferAccountAttachment;
import com.nim.session.params.SessionType;

/**
 * Administrator
 * 2017/9/18
 * 14:18
 */
public class MsgViewHolderTransferAccount extends MsgViewHolderBase {
    private static final String SINGLE_TRANSFER_ACCOUNT_DETAIL = "com.chat.activity.P2pTransferAccountDetailActivity";
    private static final String GROUP_TRANSFER_ACCOUNT_DETAIL = "com.chat.activity.TeamTransferAccountDetailActivity";
    private RelativeLayout sendView, revView;
    private TextView sendContentText, revContentText;
    private TextView sendTitleText, revTitleText;
    private TransferAccountAttachment attachment;

    public MsgViewHolderTransferAccount(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.transfer_account_item;
    }

    @Override
    protected void inflateContentView() {
        sendContentText = findViewById(R.id.tv_bri_mess_send);
        sendTitleText = findViewById(R.id.tv_bri_target_send);
        sendView = findViewById(R.id.bri_send);
        revContentText = findViewById(R.id.tv_bri_mess_rev);
        revTitleText = findViewById(R.id.tv_bri_target_rev);
        revView = findViewById(R.id.bri_rev);
    }

    @Override
    protected void bindContentView() {
        attachment = (TransferAccountAttachment) message.getAttachment();
        if (!isReceivedMessage()) {// 消息方向，自己发送的
            sendView.setVisibility(View.VISIBLE);
            revView.setVisibility(View.GONE);
            if (attachment.getTransferType() == SessionType.P2P) {
                sendContentText.setText(!TextUtils.isEmpty(attachment.getIntroduce()) ? attachment.getIntroduce() : "转账给" + attachment.getName());
            } else {
                sendContentText.setText(!TextUtils.isEmpty(attachment.getIntroduce()) ? attachment.getIntroduce() : "智享城市，智慧生活");
            }
            sendTitleText.setText(attachment.getAmount() + "元");
        } else {
            sendView.setVisibility(View.GONE);
            revView.setVisibility(View.VISIBLE);
            if (attachment.getTransferType() == SessionType.P2P) {
                revContentText.setText(!TextUtils.isEmpty(attachment.getIntroduce()) ? attachment.getIntroduce() : "转账给" + attachment.getName());
                revTitleText.setText(attachment.getAmount() + "元");
            } else {
                revContentText.setText(!TextUtils.isEmpty(attachment.getIntroduce()) ? attachment.getIntroduce() : "智享城市，智慧生活");
                revTitleText.setText("");
            }
        }
    }

    @Override
    protected int leftBackground() {
        return R.color.transparent;
    }

    @Override
    protected int rightBackground() {
        return R.color.transparent;
    }

    @Override
    protected void onItemClick() {
        if (attachment.getTransferType() == SessionType.P2P) {//1普通转账 2群转账
            startTransferDetail(SINGLE_TRANSFER_ACCOUNT_DETAIL);
        } else {
            startTransferDetail(GROUP_TRANSFER_ACCOUNT_DETAIL);
        }
    }

    private void startTransferDetail(String action) {
        Intent intent = new Intent(action);
        intent.putExtra("transferId", attachment.getId());
        context.startActivity(intent);
    }

}
