package com.nim.contact.activity;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.QueryUserInfo;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.dialog.EasyEditDialog;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.widget.SwitchButton;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nim.uikit.dialogfragment.AddFriendDialogFragment;
import com.netease.nim.uikit.face.FaceHelper;
import com.netease.nim.uikit.face.faceapi.UpdateFriendCase;
import com.netease.nim.uikit.face.faceapi.UpdateFriendParams;
import com.netease.nim.uikit.impl.cache.FriendDataCache;
import com.netease.nim.uikit.impl.cache.NimUserInfoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.FriendServiceObserve;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.friend.model.MuteListChangedNotify;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.nim.DemoCache;
import com.nim.R;
import com.nim.contact.constant.UserConstant;
import com.nim.main.model.Extras;
import com.nim.session.SessionHelper;
import com.smartcity.common.commonprovider.inter.NoDoubleClickListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.commonsdk.http.DefaultSubscriberNetCallBack;
import common.commonsdk.http.SubscribeManager;

/**
 * 用户资料页面
 * Created by huangjun on 2015/8/11.
 */
public class UserProfileActivity extends UI implements AddFriendDialogFragment.ItemClickCallBack {

    private static final String TAG = UserProfileActivity.class.getSimpleName();

    private static final String KEY_SYS_MSG = "sys_msg";

    private final boolean FLAG_ADD_FRIEND_DIRECTLY = false; // 是否直接加为好友开关，false为需要好友申请
    private final String KEY_BLACK_LIST = "black_list";
    private final String KEY_MSG_NOTICE = "msg_notice";

    private String account;

    // 基本信息
    private HeadImageView headImageView;
    private TextView nameText;
    private ImageView genderImage;
    private TextView birthdayText;
    private TextView emailText;
    private TextView signatureText;
    private TextView wordText;//留言
    private RelativeLayout birthdayLayout;
    private RelativeLayout emailLayout;
    private RelativeLayout signatureLayout;
    private RelativeLayout aliasLayout;
    private RelativeLayout wordLayout;
    private TextView nickText;

    // 开关
    private ViewGroup toggleLayout;
    private Button addFriendBtn;
    private Button removeFriendBtn;
    private Button chatBtn;
    private SwitchButton blackSwitch;
    private SwitchButton noticeSwitch;
    private Map<String, Boolean> toggleStateMap;

    private SystemMessage message;
    private Toolbar toolbar;

    public static void start(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, UserProfileActivity.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT, account);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void start(Context context, String account, SystemMessage message) {
        Intent intent = new Intent();
        intent.setClass(context, UserProfileActivity.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT, account);
        intent.putExtra(KEY_SYS_MSG, message);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);

