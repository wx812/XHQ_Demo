package com.nim.session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.session.SessionEventListener;
import com.netease.nim.uikit.business.recent.TeamType;
import com.netease.nim.uikit.business.recent.TeamTypeEntity;
import com.netease.nim.uikit.business.recent.TeamTypeHelper;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.helper.MessageHelper;
import com.netease.nim.uikit.business.session.module.MsgForwardFilter;
import com.netease.nim.uikit.business.session.module.MsgRevokeFilter;
import com.netease.nim.uikit.business.team.model.TeamExtras;
import com.netease.nim.uikit.business.team.model.TeamRequestCode;
import com.netease.nim.uikit.face.FaceHelper;
import com.netease.nim.uikit.impl.cache.TeamDataCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.Team;
import com.nim.DemoCache;
import com.nim.R;
import com.nim.session.action.AVChatAction;
import com.nim.session.action.GuessAction;
import com.nim.session.action.RedPacketAction;
import com.nim.session.action.TeamCodeAction;
import com.nim.session.action.TransferAccountAction;
import com.nim.session.activity.MessageInfoActivity;
import com.nim.session.extension.CustomAttachParser;
import com.nim.session.extension.CustomAttachment;
import com.nim.session.extension.GuessAttachment;
import com.nim.session.extension.RTSAttachment;
import com.nim.session.extension.RedPacketAttachment;
import com.nim.session.extension.RedPacketOpenedAttachment;
import com.nim.session.extension.ShareAttachment;
import com.nim.session.extension.SnapChatAttachment;
import com.nim.session.extension.StickerAttachment;
import com.nim.session.extension.TransferAccountAttachment;
import com.nim.session.viewholder.MsgViewHolderAVChat;
import com.nim.session.viewholder.MsgViewHolderDefCustom;
import com.nim.session.viewholder.MsgViewHolderGuess;
import com.nim.session.viewholder.MsgViewHolderOpenRedPacket;
import com.nim.session.viewholder.MsgViewHolderRTS;
import com.nim.session.viewholder.MsgViewHolderRedPacket;
import com.nim.session.viewholder.MsgViewHolderShare;
import com.nim.session.viewholder.MsgViewHolderSnapChat;
import com.nim.session.viewholder.MsgViewHolderSticker;
import com.nim.session.viewholder.MsgViewHolderTip;
import com.nim.session.viewholder.MsgViewHolderTransferAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * UIKit自定义消息界面用法展示类
 */
public class SessionHelper {
    private static final String TAG = SessionHelper.class.getSimpleName();
    private static SessionCustomization p2pCustomization;
    private static SessionCustomization teamCustomization;
    private static SessionCustomization myP2pCustomization;

    public static void init() {
        // 注册自定义消息附件解析器
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());

        // 注册各种扩展消息类型的显示ViewHolder
        registerViewHolders();

        // 设置会话中点击事件响应处理
        setSessionListener();

        // 注册消息转发过滤器
        registerMsgForwardFilter();

        // 注册消息撤回过滤器
        registerMsgRevokeFilter();

        // 注册消息撤回监听器
        registerMsgRevokeObserver();

        NimUIKit.setCommonP2PSessionCustomization(getP2pCustomization());

