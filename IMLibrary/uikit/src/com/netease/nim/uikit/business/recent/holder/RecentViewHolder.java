package com.netease.nim.uikit.business.recent.holder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.FontConstant;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.business.recent.TeamIconHelper;
import com.netease.nim.uikit.business.recent.adapter.RecentContactAdapter;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nim.uikit.impl.cache.TeamDataCache;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.Map;

public abstract class RecentViewHolder extends RecyclerViewHolder<BaseQuickAdapter, BaseViewHolder, RecentContact> {

    public RecentViewHolder(BaseQuickAdapter adapter) {
        super(adapter);
    }

    protected FrameLayout portraitPanel;

    protected HeadImageView imgHead;

    protected ImageView ivIcon;

    protected TextView tvNickname;

    protected TextView tvMessage;

    protected TextView tvDatetime;

    // 消息发送错误状态标记，目前没有逻辑处理
    protected ImageView imgMsgStatus;

    protected View bottomLine;

    protected View topLine;

    // 未读红点（一个占坑，一个全屏动画）
    protected TextView tvUnread;

    private ImageView imgUnreadExplosion;

    protected TextView tvOnlineState;

    // 子类覆写
    protected abstract String getContent(RecentContact recent);

    @Override
    public void convert(BaseViewHolder holder, RecentContact data, int position, boolean isScrolling) {
        inflate(holder, data);
        refresh(holder, data, position);
    }

    public void inflate(BaseViewHolder holder, final RecentContact recent) {
        this.portraitPanel = holder.getView(R.id.portrait_panel);
        this.imgHead = holder.getView(R.id.img_head);
        this.ivIcon = holder.getView(R.id.iv_icon);
        this.tvNickname = holder.getView(R.id.tv_nickname);
        this.tvMessage = holder.getView(R.id.tv_message);
        this.tvUnread = holder.getView(R.id.unread_number_tip);
        this.tvDatetime = holder.getView(R.id.tv_date_time);
        this.imgMsgStatus = holder.getView(R.id.img_msg_status);
        this.bottomLine = holder.getView(R.id.bottom_line);
        this.topLine = holder.getView(R.id.top_line);
        this.tvOnlineState = holder.getView(R.id.tv_online_state);
        holder.addOnClickListener(R.id.unread_number_tip);

    }

    public void refresh(BaseViewHolder holder, RecentContact recent, final int position) {

        updateBackground(holder, recent, position);

        loadPortrait(recent);

        updateNickLabel(UserInfoHelper.getUserTitleName(recent.getContactId(), recent.getSessionType()));

        updateOnlineState(recent);

        updateMsgLabel(holder, recent);

        updateNewIndicator(recent);

        updateContactIcon(recent);

    }

    private void updateBackground(BaseViewHolder holder, RecentContact recent, int position) {
        topLine.setVisibility(getAdapter().isFirstDataItem(position) ? View.GONE : View.VISIBLE);
        bottomLine.setVisibility(getAdapter().isLastDataItem(position) ? View.VISIBLE : View.GONE);
        if ((recent.getTag() & RecentContactsFragment.RECENT_TAG_STICKY) == 0) {
            holder.getConvertView().setBackgroundResource(R.drawable.touch_bg);
        } else {
            holder.getConvertView().setBackgroundResource(R.drawable.nim_recent_contact_sticky_selecter);
        }
    }

    protected void loadPortrait(RecentContact recent) {
        // 设置头像
        if (recent.getSessionType() == SessionTypeEnum.P2P) {
            imgHead.loadBuddyAvatar(recent.getContactId());
        } else if (recent.getSessionType() == SessionTypeEnum.Team) {
            Team team = TeamDataCache.getInstance().getTeamById(recent.getContactId());
            imgHead.loadTeamIconByTeam(team);
        }
    }

    private void updateContactIcon(RecentContact recent) {
        if (recent.getSessionType() == SessionTypeEnum.Team) {
            //1-粉团队；2-粉团队小组；3-玩家圈；4-店面讨论组；5-活动临时组；6-云店玩家圈;7-看脸圈)
            Map<String, Object> ext = recent.getExtension();
            if (ext != null && !ext.isEmpty()) {
                Object object = ext.get(TeamIconHelper.KEY_TYPE_ICON);
                if (null != object) {
                    int teamType = (int) object;
                    switch (teamType) {
                        case 1:
                        case 2:
                            ivIcon.setVisibility(View.VISIBLE);
                            ivIcon.setImageResource(R.drawable.fenxiaozubiaoshi);
                            break;
                        case 3:
                            ivIcon.setVisibility(View.VISIBLE);
                            ivIcon.setImageResource(R.drawable.quanzibiaoshi);
                            break;
                        case 4:
                            ivIcon.setVisibility(View.VISIBLE);
                            ivIcon.setImageResource(R.drawable.sq_guanliyuan);
                        case 5:
                        case 6:
                            break;
                        case 7:
                            ivIcon.setVisibility(View.VISIBLE);
                            ivIcon.setImageResource(R.drawable.lianquanbiaoshi);
                            break;
                        default:
                            ivIcon.setVisibility(View.VISIBLE);
                            ivIcon.setImageResource(R.drawable.taolunzubiaoshi);
                            break;
                    }
                }
            }
        }
    }

    private void updateNewIndicator(RecentContact recent) {
        int unreadNum = recent.getUnreadCount();
        tvUnread.setVisibility(unreadNum > 0 ? View.VISIBLE : View.GONE);
        tvUnread.setText(unreadCountShowRule(unreadNum));
    }

    private void updateMsgLabel(BaseViewHolder holder, RecentContact recent) {
        tvMessage.setTextSize(14.0f + FontConstant.TEXT_SIZE * 3);
        // 显示消息具体内容
        MoonUtil.identifyRecentVHFaceExpressionAndTags(holder.getContext(), tvMessage, getContent(recent), -1, 0.45f);
        //tvMessage.setText(getContent());

        MsgStatusEnum status = recent.getMsgStatus();
        switch (status) {
            case fail:
                imgMsgStatus.setImageResource(R.drawable.nim_g_ic_failed_small);
                imgMsgStatus.setVisibility(View.VISIBLE);
                break;
            case sending:
                imgMsgStatus.setImageResource(R.drawable.nim_recent_contact_ic_sending);
                imgMsgStatus.setVisibility(View.VISIBLE);
                break;
            default:
                imgMsgStatus.setVisibility(View.GONE);
                break;
        }

        String timeString = TimeUtil.getTimeShowString(recent.getTime(), true);
        tvDatetime.setTextSize(10.0f + FontConstant.TEXT_SIZE * 3);
        tvDatetime.setText(timeString);
    }

    protected String getOnlineStateContent(RecentContact recent) {
        return "";
    }

    protected void updateOnlineState(RecentContact recent) {
        if (recent.getSessionType() == SessionTypeEnum.Team) {
            tvOnlineState.setVisibility(View.GONE);
        } else {
            tvOnlineState.setVisibility(View.VISIBLE);
            tvOnlineState.setText(getOnlineStateContent(recent));
        }
    }

    protected void updateNickLabel(String nick) {
        tvNickname.setTextSize(16.0f + FontConstant.TEXT_SIZE * 3);
        tvNickname.setText(nick);
    }

    protected String unreadCountShowRule(int unread) {
        unread = Math.min(unread, 99);
        return String.valueOf(unread);
    }

    protected RecentContactsCallback getCallback() {
        return ((RecentContactAdapter) getAdapter()).getCallback();
    }
}
