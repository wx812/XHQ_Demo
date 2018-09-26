package com.nim.session.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.contact.core.item.ContactIdFilter;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.session.helper.MessageListPanelHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.widget.SwitchButton;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nim.uikit.dialogfragment.AddFriendDialogFragment;
import com.netease.nim.uikit.face.FaceHelper;
import com.netease.nim.uikit.impl.cache.FriendDataCache;
import com.netease.nim.uikit.impl.cache.NimUserInfoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.nim.DemoCache;
import com.nim.R;
import com.nim.contact.activity.CustomContactSelectActivity;
import com.nim.team.TeamCreateHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzxuwen on 2015/10/13.
 */
public class MessageInfoActivity extends UI implements View.OnClickListener, AddFriendDialogFragment.ItemClickCallBack {
    private final static String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
    private static final int REQUEST_CODE_NORMAL = 1;
    public static final int REQUEST_CODE_ADVANCED = 2;
    // data
    private String account;
    // view
    private SwitchButton switchButton;

    private RelativeLayout rl_background;
    private RelativeLayout rl_clear_history;
    private HeadImageView userHead;
    private TextView userName;

    public static void startActivity(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, MessageInfoActivity.class);
        intent.putExtra(EXTRA_ACCOUNT, account);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_info_activity);

        ToolBarOptions options = new ToolBarOptions();
        options.titleId = R.string.message_info;
        setToolBar(R.id.toolbar, options);

        account = getIntent().getStringExtra(EXTRA_ACCOUNT);
        findViews();
        loadUserInfo();
    }

    private void loadUserInfo() {
        NimUserInfoCache.getInstance().getUserInfoFromRemote(account, new RequestCallback<NimUserInfo>() {
            @Override
            public void onSuccess(NimUserInfo param) {
                if (null != param){
                    userHead.loadTeamIconByUrl(param.getAvatar());
                    userName.setText(param.getName());
                }
            }

            @Override
            public void onFailed(int code) {

                if (code == 408) {
                    Toast.makeText(DemoCache.getContext(), R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DemoCache.getContext(), "on failed:" + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
                Toast.makeText(DemoCache.getContext(), "on exception:" + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSwitchBtn();
    }

    private void findViews() {
        userHead = (HeadImageView) findViewById(R.id.user_layout).findViewById(R.id.imageViewHeader);
        userName = (TextView) findViewById(R.id.user_layout).findViewById(R.id.textViewName);
        userHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击头像跳转脸圈个人公共主页
                FaceHelper.startFaceDetailByUserCode(MessageInfoActivity.this, account);
            }
        });

        TextView textViewName = (TextView) findViewById(R.id.create_team_layout).findViewById(R.id.textViewName);
        textViewName.setText(R.string.create_team_chat);
        HeadImageView addImage = (HeadImageView) findViewById(R.id.create_team_layout).findViewById(R.id.imageViewHeader);
        addImage.setBackgroundResource(R.drawable.nim_team_member_add);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTeamMsg();
            }
        });

        boolean isMyFriend = FriendDataCache.getInstance().isMyFriend(account);
        textViewName.setVisibility(isMyFriend ? View.VISIBLE : View.GONE);
        addImage.setVisibility(isMyFriend ? View.VISIBLE : View.GONE);

        ((TextView) findViewById(R.id.toggle_layout).findViewById(R.id.user_profile_title)).setText(R.string.msg_notice);
        switchButton = (SwitchButton) findViewById(R.id.toggle_layout).findViewById(R.id.user_profile_toggle);
        switchButton.setOnChangedListener(onChangedListener);

        rl_background = (RelativeLayout) findViewById(R.id.rl_background);
        rl_clear_history = (RelativeLayout) findViewById(R.id.rl_clear_history);
        rl_background.setOnClickListener(this);
        rl_clear_history.setOnClickListener(this);
    }

    private void updateSwitchBtn() {
        boolean notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);
        switchButton.setCheck(notice);
    }

    private SwitchButton.OnChangedListener onChangedListener = new SwitchButton.OnChangedListener() {
        @Override
        public void OnChanged(View v, final boolean checkState) {
            if (!NetworkUtil.isNetAvailable(MessageInfoActivity.this)) {
                Toast.makeText(MessageInfoActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                switchButton.setCheck(!checkState);
                return;
            }

            NIMClient.getService(FriendService.class).setMessageNotify(account, checkState).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    if (checkState) {
                        Toast.makeText(MessageInfoActivity.this, "开启消息提醒成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MessageInfoActivity.this, "关闭消息提醒成功", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailed(int code) {
                    if (code == 408) {
                        Toast.makeText(MessageInfoActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MessageInfoActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                    }

                    switchButton.setCheck(!checkState);
                }

                @Override
                public void onException(Throwable exception) {

                }
            });
        }
    };

    private void openUserProfile() {
        //UserProfileActivity.start(this, account);
    }

    /**
     * 创建群聊
     */
    private void createTeamMsg() {
        ArrayList<String> memberAccounts = new ArrayList<>();
        memberAccounts.add(account);
        CustomContactSelectActivity.CustomOption advancedOption = getCreateContactSelectOption(memberAccounts, 50);
        advancedOption.disableFilterId = account;
        advancedOption.allowSelectEmpty = false;
        advancedOption.isCreateTeam = true;
        advancedOption.searchVisible = false;
        CustomContactSelectActivity.startActivity(this, advancedOption);
    }

    /**
     * 获取创建群通讯录选择器option
     *
     * @return
     */
    public static CustomContactSelectActivity.CustomOption getCreateContactSelectOption(ArrayList<String> memberAccounts, int teamCapacity) {
        // 限制群成员数量在群容量范围内
        int capacity = teamCapacity - (memberAccounts == null ? 0 : memberAccounts.size());
        CustomContactSelectActivity.CustomOption option = getContactSelectOption(memberAccounts);
        option.maxSelectNum = capacity;
        option.maxSelectedTip = NimUIKit.getContext().getString(com.netease.nim.uikit.R.string.reach_team_member_capacity, teamCapacity);
        option.allowSelectEmpty = true;
        option.alreadySelectedAccounts = memberAccounts;
        return option;
    }

    /**
     * 获取通讯录选择器option
     *
     * @param memberAccounts
     * @return
     */
    public static CustomContactSelectActivity.CustomOption getContactSelectOption(List<String> memberAccounts) {
        CustomContactSelectActivity.CustomOption option = new CustomContactSelectActivity.CustomOption();
        option.title = NimUIKit.getContext().getString(com.netease.nim.uikit.R.string.invite_member);
        if (memberAccounts != null) {
            ArrayList<String> disableAccounts = new ArrayList<>();
            disableAccounts.addAll(memberAccounts);
            option.itemDisableFilter = new ContactIdFilter(disableAccounts);
        }
        return option;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADVANCED) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                onCreateTeamName(selected);
            }
        }
    }

    private void onCreateTeamName(final ArrayList<String> selected) {
        AddFriendDialogFragment dialogFragment = AddFriendDialogFragment.newInstance(selected, "创建群聊", "请输入群聊名称");
        dialogFragment.setListener(this);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
        dialogFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_background) {
            Intent intent = new Intent("com.chat.activity.SetBackgroundActivity");
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.rl_clear_history) {
            EasyAlertDialogHelper.createOkCancelDiolag(this, null, "您确定要清除聊天记录吗？", true, new EasyAlertDialogHelper.OnDialogActionListener() {
                @Override
                public void doCancelAction() {

                }

                @Override
                public void doOkAction() {
                    NIMClient.getService(MsgService.class).clearChattingHistory(account, SessionTypeEnum.P2P);
                    MessageListPanelHelper.getInstance().notifyClearMessages(account);
                    finish();
                }
            }).show();
        }
    }

    @Override
    public void onItemClickSure(String userCode, String msg) {

    }

    @Override
    public void onItemClickSure(List<String> selected, String msg) {
        if (selected != null && !selected.isEmpty()) {
            TeamCreateHelper.createAdvancedTeam(MessageInfoActivity.this, msg, selected);
        } else {
            Toast.makeText(DemoCache.getContext(), "请选择至少一个联系人！", Toast.LENGTH_SHORT).show();
        }
    }
}