       /* ToolBarOptions options = new ToolBarOptions();
        options.titleId = R.string.user_profile;
        setToolBar(R.id.toolbar, options);*/
        toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.title);

        toolbar.setTitle("");
        //toolbar.inflateMenu(R.menu.user_profile_menu);
        title.setText(getResources().getString(R.string.user_profile));
        setSupportActionBar(toolbar);
        setToolBarListener();

        Uri uri = getIntent().getData();
        if (uri != null) {
            account = uri.getQueryParameter("usercode");
        } else {
            account = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
            message = (SystemMessage) getIntent().getSerializableExtra(KEY_SYS_MSG);
        }

        initActionbar();

        findViews();
        registerObserver(true);
    }

    private void setToolBarListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!DemoCache.getAccount().equals(account) && (NIMClient.getService(FriendService.class).isMyFriend(account))) {
            menu.findItem(R.id.action_shield).setVisible(true);
        } else {
            menu.findItem(R.id.action_shield).setVisible(false);
        }
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
        addFriendBtn = findView(R.id.add_buddy);
        chatBtn = findView(R.id.begin_chat);
        removeFriendBtn = findView(R.id.remove_buddy);
        birthdayLayout = findView(R.id.birthday);
        nickText = findView(R.id.user_nick);
        birthdayText = (TextView) birthdayLayout.findViewById(R.id.value);
        emailLayout = findView(R.id.email);
        emailText = (TextView) emailLayout.findViewById(R.id.value);
        signatureLayout = findView(R.id.signature);
        signatureText = (TextView) signatureLayout.findViewById(R.id.value);
        aliasLayout = findView(R.id.alias);
        wordLayout = findView(R.id.words);
        wordText = (TextView) wordLayout.findViewById(R.id.value);
        ((TextView) birthdayLayout.findViewById(R.id.attribute)).setText(R.string.birthday);
        ((TextView) emailLayout.findViewById(R.id.attribute)).setText(R.string.email);
        ((TextView) signatureLayout.findViewById(R.id.attribute)).setText(R.string.signature);
        ((TextView) aliasLayout.findViewById(R.id.attribute)).setText(R.string.alias);
        ((TextView) wordLayout.findViewById(R.id.attribute)).setText(R.string.words);

        if (null != message) {
            if (!TextUtils.isEmpty(message.getContent())) {
                wordLayout.setVisibility(View.VISIBLE);
                wordText.setText(message.getContent());
            } else {
                wordLayout.setVisibility(View.GONE);
            }
        } else {
            wordLayout.setVisibility(View.GONE);
        }

        addFriendBtn.setOnClickListener(onClickListener);
        chatBtn.setOnClickListener(onClickListener);
        removeFriendBtn.setOnClickListener(onClickListener);
        aliasLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileEditItemActivity.startActivity(UserProfileActivity.this, UserConstant.KEY_ALIAS, account);
            }
        });

        headImageView.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                FaceHelper.startFaceDetailByUserCode(UserProfileActivity.this, account.toLowerCase());
            }
        });
    }

    private void initActionbar() {
        TextView toolbarView = findView(R.id.action_bar_right_clickable_textview);
        if (!DemoCache.getAccount().equals(account)) {
            toolbarView.setVisibility(View.GONE);
            return;
        } else {
            toolbarView.setVisibility(View.GONE);
        }
        toolbarView.setText(R.string.edit);
        toolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileSettingActivity.start(UserProfileActivity.this, account);
            }
        });
    }

    private void addToggleBtn(boolean black, boolean notice) {
        noticeSwitch = addToggleItemView(KEY_MSG_NOTICE, R.string.msg_notice, notice);
        blackSwitch = addToggleItemView(KEY_BLACK_LIST, R.string.black_list, black);
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

        if (!TextUtils.isEmpty(userInfo.getBirthday())) {
            birthdayLayout.setVisibility(View.VISIBLE);
            birthdayText.setText(userInfo.getBirthday());
        } else {
            birthdayLayout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(userInfo.getEmail())) {
            emailLayout.setVisibility(View.VISIBLE);
            emailText.setText(userInfo.getEmail());
        } else {
            emailLayout.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(userInfo.getSignature())) {
            signatureLayout.setVisibility(View.VISIBLE);
            signatureText.setText(userInfo.getSignature());
        } else {
            signatureLayout.setVisibility(View.GONE);
        }

    }

    private void updateUserOperatorView() {
        chatBtn.setVisibility(View.VISIBLE);
        if (NIMClient.getService(FriendService.class).isMyFriend(account)) {
            removeFriendBtn.setVisibility(View.VISIBLE);
            addFriendBtn.setVisibility(View.GONE);
            updateAlias(true);
        } else {
            addFriendBtn.setVisibility(View.VISIBLE);
            removeFriendBtn.setVisibility(View.GONE);
            updateAlias(false);
        }
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

    private void updateAlias(boolean isFriend) {
        if (isFriend) {
            aliasLayout.setVisibility(View.VISIBLE);
            aliasLayout.findViewById(R.id.arrow_right).setVisibility(View.VISIBLE);
            Friend friend = FriendDataCache.getInstance().getFriendByAccount(account);
            if (friend != null && !TextUtils.isEmpty(friend.getAlias())) {
                nickText.setVisibility(View.VISIBLE);
                nameText.setText(friend.getAlias());
                nickText.setText("昵称：" + NimUserInfoCache.getInstance().getUserName(account));
            } else {
                nickText.setVisibility(View.GONE);
                nameText.setText(NimUserInfoCache.getInstance().getUserName(account));
            }
        } else {
            aliasLayout.setVisibility(View.GONE);
            aliasLayout.findViewById(R.id.arrow_right).setVisibility(View.GONE);
            nickText.setVisibility(View.GONE);
            nameText.setText(NimUserInfoCache.getInstance().getUserName(account));
        }
    }

    private SwitchButton.OnChangedListener onChangedListener = new SwitchButton.OnChangedListener() {
        @Override
        public void OnChanged(View v, final boolean checkState) {
            final String key = (String) v.getTag();
            if (!NetworkUtil.isNetAvailable(UserProfileActivity.this)) {
                Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(UserProfileActivity.this, "加入黑名单成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(int code) {
                            if (code == 408) {
                                Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserProfileActivity.this, "on failed：" + code, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(UserProfileActivity.this, "移除黑名单成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(int code) {
                            if (code == 408) {
                                Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserProfileActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(UserProfileActivity.this, "开启消息提醒成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "关闭消息提醒成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 408) {
                            Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
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
            if (v == addFriendBtn) {
                if (FLAG_ADD_FRIEND_DIRECTLY) {
                    doAddFriend(null, true);  // 直接加为好友
                } else {
                    //onAddFriendByVerify(); // 发起好友验证请求
                    AddFriendDialogFragment dialogFragment = AddFriendDialogFragment.newInstance(account);
                    dialogFragment.setListener(UserProfileActivity.this);
                    dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
                    dialogFragment.show(UserProfileActivity.this.getSupportFragmentManager(), "dialog");
                }
            } else if (v == removeFriendBtn) {
                onRemoveFriend();
            } else if (v == chatBtn) {
                onChat();
            }
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
            Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(account) && account.equals(DemoCache.getAccount())) {
            Toast.makeText(UserProfileActivity.this, "不能加自己为好友", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(UserProfileActivity.this, "添加好友成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "添加好友请求发送成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                        if (code == 408) {
                            Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "on failed:" + code, Toast
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

    private void onRemoveFriend() {
        Log.i(TAG, "onRemoveFriend");
        if (!NetworkUtil.isNetAvailable(this)) {
            Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(this, getString(R.string.remove_friend),
                getString(R.string.remove_friend_tip), true,
                new EasyAlertDialogHelper.OnDialogActionListener() {

                    @Override
                    public void doCancelAction() {

                    }

                    @Override
                    public void doOkAction() {
                        DialogMaker.showProgressDialog(UserProfileActivity.this, "", true);
                        UpdateFriendCase updateFriendCase = new UpdateFriendCase(UserProfileActivity.this);
                        updateFriendCase.execute(updateFriendCase.createParams(new UpdateFriendParams(QueryUserInfo.getUserCode(UserProfileActivity.this), account, 1))
                                , new DefaultSubscriberNetCallBack<String>() {
                                    @Override
                                    protected void onFailure(Throwable throwable, String s, int code) {
                                        DialogMaker.dismissProgressDialog();
                                        Toast.makeText(UserProfileActivity.this, R.string.remove_friend_fail, Toast.LENGTH_SHORT).show();
                                    }


                                    @Override
                                    protected void onSuccess(String s) {
                                        DialogMaker.dismissProgressDialog();
                                        Toast.makeText(UserProfileActivity.this, R.string.remove_friend_success, Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                        SubscribeManager.getInstance().add(TAG, updateFriendCase);
                    }
                });
        if (!isFinishing() && !isDestroyedCompatible()) {
            dialog.show();
        }
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
