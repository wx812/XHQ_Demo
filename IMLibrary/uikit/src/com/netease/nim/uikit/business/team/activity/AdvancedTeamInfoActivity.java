package com.netease.nim.uikit.business.team.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.api.model.team.TeamDataChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.session.actions.PickImageAction;
import com.netease.nim.uikit.business.team.adapter.TeamMemberAdapter;
import com.netease.nim.uikit.business.team.adapter.TeamMemberAdapter.TeamMemberItem;
import com.netease.nim.uikit.business.team.helper.AnnouncementHelper;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.business.team.model.Announcement;
import com.netease.nim.uikit.business.team.ui.TeamInfoGridView;
import com.netease.nim.uikit.business.team.viewholder.TeamMemberHolder;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.adapter.TAdapterDelegate;
import com.netease.nim.uikit.common.adapter.TViewHolder;
import com.netease.nim.uikit.common.media.picker.PickImageHelper;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.MenuDialog;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.widget.SwitchButton;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nim.uikit.face.FaceHelper;
import com.netease.nim.uikit.impl.cache.TeamDataCache;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.nos.NosService;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamBeInviteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamInviteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamMemberType;
import com.netease.nimlib.sdk.team.constant.TeamMessageNotifyTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.commonsdk.auto.compiler.DissolveCircleApiCase;
import common.commonsdk.auto.compiler.ExitCircleApiCase;
import common.commonsdk.autousecase.lifecycle.Job;
import common.commonsdk.http.DefaultSubscriberNetCallBack;

/**
 * 高级群群资料页
 * <p>
 * 默认不显示(暂时，当请求完成则会判断是否显示) 转让群功能
 * 当是否是脸圈群的接口完成  则会判断是否显示
 * Created by huangjun on 2015/3/17.
 */
