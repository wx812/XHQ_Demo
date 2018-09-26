package com.nim.main.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.QueryUserInfo;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.adapter.TAdapterDelegate;
import com.netease.nim.uikit.common.adapter.TViewHolder;
import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.listview.AutoRefreshListView;
import com.netease.nim.uikit.common.ui.listview.ListViewUtil;
import com.netease.nim.uikit.common.ui.listview.MessageListView;
import com.netease.nim.uikit.face.FaceHelper;
import com.netease.nim.uikit.face.faceapi.UpdateFriendCase;
import com.netease.nim.uikit.face.faceapi.UpdateFriendParams;
import com.netease.nim.uikit.impl.cache.NimUserInfoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.model.AddFriendNotify;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SystemMessageStatus;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.nim.R;
import com.nim.contact.activity.FriendVerifyActivity;
import com.nim.contact.activity.UserProfileActivity;
import com.nim.main.adapter.SystemMessageAdapter;
import com.nim.main.helper.MessageHelper;
import com.nim.main.viewholder.SystemMessageViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.commonsdk.auto.compiler.JoinCircleApiCase;
import common.commonsdk.autousecase.lifecycle.Job;
import common.commonsdk.http.DefaultSubscriberNetCallBack;
import common.commonsdk.http.SubscribeManager;


/**
 * 系统消息中心界面
 * <p/>
 * Created by huangjun on 2015/3/18.
 */
