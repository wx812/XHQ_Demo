package com.netease.nim.uikit.business.session.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.team.TeamDataChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.netease.nim.uikit.business.recent.TeamType;
import com.netease.nim.uikit.business.recent.TeamTypeEntity;
import com.netease.nim.uikit.business.recent.TeamTypeHelper;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nim.uikit.business.session.fragment.TeamMessageFragment;
import com.netease.nim.uikit.business.team.event.MessageTeamListUpdateEvent;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.face.FaceHelper;
import com.netease.nim.uikit.impl.cache.TeamDataCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import common.commonsdk.http.SubscribeManager;

/**
 * 群聊界面
 * <p/>
 * Created by huangjun on 2015/3/5.
 */
public class TeamMessageActivity extends BaseMessageActivity {
    private static final String TAG = TeamMessageActivity.class.getName();
    // model
    private Team team;

    private View invalidTeamTipView;

    private TextView invalidTeamTipText;

    private TeamMessageFragment fragment;

    private Class<? extends Activity> backToClass;

    //是否是脸圈聊天群
    private boolean isFaceTeam = false;


    public static void start(Context context, String tid, SessionCustomization customization,
                             Class<? extends Activity> backToClass, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, tid);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        intent.putExtra(Extras.EXTRA_BACK_TO_CLASS, backToClass);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, TeamMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(intent);
    }

    protected void findViews() {
        invalidTeamTipView = findView(R.id.invalid_team_tip);
        invalidTeamTipText = findView(R.id.invalid_team_text);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        backToClass = (Class<? extends Activity>) getIntent().getSerializableExtra(Extras.EXTRA_BACK_TO_CLASS);
        findViews();
        queryTeamType();
        registerTeamUpdateObserver(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        registerTeamUpdateObserver(false);

        SubscribeManager.getInstance().cance(TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestTeamInfo();

        //请求这个群是否是 脸圈群
        if (team != null && team.isMyTeam()) {
            //是否在这个群里
            FaceHelper.queryTeamIsFace(this, getIntent().getStringExtra(Extras.EXTRA_ACCOUNT), TAG
                    , new FaceHelper.SubscriberNetCallBack<Boolean>() {
                        @Override
                        protected void onSuccess(Boolean aBoolean) {
                            super.onSuccess(aBoolean);
                            isFaceTeam = aBoolean;
                        }
                    });
        }
    }

    /**
     * 请求群基本信息
     */
    private void requestTeamInfo() {
        // 请求群基本信息
        Team t = NIMClient.getService(TeamService.class).queryTeamBlock(sessionId);
        if (t != null) {
            TeamDataCache.getInstance().addOrUpdateTeam(t);
            updateTeamInfo(t);
        } else {
            TeamDataCache.getInstance().fetchTeamById(sessionId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result, int code) {
                    if (success && result != null) {
                        updateTeamInfo(result);
                    } else {
                        onRequestTeamInfoFailed();
                    }
                }
            });

        }
    }

    private void onRequestTeamInfoFailed() {
        Toast.makeText(TeamMessageActivity.this, "获取群组信息失败!", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 更新群信息
     *
     * @param d
     */
    private void updateTeamInfo(final Team d) {
        if (d == null) {
            return;
        }

        team = d;
        fragment.setTeam(team);

        setTitle(team == null ? sessionId : team.getName() + "(" + team.getMemberCount() + "人)");

        invalidTeamTipText.setText(team.getType() == TeamTypeEnum.Normal ? R.string.normal_team_invalid_tip : R.string.team_invalid_tip);
        invalidTeamTipView.setVisibility(team.isMyTeam() ? View.GONE : View.VISIBLE);
        if (!team.isMyTeam()) {
            //不在群里，直接退出
            EventBus.getDefault().post(new MessageTeamListUpdateEvent());
            finish();
        }
    }

    /**
     * 注册群信息更新监听
     *
     * @param register
     */
    private void registerTeamUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register);
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver, register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
    }

    /**
     * 群资料变动通知和移除群的通知（包括自己退群和群被解散）
     */
    TeamDataChangedObserver teamDataChangedObserver = new TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            if (team == null) {
                return;
            }
            for (Team t : teams) {
                if (t.getId().equals(team.getId())) {
                    updateTeamInfo(t);
                    break;
                }
            }
        }

        @Override
        public void onRemoveTeam(Team team) {
            if (team == null) {
                return;
            }
            if (team.getId().equals(TeamMessageActivity.this.team.getId())) {
                updateTeamInfo(team);
            }
        }
    };

    /**
     * 群成员资料变动通知和移除群成员通知
     */
    TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            fragment.refreshMessageList();
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> member) {
        }
    };

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            fragment.refreshMessageList();
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            fragment.refreshMessageList();
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            fragment.refreshMessageList();
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            fragment.refreshMessageList();
        }
    };

    @Override
    protected MessageFragment fragment() {
        // 添加fragment
        Bundle arguments = getIntent().getExtras();
        if (null != arguments) {
            arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.Team);
            fragment = new TeamMessageFragment();
            fragment.setArguments(arguments);
            fragment.setContainerId(R.id.message_fragment_container);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(Extras.EXTRA_ACCOUNT, sessionId);
            bundle.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.Team);
            fragment = new TeamMessageFragment();
            fragment.setArguments(bundle);
            fragment.setContainerId(R.id.message_fragment_container);
        }
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_team_message_activity;
    }

    @Override
    protected void initToolBar() {
        ToolBarOptions options = new ToolBarOptions();
        options.titleString = "群聊";
        options.logoId = R.drawable.taolunzubiaoshi;
        setToolBar(R.id.toolbar, options);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (backToClass != null) {
            Intent intent = new Intent();
            intent.setClass(this, backToClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    public boolean isFaceTeam() {
        return isFaceTeam;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void queryTeamType() {  //1-粉团队；2-粉团队小组；3-玩家圈；4-店面讨论组；5-活动临时组；6-云店玩家圈;7-看脸圈
        TeamTypeHelper.queryTeamType(this, sessionId, TAG, new TeamTypeHelper.SubscriberNetCallBack<TeamTypeEntity>() {
            @Override
            protected void onFailure(Throwable throwable, String s, int code) {
                Toast.makeText(TeamMessageActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onSuccess(TeamTypeEntity entity) {
                if (null != entity) {
                    List<TeamTypeEntity.TeamTypesBean> teamTypes = entity.getTeamTypes();
                    if (null != teamTypes && teamTypes.size() > 0) {
                        TeamTypeEntity.TeamTypesBean typesBean = teamTypes.get(0);
                        int detailType = typesBean.getDetailtType();
                        switch (detailType) {
                            case TeamType.TYPE_PLAY_CIRCLE:
                                int orgId = typesBean.getOrgId();
                                if (orgId != 0) {
                                    setLogo(R.drawable.sq_guanliyuan);
                                } else {
                                    setLogo(R.drawable.quanzibiaoshi);
                                }
                                break;
                            case TeamType.TYPE_FACE_CIRCLE:
                                setLogo(R.drawable.lianquanbiaoshi);
                                break;
                            default:
                                setLogo(R.drawable.taolunzubiaoshi);
                                break;
                        }
                    }
                } else {
                    setLogo(R.drawable.taolunzubiaoshi);
                }
            }
        });
    }
}