public class AdvancedTeamInfoActivity extends UI implements
        TAdapterDelegate, TeamMemberAdapter.AddMemberCallback, TeamMemberHolder.TeamMemberHolderEventListener {

    private static final int REQUEST_CODE_MEMBER_LIST = 102;
    private static final int REQUEST_CODE_CONTACT_SELECT = 103;
    private static final int REQUEST_PICK_ICON = 104;

    private static final int ICON_TIME_OUT = 30000;

    // constant
    private static final String TAG = "RegularTeamInfoActivity";

    private static final String EXTRA_ID = "EXTRA_ID";
    public static final String RESULT_EXTRA_REASON = "RESULT_EXTRA_REASON";
    public static final String RESULT_EXTRA_REASON_QUIT = "RESULT_EXTRA_REASON_QUIT";
    public static final String RESULT_EXTRA_REASON_DISMISS = "RESULT_EXTRA_REASON_DISMISS";

    private static final int TEAM_MEMBERS_SHOW_LIMIT = 5;

    // data
    private TeamMemberAdapter adapter;
    private String teamId;
    private Team team;
    private String creator;
    private List<String> memberAccounts;
    private List<TeamMember> members;
    private List<TeamMemberItem> dataSource;
    private MenuDialog dialog;
    private List<String> managerList;
    private UserInfoObserver userInfoObserver;
    private AbortableFuture<String> uploadFuture;

    // view
    private View headerLayout;
    private HeadImageView teamHeadImage;
    private TextView teamNameText;
    private TextView teamIdText;
    private TextView teamCreateTimeText;

    private TextView teamBusinessCard; // 我的群名片

    private View layoutMime;
    private View layoutTeamMember;
    private TeamInfoGridView gridView;
    private View layoutTeamName;
    private View layoutTeamIntroduce;
    private View layoutTeamAnnouncement;

    private SwitchButton switchButton;

    private TextView memberCountText;
    private TextView introduceEdit;
    private TextView announcementEdit;

    // state
    private boolean isSelfAdmin = false;
    private boolean isSelfManager = false;

    private RelativeLayout rl_background;

    //是否是脸圈聊天群   null没有请求成功（包含请求失败）
    private Boolean isFaceTeam = null;
    private RelativeLayout team_advanced_quit;
    private TextView tv_chat_team;
    private View layoutNotificationConfig;
    private TextView notificationConfigText;
    private MenuDialog teamNotifyDialog;

    public static void start(Context context, String tid) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID, tid);
        intent.setClass(context, AdvancedTeamInfoActivity.class);
        context.startActivity(intent);
    }

    /**
     * ************************ TAdapterDelegate **************************
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
        return TeamMemberHolder.class;
    }

    @Override
    public boolean enabled(int position) {
        return false;
    }

    /**
     * ***************************** Life cycle *****************************
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nim_advanced_team_info_activity);

        ToolBarOptions options = new ToolBarOptions();
        setToolBar(R.id.toolbar, options);

        parseIntentData();
        findViews();
        initActionbar();
        initAdapter();
        loadTeamInfo();
        requestMembers();
        registerObservers(true);

        //请求这个群是否是 脸圈群
        FaceHelper.queryTeamIsFace(this, teamId, TAG
                , new FaceHelper.SubscriberNetCallBack<Boolean>() {
                    @Override
                    protected void onSuccess(Boolean aBoolean) {
                        super.onSuccess(aBoolean);
                        isFaceTeam = aBoolean;
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_CONTACT_SELECT:
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                if (selected != null && !selected.isEmpty()) {
                    inviteMembers(selected);
                }


                if (isFaceTeam == null) {
                    FaceHelper.queryTeamIsFace(this, teamId, TAG
                            , new FaceHelper.SubscriberNetCallBack<Boolean>() {
                                @Override
                                protected void onSuccess(Boolean aBoolean) {
                                    super.onSuccess(aBoolean);
                                    isFaceTeam = aBoolean;
                                    sendJoinTeam(selected);
                                }
                            });
                } else {
                    sendJoinTeam(selected);
                }

                break;
            case AdvancedTeamNicknameActivity.REQ_CODE_TEAM_NAME:
                setBusinessCard(data.getStringExtra(AdvancedTeamNicknameActivity.EXTRA_NAME));
                break;
            case AdvancedTeamMemberInfoActivity.REQ_CODE_REMOVE_MEMBER:
                boolean isSetAdmin = data.getBooleanExtra(AdvancedTeamMemberInfoActivity.EXTRA_ISADMIN, false);
                boolean isRemoveMember = data.getBooleanExtra(AdvancedTeamMemberInfoActivity.EXTRA_ISREMOVE, false);
                String account = data.getStringExtra(EXTRA_ID);
                refreshAdmin(isSetAdmin, account);
                if (isRemoveMember) {
                    removeMember(account);
                }
                break;
            case REQUEST_CODE_MEMBER_LIST:
                boolean isMemberChange = data.getBooleanExtra(AdvancedTeamMemberActivity.EXTRA_DATA, false);
                if (isMemberChange) {
                    requestMembers();
                }
                break;
            case REQUEST_PICK_ICON:
                String path = data.getStringExtra(com.netease.nim.uikit.business.session.constant.Extras.EXTRA_FILE_PATH);
                updateTeamIcon(path);
                break;
            default:
                break;
        }
    }

    public void sendJoinTeam(ArrayList<String> userCodes) {

        if (isFaceTeam == null || isFaceTeam == false) return;

        if (team.getTeamBeInviteMode() == TeamBeInviteModeEnum.NoAuth) {
            //不需要验证直接入群
            List<Map<String, String>> userCodesList = new ArrayList<>(userCodes.size());
            for (String usercode : userCodes) {
                Map<String, String> map = new HashMap<>(1);
                map.put("usercode", usercode);
                userCodesList.add(map);
            }

        }
    }

    @Override
    protected void onDestroy() {
        if (dialog != null) {
            dialog.dismiss();
        }

        registerObservers(false);

        super.onDestroy();
    }

    private void parseIntentData() {
        teamId = getIntent().getStringExtra(EXTRA_ID);
    }

    private void findViews() {
        headerLayout = findViewById(R.id.team_info_header);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelector(R.string.set_head_image, REQUEST_PICK_ICON);
            }
        });

        teamHeadImage = (HeadImageView) findViewById(R.id.team_head_image);
        teamNameText = (TextView) findViewById(R.id.team_name);
        teamIdText = (TextView) findViewById(R.id.team_id);
        teamCreateTimeText = (TextView) findViewById(R.id.team_create_time);

        layoutMime = findViewById(R.id.team_mime_layout);
        ((TextView) layoutMime.findViewById(R.id.item_title)).setText(R.string.my_team_nickname);
        teamBusinessCard = (TextView) layoutMime.findViewById(R.id.item_detail);
        layoutMime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedTeamNicknameActivity.start(AdvancedTeamInfoActivity.this, teamBusinessCard.getText().toString());
            }
        });

        layoutTeamMember = findViewById(R.id.team_memeber_layout);
        ((TextView) layoutTeamMember.findViewById(R.id.item_title)).setText(R.string.team_member);
        memberCountText = (TextView) layoutTeamMember.findViewById(R.id.item_detail);
        gridView = (TeamInfoGridView) findViewById(R.id.team_member_grid_view);
        layoutTeamMember.setVisibility(View.GONE);
        gridView.setVisibility(View.GONE);
        memberCountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if (isFaceTeam != null && isFaceTeam) {
                    AdvancedFaceTeamMemberActivity.startActivityForResult(AdvancedTeamInfoActivity.this, teamId, REQUEST_CODE_MEMBER_LIST);
                } else {
                    AdvancedTeamMemberActivity.startActivityForResult(AdvancedTeamInfoActivity.this, teamId, REQUEST_CODE_MEMBER_LIST);
                }*/
                AdvancedFaceTeamMemberActivity.startActivityForResult(AdvancedTeamInfoActivity.this, teamId, REQUEST_CODE_MEMBER_LIST);
            }
        });

        layoutTeamName = findViewById(R.id.team_name_layout);
        ((TextView) layoutTeamName.findViewById(R.id.item_title)).setText(R.string.team_name);
        layoutTeamName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelfAdmin) {
                    TeamPropertySettingActivity.start(AdvancedTeamInfoActivity.this, teamId, TeamFieldEnum.Name, team.getName());
                } else {
                    Toast.makeText(AdvancedTeamInfoActivity.this, "您不是群主,无法修改群名称", Toast.LENGTH_SHORT).show();
                }
            }
        });

        layoutTeamIntroduce = findViewById(R.id.team_introduce_layout);
        ((TextView) layoutTeamIntroduce.findViewById(R.id.item_title)).setText(R.string.team_introduce);
        introduceEdit = ((TextView) layoutTeamIntroduce.findViewById(R.id.item_detail));
        introduceEdit.setHint(R.string.team_introduce_hint);
        layoutTeamIntroduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelfAdmin) {
                    TeamPropertySettingActivity.start(AdvancedTeamInfoActivity.this, teamId, TeamFieldEnum.Introduce, team.getIntroduce());
                } else {
                    Toast.makeText(AdvancedTeamInfoActivity.this, "您不是群主,无法发布群介绍", Toast.LENGTH_SHORT).show();
                }
            }
        });

        layoutTeamAnnouncement = findViewById(R.id.team_announcement_layout);
        ((TextView) layoutTeamAnnouncement.findViewById(R.id.item_title)).setText(R.string.team_annourcement);
        announcementEdit = ((TextView) layoutTeamAnnouncement.findViewById(R.id.item_detail));
        announcementEdit.setHint(R.string.team_announce_hint);
        layoutTeamAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedTeamAnnounceActivity.start(AdvancedTeamInfoActivity.this, teamId, isSelfAdmin);
            }
        });

        rl_background = (RelativeLayout) this.findViewById(R.id.rl_background);
        rl_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.chat.activity.SetBackgroundActivity");
                startActivity(intent);
                finish();
            }
        });

        team_advanced_quit = (RelativeLayout) this.findViewById(R.id.rl_team_advanced_quit_item);
        tv_chat_team = findViewById(R.id.quit_chat_team);
        team_advanced_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelfAdmin) {
                    //群主 解散群聊
                    if (isFaceTeam != null && isFaceTeam) {
                        //脸圈
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdvancedTeamInfoActivity.this);
                        builder.setMessage("解散脸圈后将会清除相关信息，重新上传照片将会为您创建新的脸圈")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dismissTeam();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create().show();
                    } else {
                        //其他群 直接解散
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdvancedTeamInfoActivity.this);
                        builder.setMessage("确认解散群聊?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dismissTeam();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create().show();
                    }
                } else {
                    //退出群聊
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdvancedTeamInfoActivity.this);
                    builder.setMessage("确认退出群聊?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    quitTeam();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create().show();
                }

            }
        });
        initNotify();
    }

    /**
     * 打开图片选择器
     */
    private void showSelector(int titleId, final int requestCode) {
        PickImageHelper.PickImageOption option = new PickImageHelper.PickImageOption();
        option.titleResId = titleId;
        option.multiSelect = false;
        option.crop = true;
        option.cropOutputImageWidth = 720;
        option.cropOutputImageHeight = 720;

        PickImageHelper.pickImage(AdvancedTeamInfoActivity.this, requestCode, option);
    }

    /**
     * 群消息提醒设置
     */
    private void initNotify() {
        // 群消息提醒设置
        ((TextView) findViewById(R.id.team_notification_config_layout).findViewById(R.id.user_profile_title)).setText("消息提醒");
        switchButton = (SwitchButton) findViewById(R.id.team_notification_config_layout).findViewById(R.id.user_profile_toggle);
        switchButton.setOnChangedListener(new SwitchButton.OnChangedListener() {
            @Override
            public void OnChanged(View v, final boolean checkState) {
                if (!NetworkUtil.isNetAvailable(AdvancedTeamInfoActivity.this)) {
                    Toast.makeText(AdvancedTeamInfoActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                    switchButton.setCheck(!checkState);
                    return;
                }

                final TeamMessageNotifyTypeEnum notifyType = team.getMessageNotifyType();

                if (notifyType == null) {
                    return;
                }
                NIMClient.getService(TeamService.class).muteTeam(teamId, notifyType == TeamMessageNotifyTypeEnum.All ? TeamMessageNotifyTypeEnum.Mute : TeamMessageNotifyTypeEnum.All).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        if (checkState) {
                            Toast.makeText(AdvancedTeamInfoActivity.this, "开启消息提醒", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdvancedTeamInfoActivity.this, "关闭消息提醒", Toast.LENGTH_SHORT).show();
                        }

                        switchButton.setCheck(team.getMessageNotifyType() != TeamMessageNotifyTypeEnum.Mute);
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 408) {
                            Toast.makeText(AdvancedTeamInfoActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdvancedTeamInfoActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                        }

                        switchButton.setCheck(team.getMessageNotifyType() != TeamMessageNotifyTypeEnum.Mute);
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
            }
        });

        /*layoutNotificationConfig = findViewById(R.id.team_notification_config_layout);
        ((TextView) layoutNotificationConfig.findViewById(R.id.item_title)).setText(R.string.team_notification_config);
        notificationConfigText = (TextView) layoutNotificationConfig.findViewById(R.id.item_detail);
        layoutNotificationConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTeamNotifyMenu();
            }
        });*/

    }

    private void showTeamNotifyMenu() {
        if (teamNotifyDialog == null) {
            List<String> btnNames = TeamHelper.createNotifyMenuStrings();
            int type = team.getMessageNotifyType().getValue();
            teamNotifyDialog = new MenuDialog(AdvancedTeamInfoActivity.this, btnNames, type, 3, new MenuDialog
                    .MenuDialogOnButtonClickListener() {
                @Override
                public void onButtonClick(String name) {
                    teamNotifyDialog.dismiss();

                    TeamMessageNotifyTypeEnum type = TeamHelper.getNotifyType(name);
                    if (type == null) {
                        return;
                    }
                    DialogMaker.showProgressDialog(AdvancedTeamInfoActivity.this, getString(R.string.empty), true);
                    NIMClient.getService(TeamService.class).muteTeam(teamId, type).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            DialogMaker.dismissProgressDialog();
                            updateTeamNotifyText(team.getMessageNotifyType());
                        }

                        @Override
                        public void onFailed(int code) {
                            DialogMaker.dismissProgressDialog();
                            teamNotifyDialog.undoLastSelect();
                            Log.d(TAG, "muteTeam failed code:" + code);
                        }

                        @Override
                        public void onException(Throwable exception) {
                            DialogMaker.dismissProgressDialog();
                        }
                    });
                }
            });
        }
        teamNotifyDialog.show();
    }

    private void updateTeamNotifyText(TeamMessageNotifyTypeEnum typeEnum) {
        if (typeEnum == TeamMessageNotifyTypeEnum.All) {
            notificationConfigText.setText(getString(R.string.team_notify_all));
        } else if (typeEnum == TeamMessageNotifyTypeEnum.Manager) {
            notificationConfigText.setText(getString(R.string.team_notify_manager));
        } else if (typeEnum == TeamMessageNotifyTypeEnum.Mute) {
            notificationConfigText.setText(getString(R.string.team_notify_mute));
        }
    }

    private void initActionbar() {
        TextView toolbarView = findView(R.id.action_bar_right_clickable_textview);
        toolbarView.setText(R.string.menu);
        toolbarView.setVisibility(View.GONE);
        toolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegularTeamMenu();
            }
        });
    }

    private void initAdapter() {
        memberAccounts = new ArrayList<>();
        members = new ArrayList<>();
        dataSource = new ArrayList<>();
        managerList = new ArrayList<>();
        adapter = new TeamMemberAdapter(this, dataSource, this, null, this);
        adapter.setEventListener(this);

        gridView.setSelector(R.color.transparent);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        gridView.setAdapter(adapter);
    }

    /**
     * 初始化群组基本信息
     */
    private void loadTeamInfo() {
        Team t = TeamDataCache.getInstance().getTeamById(teamId);
        if (t != null) {
            updateTeamInfo(t);
        } else {
            TeamDataCache.getInstance().fetchTeamById(teamId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result, int code) {
                    if (success && result != null) {
                        updateTeamInfo(result);
                    } else {
                        onGetTeamInfoFailed();
                    }
                }
            });
        }
    }

    private void onGetTeamInfoFailed() {
        Toast.makeText(this, getString(R.string.team_not_exist), Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 更新群信息
     *
     * @param t
     */
    private void updateTeamInfo(final Team t) {
        this.team = t;

        if (team == null) {
            Toast.makeText(this, getString(R.string.team_not_exist), Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            creator = team.getCreator();
            if (creator.equals(NimUIKit.getAccount())) {
                isSelfAdmin = true;
            }

            setTitle(team.getName());
        }

        teamHeadImage.loadTeamIconByTeam(team);
        teamNameText.setText(team.getName());
        teamIdText.setText(TeamHelper.getTeamMemberDisplayName(team.getId(), creator));
        teamCreateTimeText.setText(TimeUtil.getDateTimeString(team.getCreateTime(), "yyyy/MM/dd"));

        ((TextView) layoutTeamName.findViewById(R.id.item_detail)).setText(team.getName());
        introduceEdit.setText(team.getIntroduce());

        //消息提醒设置 TeamMessageNotifyTypeEnum.All 可提醒 TeamMessageNotifyTypeEnum.mute 不提醒
        switchButton.setCheck(team.getMessageNotifyType() != TeamMessageNotifyTypeEnum.Mute);

        memberCountText.setText(String.format("共%d人", team.getMemberCount()));

        setAnnouncement(team.getAnnouncement());
    }

    /**
     * 更新群成员信息
     *
     * @param m
     */
    private void updateTeamMember(final List<TeamMember> m) {
        if (m != null && m.isEmpty()) {
            return;
        }

        updateTeamBusinessCard(m);
        addTeamMembers(m, true);
    }

    /**
     * 更新我的群名片
     *
     * @param m
     */
    private void updateTeamBusinessCard(List<TeamMember> m) {
        for (TeamMember teamMember : m) {
            if (teamMember != null && teamMember.getAccount().equals(NimUIKit.getAccount())) {
                teamBusinessCard.setText(teamMember.getTeamNick() != null ? teamMember.getTeamNick() : "");
            }
        }
    }

    /**
     * 添加群成员到列表
     *
     * @param m     群成员列表
     * @param clear 是否清除
     */
    private void addTeamMembers(final List<TeamMember> m, boolean clear) {
        if (m == null || m.isEmpty()) {
            return;
        }

        isSelfManager = false;
        isSelfAdmin = false;

        if (clear) {
            this.members.clear();
            this.memberAccounts.clear();
        }

        // add
        if (this.members.isEmpty()) {
            this.members.addAll(m);
        } else {
            for (TeamMember tm : m) {
                if (!this.memberAccounts.contains(tm.getAccount())) {
                    this.members.add(tm);
                }
            }
        }

        // sort
        Collections.sort(this.members, TeamHelper.teamMemberComparator);

        // accounts, manager, creator
        this.memberAccounts.clear();
        this.managerList.clear();
        for (TeamMember tm : members) {
            if (tm == null) {
                continue;
            }
            if (tm.getType() == TeamMemberType.Manager) {
                managerList.add(tm.getAccount());
            }
            if (tm.getAccount().equals(NimUIKit.getAccount())) {
                if (tm.getType() == TeamMemberType.Manager) {
                    isSelfManager = true;
                } else if (tm.getType() == TeamMemberType.Owner) {
                    isSelfAdmin = true;
                    creator = NimUIKit.getAccount();
                }
            }
            this.memberAccounts.add(tm.getAccount());
        }

        showTeamTagInfo();
        updateAuthenView();
        updateTeamMemberDataSource();
    }

    /**
     * 更新身份验证是否显示
     */
    private void updateAuthenView() {
        if (isSelfAdmin || isSelfManager) {
            announcementEdit.setHint(R.string.without_content);
        } else {
            introduceEdit.setHint(R.string.without_content);
            announcementEdit.setHint(R.string.without_content);
        }
    }

    /**
     * 更新成员信息
     */
    private void updateTeamMemberDataSource() {
        if (members.size() > 0) {
            gridView.setVisibility(View.VISIBLE);
            layoutTeamMember.setVisibility(View.VISIBLE);
        } else {
            gridView.setVisibility(View.GONE);
            layoutTeamMember.setVisibility(View.GONE);
            return;
        }

        dataSource.clear();

        // add item
        if (team.getTeamInviteMode() == TeamInviteModeEnum.All || isSelfAdmin || isSelfManager) {
            dataSource.add(new TeamMemberItem(TeamMemberAdapter.TeamMemberItemTag.ADD, null, null,
                    null));
        }

        // member item
        int count = 0;
        String identity = null;
        for (String account : memberAccounts) {
            int limit = TEAM_MEMBERS_SHOW_LIMIT;
            if (team.getTeamInviteMode() == TeamInviteModeEnum.All || isSelfAdmin || isSelfManager) {
                limit = TEAM_MEMBERS_SHOW_LIMIT - 1;
            }
            if (count < limit) {
                identity = getIdentity(account);
                dataSource.add(new TeamMemberItem(TeamMemberAdapter.TeamMemberItemTag
                        .NORMAL, teamId, account, identity));
            }
            count++;
        }

        // refresh
        adapter.notifyDataSetChanged();
        memberCountText.setText(String.format("共%d人", count));
    }

    private String getIdentity(String account) {
        String identity;
        if (creator.equals(account)) {
            identity = TeamMemberHolder.OWNER;
        } else if (managerList.contains(account)) {
            identity = TeamMemberHolder.ADMIN;
        } else {
            identity = null;
        }
        return identity;
    }

    /**
     * *************************** 加载&变更数据源 ********************************
     */
    private void requestMembers() {
        TeamDataCache.getInstance().fetchTeamMemberList(teamId, new SimpleCallback<List<TeamMember>>() {
            @Override
            public void onResult(boolean success, List<TeamMember> members, int code) {
                if (success && members != null && !members.isEmpty()) {
                    updateTeamMember(members);
                }
            }
        });
    }

    /**
     * ************************** 群信息变更监听 **************************
     */
    /**
     * 注册群信息更新监听
     *
     * @param register
     */
    private void registerObservers(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberObserver, register);
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataObserver, register);
        registerUserInfoChangedObserver(register);
    }

    TeamMemberDataChangedObserver teamMemberObserver = new TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> m) {
            for (TeamMember mm : m) {
                for (TeamMember member : members) {
                    if (mm.getAccount().equals(member.getAccount())) {
                        members.set(members.indexOf(member), mm);
                        break;
                    }
                }
            }
            addTeamMembers(m, false);
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> members) {
            for (TeamMember member : members) {
                removeMember(member.getAccount());
            }
        }
    };

    TeamDataChangedObserver teamDataObserver = new TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            for (Team team : teams) {
                if (team.getId().equals(teamId)) {
                    updateTeamInfo(team);
                    updateTeamMemberDataSource();
                    break;
                }
            }
        }

        @Override
        public void onRemoveTeam(Team team) {
            if (team.getId().equals(teamId)) {
                AdvancedTeamInfoActivity.this.team = team;
                finish();
            }
        }
    };

    /**
     * ******************************* Action *********************************
     */

    /**
     * 从联系人选择器发起邀请成员
     */
    @Override
    public void onAddMember() {
        ContactSelectActivity.Option option = TeamHelper.getContactSelectOption(memberAccounts);
        NimUIKit.startContactSelector(AdvancedTeamInfoActivity.this, option, REQUEST_CODE_CONTACT_SELECT);
    }

    /**
     * 邀请群成员
     *
     * @param accounts 邀请帐号
     */
    private void inviteMembers(ArrayList<String> accounts) {
        NIMClient.getService(TeamService.class).addMembers(teamId, accounts).setCallback(new RequestCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> failedAccounts) {
                if (failedAccounts == null || failedAccounts.isEmpty()) {
                    Toast.makeText(AdvancedTeamInfoActivity.this, "添加群成员成功", Toast.LENGTH_SHORT).show();
                } else {
                    TeamHelper.onMemberTeamNumOverrun(failedAccounts, AdvancedTeamInfoActivity.this);
                }
            }

            @Override
            public void onFailed(int code) {
                if (code == ResponseCode.RES_TEAM_INVITE_SUCCESS) {
                    Toast.makeText(AdvancedTeamInfoActivity.this, R.string.team_invite_members_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdvancedTeamInfoActivity.this, "invite members failed, code=" + code, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "invite members failed, code=" + code);
                }
            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }

    /**
     * 非群主退出群
     */
    private void quitTeam() {

        DialogMaker.showProgressDialog(this, getString(R.string.loading), true);
        Job.get(this, ExitCircleApiCase.class).execute(new Object[]{NimUIKit.getAccount(),teamId,NimUIKit.getAccount(),creator}, new DefaultSubscriberNetCallBack<String>() {
            @Override
            protected void onFailure(Throwable throwable, String s, int i) {
                DialogMaker.dismissProgressDialog();
                Toast.makeText(AdvancedTeamInfoActivity.this, R.string.quit_team_failed, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onSuccess(String s) {
                DialogMaker.dismissProgressDialog();
                //Toast.makeText(AdvancedTeamInfoActivity.this, R.string.quit_team_success, Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK, new Intent().putExtra(RESULT_EXTRA_REASON, RESULT_EXTRA_REASON_QUIT));
                finish();
            }
        });
        
    }

    /**
     * 群主解散群(直接退出)
     */
    private void dismissTeam() {

        DialogMaker.showProgressDialog(this, getString(R.string.loading), true);

        Job.get(this, DissolveCircleApiCase.class).execute(new Object[]{NimUIKit.getAccount(),teamId,NimUIKit.getAccount()}, new DefaultSubscriberNetCallBack<String>() {
            @Override
            protected void onFailure(Throwable throwable, String s, int i) {
                DialogMaker.dismissProgressDialog();
                Toast.makeText(AdvancedTeamInfoActivity.this, R.string.dismiss_team_failed, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onSuccess(String s) {
                NIMClient.getService(TeamService.class).dismissTeam(teamId);

                DialogMaker.dismissProgressDialog();
                setResult(Activity.RESULT_OK, new Intent().putExtra(RESULT_EXTRA_REASON, RESULT_EXTRA_REASON_DISMISS));
                //Toast.makeText(AdvancedTeamInfoActivity.this, R.string.dismiss_team_success, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    /**
     * ******************************* Event *********************************
     */
    /**
     * 显示菜单
     */
    private void showRegularTeamMenu() {
        final List<String> btnNames = new ArrayList<>();
        if (isSelfAdmin) {
            btnNames.add(getString(R.string.dismiss_team));
            btnNames.add(getString(R.string.cancel));
        } else {
            btnNames.add(getString(R.string.quit_team));
            btnNames.add(getString(R.string.cancel));
        }
        dialog = new MenuDialog(this, btnNames, new MenuDialog.MenuDialogOnButtonClickListener() {
            @Override
            public void onButtonClick(String name) {
                if (name.equals(getString(R.string.quit_team))) {
                    quitTeam();
                } else if (name.equals(getString(R.string.dismiss_team))) {
                    if (isFaceTeam != null && isFaceTeam) {
                        //脸圈
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdvancedTeamInfoActivity.this);
                        builder.setMessage("解散脸圈后将会清除相关信息，重新上传照片将会为您创建新的脸圈")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dismissTeam();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create().show();
                    } else {
                        dismissTeam();
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * 显示退出群聊或解散群聊
     */
    private void showTeamTagInfo() {
        if (isSelfAdmin) {
            //解散
            tv_chat_team.setText("解散群聊");
        } else {
            //退出
            tv_chat_team.setText("退出群聊");
        }

    }

    /**
     * 设置我的名片
     *
     * @param nickname 群昵称
     */
    private void setBusinessCard(final String nickname) {
        DialogMaker.showProgressDialog(this, getString(R.string.empty), true);
        NIMClient.getService(TeamService.class).updateMemberNick(teamId, NimUIKit.getAccount(), nickname).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                DialogMaker.dismissProgressDialog();
                teamBusinessCard.setText(!TextUtils.isEmpty(nickname.trim()) ? nickname.trim() : "暂无");
                Toast.makeText(AdvancedTeamInfoActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code) {
                DialogMaker.dismissProgressDialog();
                Toast.makeText(AdvancedTeamInfoActivity.this, String.format(getString(R.string.update_failed), code),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onHeadImageViewClick(String account) {
        FaceHelper.startFaceDetailByUserCode(this, account);
    }

    /**
     * 设置群公告
     *
     * @param announcement 群公告
     */
    private void setAnnouncement(String announcement) {
        Announcement a = AnnouncementHelper.getLastAnnouncement(teamId, announcement);
        if (a == null) {
            announcementEdit.setText("");
        } else {
            announcementEdit.setText(a.getTitle());
        }
    }

    /**
     * 移除群成员成功后，删除列表中的群成员
     *
     * @param account 被删除成员帐号
     */
    private void removeMember(String account) {
        if (TextUtils.isEmpty(account)) {
            return;
        }

        memberAccounts.remove(account);

        for (TeamMember m : members) {
            if (m.getAccount().equals(account)) {
                members.remove(m);
                break;
            }
        }

        memberCountText.setText(String.format("共%d人", members.size()));

        for (TeamMemberItem item : dataSource) {
            if (item.getAccount() != null && item.getAccount().equals(account)) {
                dataSource.remove(item);
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 是否设置了管理员刷新界面
     *
     * @param isSetAdmin
     * @param account
     */
    private void refreshAdmin(boolean isSetAdmin, String account) {
        if (isSetAdmin) {
            if (managerList.contains(account)) {
                return;
            }
            managerList.add(account);
            updateTeamMemberDataSource();
        } else {
            if (managerList.contains(account)) {
                managerList.remove(account);
                updateTeamMemberDataSource();
            }
        }
    }

    private void registerUserInfoChangedObserver(boolean register) {
        if (register) {
            if (userInfoObserver == null) {
                userInfoObserver = new UserInfoObserver() {
                    @Override
                    public void onUserInfoChanged(List<String> accounts) {
                        adapter.notifyDataSetChanged();
                    }
                };
            }
            NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, true);
        } else {
            NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, false);
        }
    }

    /**
     * 更新头像
     */
    private void updateTeamIcon(final String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        File file = new File(path);
        if (file == null) {
            return;
        }
        DialogMaker.showProgressDialog(this, null, null, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelUpload(R.string.team_update_cancel);
            }
        }).setCanceledOnTouchOutside(true);

        LogUtil.i(TAG, "start upload icon, local file path=" + file.getAbsolutePath());
        new Handler().postDelayed(outimeTask, ICON_TIME_OUT);
        uploadFuture = NIMClient.getService(NosService.class).upload(file, PickImageAction.MIME_JPEG);
        uploadFuture.setCallback(new RequestCallbackWrapper<String>() {
            @Override
            public void onResult(int code, String url, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && !TextUtils.isEmpty(url)) {
                    LogUtil.i(TAG, "upload icon success, url =" + url);

                    NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.ICON, url).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            DialogMaker.dismissProgressDialog();
                            Toast.makeText(AdvancedTeamInfoActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
                            onUpdateDone();
                        }

                        @Override
                        public void onFailed(int code) {
                            DialogMaker.dismissProgressDialog();
                            if (code == ResponseCode.RES_TEAM_ENACCESS) {
                                Toast.makeText(AdvancedTeamInfoActivity.this, R.string.no_permission, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AdvancedTeamInfoActivity.this, String.format(getString(R.string.update_failed), code), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onException(Throwable exception) {
                            DialogMaker.dismissProgressDialog();
                        }
                    }); // 更新资料
                } else {
                    Toast.makeText(AdvancedTeamInfoActivity.this, R.string.team_update_failed, Toast
                            .LENGTH_SHORT).show();
                    onUpdateDone();
                }
            }
        });
    }

    private void cancelUpload(int resId) {
        if (uploadFuture != null) {
            uploadFuture.abort();
            Toast.makeText(AdvancedTeamInfoActivity.this, resId, Toast.LENGTH_SHORT).show();
            onUpdateDone();
        }
    }

    private Runnable outimeTask = new Runnable() {
        @Override
        public void run() {
            cancelUpload(R.string.team_update_failed);
        }
    };

    private void onUpdateDone() {
        uploadFuture = null;
        DialogMaker.dismissProgressDialog();
    }
}