public class SystemMessageActivity extends BaseChatUI implements TAdapterDelegate,
        AutoRefreshListView.OnRefreshListener, SystemMessageViewHolder.SystemMessageListener {

    private static final String TAG = SystemMessageActivity.class.getSimpleName();

    private static final boolean MERGE_ADD_FRIEND_VERIFY = true; // 是否要合并好友申请，同一个用户仅保留最近一条申请内容（默认不合并）

    private static final int LOAD_MESSAGE_COUNT = 10;

    private RelativeLayout rl_data;
    private ImageView iv_logo;

    // view
    private MessageListView listView;

    // adapter
    private SystemMessageAdapter adapter;
    private List<SystemMessage> items = new ArrayList<>();
    private Set<Long> itemIds = new HashSet<>();

    // db
    private boolean firstLoad = true;

    public static void start(Context context) {
        start(context, null, true);
    }

    public static void start(Context context, Intent extras, boolean clearTop) {
        Intent intent = new Intent();
        intent.setClass(context, SystemMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (clearTop) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
        return SystemMessageViewHolder.class;
    }

    @Override
    public boolean enabled(int position) {
        return false;
    }


    @Override
    public void onRefreshFromStart() {
    }

    @Override
    public void onRefreshFromEnd() {
        loadMessages(); // load old data
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.system_notification_message_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.title);

        toolbar.setTitle("");
        title.setText(getResources().getString(R.string.verify_reminder));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.fanhui));

        rl_data = findViewById(R.id.rl_data);
        iv_logo = findViewById(R.id.iv_logo);
        iv_logo.setImageResource(R.mipmap.wuxiaoxi);

        initAdapter();
        initListView();

        loadMessages(); // load old data
        registerSystemObserver(true);
        registerEventBusObserver(true);
    }

    private void registerEventBusObserver(boolean b) {
        if (b) {
            EventBus.getDefault().register(this);
        } else {
            EventBus.getDefault().unregister(this);
        }
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
    protected void onResume() {
        super.onResume();

        NIMClient.getService(SystemMessageService.class).resetSystemMessageUnreadCount();
    }

    @Override
    protected void onStop() {
        super.onStop();

        NIMClient.getService(SystemMessageService.class).resetSystemMessageUnreadCount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SubscribeManager.getInstance().cance(TAG);
        registerSystemObserver(false);
        registerEventBusObserver(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshMessage(SystemMessage message) {
        refreshViewHolder(message);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notification_s_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.notification_menu_btn) {
            showSureDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SystemMessageActivity.this);
        builder.setMessage("确认清除所有消息吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllMessages();
                    }
                })
                .setNegativeButton("取消", null)
                .create().show();
    }

    private void initAdapter() {
        adapter = new SystemMessageAdapter(this, items, this, this);
    }

    private void initListView() {
        listView = (MessageListView) findViewById(R.id.messageListView);
        listView.setMode(AutoRefreshListView.Mode.END);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }

        // adapter
        listView.setAdapter(adapter);

        // listener
        listView.setOnRefreshListener(this);
    }


    private int loadOffset = 0;
    private Set<String> addFriendVerifyRequestAccounts = new HashSet<>(); // 发送过好友申请的账号（好友申请合并用）

    /**
     * 加载历史消息
     */
    public void loadMessages() {
        listView.onRefreshStart(AutoRefreshListView.Mode.END);
        boolean loadCompleted; // 是否已经加载完成，后续没有数据了or已经满足本次请求数量
        int validMessageCount = 0; // 实际加载的数量（排除被过滤被合并的条目）
        List<String> messageFromAccounts = new ArrayList<>(LOAD_MESSAGE_COUNT);

        while (true) {
            List<SystemMessage> temps = NIMClient.getService(SystemMessageService.class)
                    .querySystemMessagesBlock(loadOffset, LOAD_MESSAGE_COUNT);

            loadOffset += temps.size();
            loadCompleted = temps.size() < LOAD_MESSAGE_COUNT;

            int tempValidCount = 0;

            for (SystemMessage m : temps) {
                // 去重
                if (duplicateFilter(m)) {
                    continue;
                }

                // 同一个账号的好友申请仅保留最近一条
                if (addFriendVerifyFilter(m)) {
                    continue;
                }

                // 保存有效消息
                items.add(m);
                tempValidCount++;
                if (!messageFromAccounts.contains(m.getFromAccount())) {
                    messageFromAccounts.add(m.getFromAccount());
                }

                // 判断是否达到请求数
                if (++validMessageCount >= LOAD_MESSAGE_COUNT) {
                    loadCompleted = true;
                    // 已经满足要求，此时需要修正loadOffset
                    loadOffset -= temps.size();
                    loadOffset += tempValidCount;

                    break;
                }
            }

            if (loadCompleted) {
                break;
            }
        }

        // 更新数据源，刷新界面
        refresh();

        boolean first = firstLoad;
        firstLoad = false;
        if (first) {
            ListViewUtil.scrollToPosition(listView, 0, 0); // 第一次加载后显示顶部
        }

        listView.onRefreshComplete(validMessageCount, LOAD_MESSAGE_COUNT, true);

        // 收集未知用户资料的账号集合并从远程获取
        collectAndRequestUnknownUserInfo(messageFromAccounts);
    }

    /**
     * 新消息到来
     */
    private void onIncomingMessage(final SystemMessage message) {
        // 同一个账号的好友申请仅保留最近一条
        if (addFriendVerifyFilter(message)) {
            SystemMessage del = null;
            for (SystemMessage m : items) {
                if (m.getFromAccount().equals(message.getFromAccount()) && m.getType() == SystemMessageType.AddFriend) {
                    AddFriendNotify attachData = (AddFriendNotify) m.getAttachObject();
                    if (attachData != null && attachData.getEvent() == AddFriendNotify.Event.RECV_ADD_FRIEND_VERIFY_REQUEST) {
                        del = m;
                        break;
                    }
                }
            }

            if (del != null) {
                items.remove(del);
            }
        }

        loadOffset++;
        items.add(0, message);

        refresh();

        // 收集未知用户资料的账号集合并从远程获取
        List<String> messageFromAccounts = new ArrayList<>(1);
        messageFromAccounts.add(message.getFromAccount());
        collectAndRequestUnknownUserInfo(messageFromAccounts);
    }


    // 去重
    private boolean duplicateFilter(final SystemMessage msg) {
        if (itemIds.contains(msg.getMessageId())) {
            return true;
        }

        itemIds.add(msg.getMessageId());
        return false;
    }

    // 同一个账号的好友申请仅保留最近一条
    private boolean addFriendVerifyFilter(final SystemMessage msg) {
        if (!MERGE_ADD_FRIEND_VERIFY) {
            return false; // 不需要MERGE，不过滤
        }

        if (msg.getType() != SystemMessageType.AddFriend) {
            return false; // 不过滤
        }

        AddFriendNotify attachData = (AddFriendNotify) msg.getAttachObject();
        if (attachData == null) {
            return true; // 过滤
        }

        if (attachData.getEvent() != AddFriendNotify.Event.RECV_ADD_FRIEND_VERIFY_REQUEST) {
            return false; // 不过滤
        }

        if (addFriendVerifyRequestAccounts.contains(msg.getFromAccount())) {
            return true; // 过滤
        }

        addFriendVerifyRequestAccounts.add(msg.getFromAccount());
        return false; // 不过滤
    }

    // 请求未知的用户资料
    private void collectAndRequestUnknownUserInfo(List<String> messageFromAccounts) {
        List<String> unknownAccounts = new ArrayList<>();
        for (String account : messageFromAccounts) {
            if (!NimUserInfoCache.getInstance().hasUser(account)) {
                unknownAccounts.add(account);
            }
        }

        if (!unknownAccounts.isEmpty()) {
            requestUnknownUser(unknownAccounts);
        }
    }

    @Override
    public void onAgree(SystemMessage message) {
        onSystemNotificationDeal(message, true);
    }

    @Override
    public void onReject(SystemMessage message) {
        onSystemNotificationDeal(message, false);
    }

    @Override
    public void onLongPressed(SystemMessage message) {
        showLongClickMenu(message);
    }

    @Override
    public void onPressed(SystemMessage message) {
        if (!NIMClient.getService(FriendService.class).isMyFriend(message.getFromAccount())) {
            //query(message);
            FriendVerifyActivity.start(this, message.getFromAccount(), message);
        } else {
            if (message.getStatus() == SystemMessageStatus.init) {
                //未处理的消息
                if (MessageHelper.isVerifyMessageNeedDeal(message)) {
                    //是好友验证类型的消息 才会跳好友验证页面
                    FriendVerifyActivity.start(this, message.getFromAccount(), message);
                } else {
                    FaceHelper.startFaceDetailByUserCode(this, message.getFromAccount());
                }

            } else {
                //处理过的消息才跳脸圈
                FaceHelper.startFaceDetailByUserCode(this, message.getFromAccount());
            }
        }
    }

    private void onSystemNotificationDeal(final SystemMessage message, final boolean pass) {
        RequestCallback callback = new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                onProcessSuccess(pass, message);
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

                NIMClient.getService(TeamService.class).searchTeam(message.getTargetId()).setCallback(new RequestCallback<Team>() {
                        @Override
                        public void onSuccess(Team param) {
                            Job.get(this, JoinCircleApiCase.class).execute(new Object[]{message.getFromAccount(), message.getTargetId(), NimUIKit.getAccount(),param.getCreator(),param.getName()}, new DefaultSubscriberNetCallBack<String>() {
                                @Override
                                protected void onFailure(Throwable throwable, String s, int i) {
                                    DialogMaker.dismissProgressDialog();
                                    onProcessFailed(0, message);
                                }

                                @Override
                                protected void onSuccess(String s) {
                                    DialogMaker.dismissProgressDialog();
                                    onProcessSuccess(pass, message);
                                }
                            });
                        }

                        @Override
                        public void onFailed(int code) {
                    }

                    @Override
                    public void onException(Throwable exception) {
                        onProcessSuccess(pass, message);
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
                UpdateFriendCase updateFriendCase = new UpdateFriendCase(SystemMessageActivity.this);
                updateFriendCase.execute(updateFriendCase.createParams(new UpdateFriendParams(QueryUserInfo.getUserCode(SystemMessageActivity.this), message.getFromAccount(), 0))
                        , new DefaultSubscriberNetCallBack<String>() {
                            @Override
                            protected void onFailure(Throwable throwable, String s, int code) {
                                Toast.makeText(SystemMessageActivity.this, s, Toast.LENGTH_SHORT).show();
                            }


                            @Override
                            protected void onSuccess(String s) {
                                Log.e(TAG, "s=" + s);
                                onProcessSuccess(pass, message);
                            }
                        });
                SubscribeManager.getInstance().add(TAG, updateFriendCase);
            } else {
                NIMClient.getService(FriendService.class).ackAddFriendRequest(message.getFromAccount(), false).setCallback(callback);
            }
        }
    }

    private void onProcessSuccess(final boolean pass, SystemMessage message) {
        SystemMessageStatus status = pass ? SystemMessageStatus.passed : SystemMessageStatus.declined;
        NIMClient.getService(SystemMessageService.class).setSystemMessageStatus(message.getMessageId(),
                status);
        message.setStatus(status);
        refreshViewHolder(message);
    }

    private void onProcessFailed(final int code, SystemMessage message) {
        Log.e(TAG, "failed, error code=" + code);
        Toast.makeText(SystemMessageActivity.this, "已过期", Toast.LENGTH_LONG).show();
        if (code == 408) {
            return;
        }

        SystemMessageStatus status = SystemMessageStatus.expired;
        NIMClient.getService(SystemMessageService.class).setSystemMessageStatus(message.getMessageId(),
                status);
        message.setStatus(status);
        refreshViewHolder(message);
    }

    private void refresh() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                if (adapter.getCount() > 0) {
                    rl_data.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    rl_data.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void refreshViewHolder(final SystemMessage message) {
        final long messageId = message.getMessageId();

        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            SystemMessage item = items.get(i);
            if (messageId == item.getMessageId()) {
                index = i;
                break;
            }
        }

        if (index < 0) {
            return;
        }

        final int m = index;
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (m < 0) {
                    return;
                }

                Object tag = ListViewUtil.getViewHolderByIndex(listView, m);
                if (tag instanceof SystemMessageViewHolder) {
                    ((SystemMessageViewHolder) tag).refreshDirectly(message);
                }
            }
        });
    }

    private void registerSystemObserver(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeReceiveSystemMsg(systemMessageObserver, register);
    }

    Observer<SystemMessage> systemMessageObserver = new Observer<SystemMessage>() {
        @Override
        public void onEvent(SystemMessage systemMessage) {
            onIncomingMessage(systemMessage);
        }
    };

    private void requestUnknownUser(List<String> accounts) {
        NimUserInfoCache.getInstance().getUserInfoFromRemote(accounts, new RequestCallbackWrapper<List<NimUserInfo>>() {
            @Override
            public void onResult(int code, List<NimUserInfo> users, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS) {
                    if (users != null && !users.isEmpty()) {
                        refresh();
                    }
                }
            }
        });
    }

    private void deleteAllMessages() {
        NIMClient.getService(SystemMessageService.class).clearSystemMessages();
        NIMClient.getService(SystemMessageService.class).resetSystemMessageUnreadCount();
        items.clear();
        refresh();
        Toast.makeText(SystemMessageActivity.this, R.string.clear_all_success, Toast.LENGTH_SHORT).show();
    }

    private void showLongClickMenu(final SystemMessage message) {
        CustomAlertDialog alertDialog = new CustomAlertDialog(this);
        alertDialog.setTitle(R.string.delete_tip);
        String title = getString(R.string.delete_system_message);
        alertDialog.addItem(title, new CustomAlertDialog.onSeparateItemClickListener() {
            @Override
            public void onClick() {
                deleteSystemMessage(message);
            }
        });
        alertDialog.show();
    }

    private void deleteSystemMessage(final SystemMessage message) {
        NIMClient.getService(SystemMessageService.class).deleteSystemMessage(message.getMessageId());
        items.remove(message);
        refresh();
        Toast.makeText(SystemMessageActivity.this, R.string.delete_success, Toast.LENGTH_SHORT).show();
    }

    private void query(final SystemMessage message) {
        DialogMaker.showProgressDialog(this, null, false);
        NimUserInfoCache.getInstance().getUserInfoFromRemote(message.getFromAccount(), new RequestCallback<NimUserInfo>() {
            @Override
            public void onSuccess(NimUserInfo user) {
                DialogMaker.dismissProgressDialog();
                if (user == null) {
                    EasyAlertDialogHelper.showOneButtonDiolag(SystemMessageActivity.this, R.string.user_not_exsit,
                            R.string.user_tips, R.string.ok, false, null);
                } else {
                    UserProfileActivity.start(SystemMessageActivity.this, message.getFromAccount(), message);
                }
            }

            @Override
            public void onFailed(int code) {
                DialogMaker.dismissProgressDialog();
                if (code == 408) {
                    Toast.makeText(SystemMessageActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SystemMessageActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
                Toast.makeText(SystemMessageActivity.this, "on exception:" + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