        NimUIKit.setCommonTeamSessionCustomization(getTeamCustomization());
    }

    public static void startP2PSession(Context context, String account) {
        startP2PSession(context, account, null);
    }

    public static void startP2PSession(Context context, String account, IMMessage anchor) {
        if (!DemoCache.getAccount().equals(account)) {
            NimUIKit.startP2PSession(context, account, anchor);
        } else {
            NimUIKit.startChatting(context, account, SessionTypeEnum.P2P, getMyP2pCustomization(), anchor);
        }
    }

    public static void startTeamSession(Context context, String tid) {
        startTeamSession(context, tid, null);
    }

    public static void startTeamSession(Context context, String tid, IMMessage anchor) {
        NimUIKit.startTeamSession(context, tid, anchor);
    }

    // 打开群聊界面(用于 UIKIT 中部分界面跳转回到指定的页面)
    public static void startTeamSession(Context context, String tid, Class<? extends Activity> backToClass, IMMessage anchor) {
        NimUIKit.startChatting(context, tid, SessionTypeEnum.Team, getTeamCustomization(), backToClass, anchor);
    }

    // 定制化单聊界面。如果使用默认界面，返回null即可
    private static SessionCustomization getP2pCustomization() {
        if (p2pCustomization == null) {
            p2pCustomization = new SessionCustomization() {
                // 由于需要Activity Result， 所以重载该函数。
                @Override
                public void onActivityResult(final Activity activity, int requestCode, int resultCode, Intent data) {
                    super.onActivityResult(activity, requestCode, resultCode, data);

                }

                @Override
                public MsgAttachment createStickerAttachment(String category, String item) {
                    return new StickerAttachment(category, item);
                }
            };

            // 背景
//            p2pCustomization.backgroundColor = Color.BLUE;
//            p2pCustomization.backgroundUri = "file:///android_asset/xx/bk.jpg";
//            p2pCustomization.backgroundUri = "file:///sdcard/Pictures/bk.png";
//            p2pCustomization.backgroundUri = "android.resource://com.netease.nim.demo/drawable/bk"

            // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
            ArrayList<BaseAction> actions = new ArrayList<>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                actions.add(new AVChatAction(AVChatType.AUDIO));
                actions.add(new AVChatAction(AVChatType.VIDEO));
            }
            actions.add(new GuessAction());
            actions.add(new TransferAccountAction());
            actions.add(new RedPacketAction());

            p2pCustomization.actions = actions;
            p2pCustomization.withSticker = true;

            // 定制ActionBar右边的按钮，可以加多个
            ArrayList<SessionCustomization.OptionsButton> buttons = new ArrayList<>();
            SessionCustomization.OptionsButton infoButton = new SessionCustomization.OptionsButton() {
                @Override
                public void onClick(Context context, View view, String sessionId) {
                    MessageInfoActivity.startActivity(context, sessionId); //打开聊天信息
                }
            };

            infoButton.iconId = R.drawable.icon_nim_right_setting;
            buttons.add(infoButton);
            p2pCustomization.buttons = buttons;
        }

        return p2pCustomization;
    }

    private static SessionCustomization getMyP2pCustomization() {
        if (myP2pCustomization == null) {
            myP2pCustomization = new SessionCustomization() {
                // 由于需要Activity Result， 所以重载该函数。
                @Override
                public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
                    if (requestCode == TeamRequestCode.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                        String result = data.getStringExtra(TeamExtras.RESULT_EXTRA_REASON);
                        if (result == null) {
                            return;
                        }
                        if (result.equals(TeamExtras.RESULT_EXTRA_REASON_CREATE)) {
                            String tid = data.getStringExtra(TeamExtras.RESULT_EXTRA_DATA);
                            if (TextUtils.isEmpty(tid)) {
                                return;
                            }

                            startTeamSession(activity, tid);
                            activity.finish();
                        }
                    }
                }

                @Override
                public MsgAttachment createStickerAttachment(String category, String item) {
                    return new StickerAttachment(category, item);
                }
            };

            // 背景
//            p2pCustomization.backgroundColor = Color.BLUE;
//            p2pCustomization.backgroundUri = "file:///android_asset/xx/bk.jpg";
//            p2pCustomization.backgroundUri = "file:///sdcard/Pictures/bk.png";
//            p2pCustomization.backgroundUri = "android.resource://com.netease.nim.demo/drawable/bk"

            // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
            ArrayList<BaseAction> actions = new ArrayList<>();
            actions.add(new GuessAction());
            actions.add(new TransferAccountAction());
            actions.add(new RedPacketAction());

            myP2pCustomization.actions = actions;
            myP2pCustomization.withSticker = true;
            // 定制ActionBar右边的按钮，可以加多个
            ArrayList<SessionCustomization.OptionsButton> buttons = new ArrayList<>();
            myP2pCustomization.buttons = buttons;
        }
        return myP2pCustomization;
    }

    private static final int MIN_CLICK_DELAY_TIME = 3000;// 两次点击按钮之间的点击间隔不能少于3000毫秒
    private static long lastClickTime;

    public static SessionCustomization getTeamCustomization() {
        if (teamCustomization == null) {
            ArrayList<BaseAction> actions = new ArrayList<>();
            actions.add(new GuessAction());
            actions.add(new TransferAccountAction());
            actions.add(new TeamCodeAction());
            actions.add(new RedPacketAction());

            teamCustomization = new SessionCustomization() {
                @Override
                public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
                    super.onActivityResult(activity, requestCode, resultCode, data);
                }

                @Override
                public MsgAttachment createStickerAttachment(String category, String item) {
                    return new StickerAttachment(category, item);
                }
            };

            teamCustomization.actions = actions;

            // 定制ActionBar右边的按钮，可以加多个
            ArrayList<SessionCustomization.OptionsButton> buttons = new ArrayList<>();

            SessionCustomization.OptionsButton infoButton = new SessionCustomization.OptionsButton() {
                @Override
                public void onClick(Context context, View view, String sessionId) {
                    long curClickTime = System.currentTimeMillis();
                    if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                        lastClickTime = curClickTime;
                        queryTeamType(context, sessionId);
                    }
                }
            };
            infoButton.iconId = R.drawable.icon_nim_group_setting;
            buttons.add(infoButton);
            teamCustomization.buttons = buttons;

            teamCustomization.withSticker = true;
        }

        return teamCustomization;
    }

    private static void registerViewHolders() {
        NimUIKit.registerMsgItemViewHolder(AVChatAttachment.class, MsgViewHolderAVChat.class);
        NimUIKit.registerMsgItemViewHolder(GuessAttachment.class, MsgViewHolderGuess.class);
        NimUIKit.registerMsgItemViewHolder(CustomAttachment.class, MsgViewHolderDefCustom.class);
        NimUIKit.registerMsgItemViewHolder(ShareAttachment.class, MsgViewHolderShare.class);
        NimUIKit.registerMsgItemViewHolder(StickerAttachment.class, MsgViewHolderSticker.class);
        NimUIKit.registerMsgItemViewHolder(SnapChatAttachment.class, MsgViewHolderSnapChat.class);
        NimUIKit.registerMsgItemViewHolder(RTSAttachment.class, MsgViewHolderRTS.class);
        NimUIKit.registerTipMsgViewHolder(MsgViewHolderTip.class);
        NimUIKit.registerMsgItemViewHolder(TransferAccountAttachment.class, MsgViewHolderTransferAccount.class);
        NimUIKit.registerMsgItemViewHolder(RedPacketAttachment.class, MsgViewHolderRedPacket.class);
        NimUIKit.registerMsgItemViewHolder(RedPacketOpenedAttachment.class, MsgViewHolderOpenRedPacket.class);
    }

    private static void setSessionListener() {
        SessionEventListener listener = new SessionEventListener() {
            @Override
            public void onAvatarClicked(Context context, IMMessage message) {
                // 一般用于打开用户资料页面  自己暂不做跳转
                if (!DemoCache.getAccount().equals(message.getFromAccount())) {
                    //UserProfileActivity.start(context, message.getFromAccount());
                    FaceHelper.startFaceDetailByUserCode(context, message.getFromAccount());
                }
            }

            @Override
            public void onAvatarLongClicked(Context context, IMMessage message) {
                // 一般用于群组@功能，或者弹出菜单，做拉黑，加好友等功能
            }
        };

        NimUIKit.setSessionListener(listener);
    }


    /**
     * 消息转发过滤器
     */
    private static void registerMsgForwardFilter() {
        NimUIKit.setMsgForwardFilter(new MsgForwardFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (message.getDirect() == MsgDirectionEnum.In
                        && (message.getAttachStatus() == AttachStatusEnum.transferring
                        || message.getAttachStatus() == AttachStatusEnum.fail)) {
                    // 接收到的消息，附件没有下载成功，不允许转发
                    return true;
                } else if (message.getMsgType() == MsgTypeEnum.custom && message.getAttachment() != null
                        && (message.getAttachment() instanceof SnapChatAttachment
                        || message.getAttachment() instanceof RTSAttachment
                        || message.getAttachment() instanceof TransferAccountAttachment
                        || message.getAttachment() instanceof RedPacketAttachment)) {
                    // 白板消息和阅后即焚消息，转账 红包消息 不允许转发
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 消息撤回过滤器
     */
    private static void registerMsgRevokeFilter() {
        NimUIKit.setMsgRevokeFilter(new MsgRevokeFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (message.getAttachment() != null
                        && (message.getAttachment() instanceof AVChatAttachment
                        || message.getAttachment() instanceof RTSAttachment
                        || message.getAttachment() instanceof TransferAccountAttachment
                        || message.getAttachment() instanceof RedPacketAttachment)) {
                    // 视频通话消息和白板消息，转账 红包消息 不允许撤回
                    return true;
                } else if (DemoCache.getAccount().equals(message.getSessionId())) {
                    // 发给我的电脑 不允许撤回
                    return true;
                }
                return false;
            }
        });
    }

    private static void registerMsgRevokeObserver() {
        NIMClient.getService(MsgServiceObserve.class).observeRevokeMessage(new Observer<IMMessage>() {
            @Override
            public void onEvent(IMMessage message) {
                if (message == null) {
                    return;
                }

                MessageHelper.getInstance().onRevokeMessage(message);
            }
        }, true);
    }

    private static void queryTeamType(final Context context, final String sessionId) {
        TeamTypeHelper.queryTeamType(context, sessionId, TAG, new TeamTypeHelper.SubscriberNetCallBack<TeamTypeEntity>() {
            @Override
            protected void onFailure(Throwable throwable, String s, int code) {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onSuccess(TeamTypeEntity entity) {
                if (null != entity) {
                    List<TeamTypeEntity.TeamTypesBean> teamTypes = entity.getTeamTypes();
                    if (null != teamTypes && teamTypes.size() > 0) {
                        TeamTypeEntity.TeamTypesBean typesBean = teamTypes.get(0);
                        String detailId = typesBean.getDetailId();
                        int detailType = typesBean.getDetailtType();
                        switch (detailType) {
                            case TeamType.TYPE_PLAY_CIRCLE:
                                int orgId = typesBean.getOrgId();
                                if (orgId != 0) {
                                    startGroupDetail(context, orgId);
                                } else {
                                    startCircleDetail(context, detailId);
                                }
                                break;
                            default:
                                getTeamInfo(context, sessionId);
                                break;
                        }
                    }
                } else {
                    getTeamInfo(context, sessionId);
                }
            }
        });
    }

    private static void startCircleDetail(Context context, String circleId) {
        try {
            Intent intent = new Intent("NewCircleDetailActivity", Uri.parse("smartcity://playcircle/circleDetailPage?circleId=" + circleId));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startGroupDetail(Context context, int orgId) {
        try {
            Intent intent = new Intent("organize.groupdetial", Uri.parse("smartcity://organize/groupdetail?id=" + orgId));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getTeamInfo(Context context, String sessionId) {
        Team team = TeamDataCache.getInstance().getTeamById(sessionId);
        if (team != null && team.isMyTeam()) {
            NimUIKit.startTeamInfo(context, sessionId);
        }
    }
}
