package com.nim.contact.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.QueryUserInfo;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyEditDialog;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.widget.SwitchButton;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nim.uikit.dialogfragment.AddFriendDialogFragment;
import com.netease.nim.uikit.face.FaceHelper;
import com.netease.nim.uikit.face.faceapi.UpdateFriendCase;
import com.netease.nim.uikit.face.faceapi.UpdateFriendParams;
import com.netease.nim.uikit.impl.cache.NimUserInfoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.FriendServiceObserve;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.netease.nimlib.sdk.friend.model.MuteListChangedNotify;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SystemMessageStatus;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.nim.DemoCache;
import com.nim.R;
import com.nim.main.activity.BaseChatUI;
import com.nim.main.helper.MessageHelper;
import com.nim.main.model.Extras;
import com.nim.session.SessionHelper;
import com.smartcity.common.commonprovider.inter.NoDoubleClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.commonsdk.auto.compiler.JoinCircleApiCase;
import common.commonsdk.autousecase.lifecycle.Job;
import common.commonsdk.http.DefaultSubscriberNetCallBack;
import common.commonsdk.http.SubscribeManager;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 用户资料页面------>好友验证
 * Created by huangjun on 2015/8/11.
 */
public class FriendVerifyActivity extends BaseChatUI implements AddFriendDialogFragment.ItemClickCallBack {

    private static final String TAG = FriendVerifyActivity.class.getSimpleName();

    private static final String KEY_SYS_MSG = "sys_msg";

    private final boolean FLAG_ADD_FRIEND_DIRECTLY = false; // 是否直接加为好友开关，false为需要好友申请
    private final String KEY_BLACK_LIST = "black_list";
    private final String KEY_MSG_NOTICE = "msg_notice";

    private String account;

    // 基本信息
    private HeadImageView headImageView;
    private TextView nameText;
    private ImageView genderImage;
    // 开关
    private ViewGroup toggleLayout;
    private SwitchButton blackSwitch;
    private SwitchButton noticeSwitch;
    private Map<String, Boolean> toggleStateMap;

    private Toolbar toolbar;
    private TextView wordText;
    private String verifyContent;
    private Button bt_declined;
    private Button bt_agree;
    private SystemMessage systemMessage;

    public static void start(Context context, String account, SystemMessage message) {
        Intent intent = new Intent();
        intent.setClass(context, FriendVerifyActivity.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT, account);
        intent.putExtra("systemMessage", message);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_verify_activity);

        toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.title);

        toolbar.setTitle("");
        title.setText(getResources().getString(R.string.friend_verify_str));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.fanhui));

        account = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
        systemMessage = (SystemMessage) getIntent().getSerializableExtra("systemMessage");

        initActionbar();

        findViews();
        registerObserver(true);
    }

    @Override
    protected int getStateBarColor() {
        return Color.TRANSPARENT;
    }

    @Override
    public void initToolBar() {
        setTintBarRes(R.mipmap.bg_top_dianci);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.user_profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        /*if (!DemoCache.getAccount().equals(account) && (NIMClient.getService(FriendService.class).isMyFriend(account))) {
            menu.findItem(R.id.action_shield).setVisible(true);
        } else {
            menu.findItem(R.id.action_shield).setVisible(false);
        }*/
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_shield) {
            Intent intent = new Intent("com.chat.activity.ShieldDynamicActivity");
            intent.putExtra(Extras.EXTRA_ACCOUNT, account);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserInfo();
        updateToggleView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObserver(false);
        SubscribeManager.getInstance().cance(TAG);
    }

    private void registerObserver(boolean register) {
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
        NIMClient.getService(FriendServiceObserve.class).observeMuteListChangedNotify(muteListChangedNotifyObserver, register);
    }

    Observer<MuteListChangedNotify> muteListChangedNotifyObserver = new Observer<MuteListChangedNotify>() {
        @Override
        public void onEvent(MuteListChangedNotify notify) {
            setToggleBtn(noticeSwitch, !notify.isMute());
        }
    };

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> account) {
            updateUserOperatorView();
        }

        @Override
        public void onDeletedFriends(List<String> account) {
            updateUserOperatorView();
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            updateUserOperatorView();
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            updateUserOperatorView();
        }
    };

    private void findViews() {
        headImageView = findView(R.id.user_head_image);
        nameText = findView(R.id.user_name);
        genderImage = findView(R.id.gender_img);
        toggleLayout = findView(R.id.toggle_layout);
        wordText = (TextView) findViewById(R.id.verify_text);
        bt_declined = findViewById(R.id.bt_declined);
        bt_agree = findViewById(R.id.bt_agree);

        wordText.setText(MessageHelper.getVerifyNotificationText(systemMessage));
        headImageView.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                FaceHelper.startFaceDetailByUserCode(FriendVerifyActivity.this, account.toLowerCase());
            }
        });

        bt_agree.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                //同意
                onAgree(systemMessage);
            }
        });

        bt_declined.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                //拒绝
                onReject(systemMessage);
            }
        });


    }

    public void onAgree(SystemMessage message) {
        onSystemNotificationDeal(message, true, 0);
    }

    public void onReject(SystemMessage message) {
        onSystemNotificationDeal(message, false, 1);
    }

    private void onSystemNotificationDeal(final SystemMessage message, final boolean pass, final int type) {
        RequestCallback callback = new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                onProcessSuccess(pass, message, type);
            }

            @Override
            public void onFailed(int code) {
                onProcessFailed(code, message);
            }

            @Override
            public void onException(Throwable exception) {

            }
        };
        if (message.getType() == SystemMessageType.TeamInvite) {
            if (pass) {

                //同意进群消息  由脸圈组调用接口远程同意
                DialogMaker.showProgressDialog(this, getString(com.netease.nim.uikit.R.string.loading), true);

                Observable.create(new Observable.OnSubscribe<Team>() {
                    @Override
                    public void call(final Subscriber<? super Team> subscriber) {
                        NIMClient.getService(TeamService.class).searchTeam(message.getTargetId()).setCallback(new RequestCallback<Team>() {
                            @Override
                            public void onSuccess(Team param) {
                                subscriber.onNext(param);
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onFailed(int code) {
                                subscriber.onError(new Throwable(code + ""));
                            }

                            @Override
                            public void onException(Throwable exception) {
                                subscriber.onError(exception);
                            }
                        });
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new rx.Observer<Team>() {
                            @Override
                            public void onNext(Team team) {

                                Job.get(this, JoinCircleApiCase.class).execute(new Object[]{message.getFromAccount(), message.getTargetId(), NimUIKit.getAccount(),team.getCreator(),team.getName()}, new DefaultSubscriberNetCallBack<String>() {
                                    @Override
                                    protected void onFailure(Throwable throwable, String s, int i) {
                                        DialogMaker.dismissProgressDialog();
                                        onProcessFailed(0, message);
                                    }

                                    @Override
                                    protected void onSuccess(String s) {
                                        DialogMaker.dismissProgressDialog();
                                        onProcessSuccess(pass, message,type);
                                    }
                                });
                            }

                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                onProcessSuccess(pass, message,type);
                            }
                        });


            } else {
                NIMClient.getService(TeamService.class).declineInvite(message.getTargetId(), message.getFromAccount(), "").setCallback(callback);
            }
        } else if (message.getType() == SystemMessageType.ApplyJoinTeam) {
            if (pass) {
                NIMClient.getService(TeamService.class).passApply(message.getTargetId(), message.getFromAccount()).setCallback(callback);
            } else {
                NIMClient.getService(TeamService.class).rejectApply(message.getTargetId(), message.getFromAccount(), "").setCallback(callback);
            }
        } else if (message.getType() == SystemMessageType.AddFriend) {
            if (pass) {
                UpdateFriendCase updateFriendCase = new UpdateFriendCase(FriendVerifyActivity.this);
                updateFriendCase.execute(updateFriendCase.createParams(new UpdateFriendParams(QueryUserInfo.getUserCode(FriendVerifyActivity.this), message.getFromAccount(), 0))
                        , new DefaultSubscriberNetCallBack<String>() {
                            @Override
                            protected void onFailure(Throwable throwable, String s, int code) {
                                Toast.makeText(FriendVerifyActivity.this, s, Toast.LENGTH_SHORT).show();
                            }


                            @Override
                            protected void onSuccess(String s) {
                                Log.e(TAG, "s=" + s);
                                onProcessSuccess(pass, message, type);
                            }
                        });
                SubscribeManager.getInstance().add(TAG, updateFriendCase);
            } else {
                NIMClient.getService(FriendService.class).ackAddFriendRequest(message.getFromAccount(), false).setCallback(callback);
            }
        }
    }

    private void onProcessSuccess(final boolean pass, SystemMessage message, int type) {
        SystemMessageStatus status = pass ? SystemMessageStatus.passed : SystemMessageStatus.declined;
        NIMClient.getService(SystemMessageService.class).setSystemMessageStatus(message.getMessageId(),
                status);
        message.setStatus(status);
        if (type == 0) {
            bt_agree.setText("已同意");
        } else if (type == 1) {
            bt_declined.setText("已拒绝");
        }
        refreshViewHolder(message);
        finish();
    }

    private void onProcessFailed(final int code, SystemMessage message) {
        Log.e(TAG, "failed, error code=" + code);
        Toast.makeText(this, "已过期", Toast.LENGTH_LONG).show();
        if (code == 408) {
            return;
        }

        SystemMessageStatus status = SystemMessageStatus.expired;
        NIMClient.getService(SystemMessageService.class).setSystemMessageStatus(message.getMessageId(),
                status);
        message.setStatus(status);
        refreshViewHolder(message);
    }

    private void refreshViewHolder(final SystemMessage message) {
        EventBus.getDefault().post(message);
    }

    private void initActionbar() {

    }

    private void addToggleBtn(boolean black, boolean notice) {
        noticeSwitch = addToggleItemView(KEY_MSG_NOTICE, R.string.msg_notice, notice);
        //blackSwitch = addToggleItemView(KEY_BLACK_LIST, R.string.black_list, black);
    }

    private void setToggleBtn(SwitchButton btn, boolean isChecked) {
        btn.setCheck(isChecked);
    }

    private void updateUserInfo() {
        if (NimUserInfoCache.getInstance().hasUser(account)) {
            updateUserInfoView();
            return;
        }

        NimUserInfoCache.getInstance().getUserInfoFromRemote(account, new RequestCallbackWrapper<NimUserInfo>() {
            @Override
            public void onResult(int code, NimUserInfo result, Throwable exception) {
                updateUserInfoView();
            }
        });
    }

    private void updateUserInfoView() {
        headImageView.loadBuddyAvatar(account);

        if (DemoCache.getAccount().equals(account)) {
            nameText.setText(NimUserInfoCache.getInstance().getUserName(account));
        }

        final NimUserInfo userInfo = NimUserInfoCache.getInstance().getUserInfo(account);
        if (userInfo == null) {
            LogUtil.e(TAG, "userInfo is null when updateUserInfoView");
            return;
        }

        if (userInfo.getGenderEnum() == GenderEnum.MALE) {
            genderImage.setVisibility(View.VISIBLE);
            genderImage.setBackgroundResource(R.drawable.nim_male);
        } else if (userInfo.getGenderEnum() == GenderEnum.FEMALE) {
            genderImage.setVisibility(View.VISIBLE);
            genderImage.setBackgroundResource(R.drawable.nim_female);
        } else {
            genderImage.setVisibility(View.GONE);
        }

        nameText.setText(userInfo.getName());
    }

    private void updateUserOperatorView() {
        /*chatBtn.setVisibility(View.VISIBLE);
        if (NIMClient.getService(FriendService.class).isMyFriend(account)) {
            removeFriendBtn.setVisibility(View.VISIBLE);
            addFriendBtn.setVisibility(View.GONE);
            updateAlias(true);
        } else {
            addFriendBtn.setVisibility(View.VISIBLE);
            removeFriendBtn.setVisibility(View.GONE);
            updateAlias(false);
        }*/
    }

    private void updateToggleView() {
        if (DemoCache.getAccount() != null && !DemoCache.getAccount().equals(account)) {
            boolean black = NIMClient.getService(FriendService.class).isInBlackList(account);
            boolean notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);
            if (blackSwitch == null || noticeSwitch == null) {
                addToggleBtn(black, notice);
            } else {
                setToggleBtn(blackSwitch, black);
                setToggleBtn(noticeSwitch, notice);
            }
            Log.i(TAG, "black=" + black + ", notice=" + notice);
            updateUserOperatorView();
        }
    }

    private SwitchButton addToggleItemView(String key, int titleResId, boolean initState) {
        ViewGroup vp = (ViewGroup) getLayoutInflater().inflate(R.layout.nim_user_profile_toggle_item, null);
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.isetting_item_height));
        vp.setLayoutParams(vlp);

        TextView titleText = ((TextView) vp.findViewById(R.id.user_profile_title));
        titleText.setText(titleResId);

        SwitchButton switchButton = (SwitchButton) vp.findViewById(R.id.user_profile_toggle);
        switchButton.setCheck(initState);
        switchButton.setOnChangedListener(onChangedListener);
        switchButton.setTag(key);

        toggleLayout.addView(vp);

        if (toggleStateMap == null) {
            toggleStateMap = new HashMap<>();
        }
        toggleStateMap.put(key, initState);
        return switchButton;
    }

    private SwitchButton.OnChangedListener onChangedListener = new SwitchButton.OnChangedListener() {
        @Override
        public void OnChanged(View v, final boolean checkState) {
            final String key = (String) v.getTag();
            if (!NetworkUtil.isNetAvailable(FriendVerifyActivity.this)) {
                Toast.makeText(FriendVerifyActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                if (key.equals(KEY_BLACK_LIST)) {
                    blackSwitch.setCheck(!checkState);
                } else if (key.equals(KEY_MSG_NOTICE)) {
                    noticeSwitch.setCheck(!checkState);
                }
                return;
            }

            updateStateMap(checkState, key);

            if (key.equals(KEY_BLACK_LIST)) {
                if (checkState) {
                    NIMClient.getService(FriendService.class).addToBlackList(account).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            Toast.makeText(FriendVerifyActivity.this, "加入黑名单成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(int code) {
                            if (code == 408) {
                                Toast.makeText(FriendVerifyActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FriendVerifyActivity.this, "on failed：" + code, Toast.LENGTH_SHORT).show();
                            }
                            updateStateMap(!checkState, key);
                            blackSwitch.setCheck(!checkState);
                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
                } else {
                    NIMClient.getService(FriendService.class).removeFromBlackList(account).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            Toast.makeText(FriendVerifyActivity.this, "移除黑名单成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(int code) {
                            if (code == 408) {
                                Toast.makeText(FriendVerifyActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FriendVerifyActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                            }
                            updateStateMap(!checkState, key);
                            blackSwitch.setCheck(!checkState);
                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
                }
            } else if (key.equals(KEY_MSG_NOTICE)) {
                NIMClient.getService(FriendService.class).setMessageNotify(account, checkState).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        if (checkState) {
                            Toast.makeText(FriendVerifyActivity.this, "开启消息提醒成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FriendVerifyActivity.this, "关闭消息提醒成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 408) {
                            Toast.makeText(FriendVerifyActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FriendVerifyActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                        }
                        updateStateMap(!checkState, key);
                        noticeSwitch.setCheck(!checkState);
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
            }
        }
    };

    private void updateStateMap(boolean checkState, String key) {
        if (toggleStateMap.containsKey(key)) {
            toggleStateMap.put(key, checkState);  // update state
            Log.i(TAG, "toggle " + key + "to " + checkState);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*if (v == addFriendBtn) {
                if (FLAG_ADD_FRIEND_DIRECTLY) {
                    doAddFriend(null, true);  // 直接加为好友
                } else {
                    //onAddFriendByVerify(); // 发起好友验证请求
                    AddFriendDialogFragment dialogFragment = AddFriendDialogFragment.newInstance(account);
                    dialogFragment.setListener(FriendVerifyActivity.this);
                    dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
                    dialogFragment.show(FriendVerifyActivity.this.getSupportFragmentManager(), "dialog");
                }
            }*/
        }
    };

    /**
     * 通过验证方式添加好友
     */
    private void onAddFriendByVerify() {
        final EasyEditDialog requestDialog = new EasyEditDialog(this);
        requestDialog.setEditTextMaxLength(32);
        requestDialog.setTitle(getString(R.string.add_friend_verify_tip));
        requestDialog.addNegativeButtonListener(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
            }
        });
        requestDialog.addPositiveButtonListener(R.string.send, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
                String msg = requestDialog.getEditMessage();
                doAddFriend(msg, false);
            }
        });
        requestDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        requestDialog.show();
    }

    private void doAddFriend(String msg, boolean addDirectly) {
        if (!NetworkUtil.isNetAvailable(this)) {
            Toast.makeText(FriendVerifyActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(account) && account.equals(DemoCache.getAccount())) {
            Toast.makeText(FriendVerifyActivity.this, "不能加自己为好友", Toast.LENGTH_SHORT).show();
            return;
        }
        final VerifyType verifyType = addDirectly ? VerifyType.DIRECT_ADD : VerifyType.VERIFY_REQUEST;
        DialogMaker.showProgressDialog(this, "", true);
        NIMClient.getService(FriendService.class).addFriend(new AddFriendData(account, verifyType, msg))
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        DialogMaker.dismissProgressDialog();
                        updateUserOperatorView();
                        if (VerifyType.DIRECT_ADD == verifyType) {
                            Toast.makeText(FriendVerifyActivity.this, "添加好友成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FriendVerifyActivity.this, "添加好友请求发送成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                        if (code == 408) {
                            Toast.makeText(FriendVerifyActivity.this, R.string.network_is_not_available, Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FriendVerifyActivity.this, "on failed:" + code, Toast
                                    .LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                });

        Log.i(TAG, "onAddFriendByVerify");
    }

    private void onChat() {
        Log.i(TAG, "onChat");
        SessionHelper.startP2PSession(this, account);
    }

    @Override
    public void onItemClickSure(String userCode, String msg) {
        doAddFriend(msg, false);
    }

    @Override
    public void onItemClickSure(List<String> selected, String msg) {

    }
}
