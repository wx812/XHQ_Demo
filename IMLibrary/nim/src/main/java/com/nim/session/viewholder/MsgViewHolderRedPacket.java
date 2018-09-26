package com.nim.session.viewholder;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.business.session.module.ModuleProxy;
import com.netease.nim.uikit.business.session.module.list.MsgAdapter;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.nim.R;
import com.nim.redpacket.NimOpenRpCallback;
import com.nim.redpacket.UpdateRpCallBack;
import com.nim.redpacket.constant.RedPacketStatus;
import com.nim.redpacket.helper.NimRedPacketClient;
import com.nim.session.extension.RedPacketAttachment;

import java.util.HashMap;
import java.util.Map;

/**
 * Administrator
 * 2018/1/5
 * 11:31
 */
public class MsgViewHolderRedPacket extends MsgViewHolderBase implements UpdateRpCallBack {
    private static final String TAG = MsgViewHolderRedPacket.class.getName();
    private RelativeLayout sendView, revView;
    private TextView sendContentText, revContentText;
    private TextView sendTitleText, revTitleText;
    private RedPacketAttachment attachment;
    private int rpStatus;

    public MsgViewHolderRedPacket(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.red_packet_item;
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
        Map<String, Object> data = message.getLocalExtension();
        if (null != data) {
            rpStatus = (Integer) data.get("RedPacketStatus");
        }
        attachment = (RedPacketAttachment) message.getAttachment();
        if (!isReceivedMessage()) {// 自己发送的
            sendView.setVisibility(View.VISIBLE);
            revView.setVisibility(View.GONE);
            sendView.setBackgroundResource(getBackgroundByStatus(rpStatus));
            sendContentText.setText(attachment.getContent());
            sendTitleText.setText(getTitleByRpStatus(rpStatus));
        } else {
            sendView.setVisibility(View.GONE);
            revView.setVisibility(View.VISIBLE);
            revView.setBackgroundResource(getBackgroundByStatus(rpStatus));
            revContentText.setText(attachment.getContent());
            revTitleText.setText(getTitleByRpStatus(rpStatus));
        }
    }

    private int getBackgroundByStatus(int rpStatus) {
        switch (rpStatus) {
            case RedPacketStatus.RP_OPEN:
                return !isReceivedMessage() ? R.drawable.chat_redpacket_qp_lq_right_gl : R.drawable.chat_redpacket_qp_lq_left_gl;
            case RedPacketStatus.RP_GET_FINISHED:
                return !isReceivedMessage() ? R.drawable.chat_redpacket_qp_lq_right_gl : R.drawable.chat_redpacket_qp_lq_left_gl;
            case RedPacketStatus.RP_EXPIRE:
                return !isReceivedMessage() ? R.drawable.chat_redpacket_qp_lq_right_gl : R.drawable.chat_redpacket_qp_lq_left_gl;
        }
        return !isReceivedMessage() ? R.drawable.red_packet_send_bg : R.drawable.red_packet_rev_bg;
    }

    private String getTitleByRpStatus(int rpStatus) {
        switch (rpStatus) {
            case RedPacketStatus.RP_OPEN:
                return "红包已领取";
            case RedPacketStatus.RP_GET_FINISHED:
                return "红包已被领完";
            case RedPacketStatus.RP_EXPIRE:
                return "红包已过期";
        }
        return !isReceivedMessage() ? "查看红包" : "领取红包";
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
        BaseMultiItemFetchLoadAdapter adapter = getAdapter();
        ModuleProxy proxy = null;
        if (adapter instanceof MsgAdapter) {
            proxy = ((MsgAdapter) adapter).getContainer().proxy;
        } else {
            //ToDo
        }
        NimOpenRpCallback cb = new NimOpenRpCallback(message.getFromAccount(), message.getSessionId(), message.getSessionType(), proxy);
        NimRedPacketClient.startOpenRpDialog((Activity) context, message.getSessionType(), attachment.getRedPacketId(), cb, this);
    }

    @Override
    public void updateRp(int state) {
        Map<String, Object> map = new HashMap<>();
        map.put("RedPacketStatus", state);
        message.setLocalExtension(map);
        NIMClient.getService(MsgService.class).updateIMMessage(message);
        getAdapter().notifyDataSetChanged();
    }
}
