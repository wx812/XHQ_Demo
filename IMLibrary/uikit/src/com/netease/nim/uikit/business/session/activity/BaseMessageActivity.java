package com.netease.nim.uikit.business.session.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.business.recent.TeamType;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nim.uikit.business.team.model.TeamTypeEntity;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.face.FaceHelper;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.List;

import common.commonsdk.http.SubscribeManager;

/**
 * Created by zhoujianghua on 2015/9/10.
 */
public abstract class BaseMessageActivity extends UI {
    public static final String TAG = BaseMessageActivity.class.getName();

    protected String sessionId;

    private SessionCustomization customization;

    private MessageFragment messageFragment;

    protected abstract MessageFragment fragment();

    protected abstract int getContentViewId();

    protected abstract void initToolBar();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentViewId());
        initToolBar();
        parseIntent();

        messageFragment = (MessageFragment) switchContent(fragment());
    }

    @Override
    public void onBackPressed() {
        if (messageFragment == null || !messageFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (messageFragment != null) {
            messageFragment.onActivityResult(requestCode, resultCode, data);
        }

        if (customization != null) {
            customization.onActivityResult(this, requestCode, resultCode, data);
        }
    }

    private void parseIntent() {
        Uri uri = getIntent().getData();
        if (null != uri) {
            sessionId = uri.getQueryParameter("teamid");
            customization = NimUIKitImpl.getCommonTeamSessionCustomization();
            queryTeamType();
        } else {
            sessionId = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
            customization = (SessionCustomization) getIntent().getSerializableExtra(Extras.EXTRA_CUSTOMIZATION);
        }

        if (customization != null) {
            addRightCustomViewOnActionBar(this, customization.buttons);
        }
    }

    // 添加action bar的右侧按钮及响应事件
    private void addRightCustomViewOnActionBar(UI activity, List<SessionCustomization.OptionsButton> buttons) {
        if (buttons == null || buttons.size() == 0) {
            return;
        }

        Toolbar toolbar = getToolBar();
        if (toolbar == null) {
            return;
        }

        LinearLayout view = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.nim_action_bar_custom_view, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        for (final SessionCustomization.OptionsButton button : buttons) {
            ImageView imageView = new ImageView(activity);
            imageView.setImageResource(button.iconId);
            imageView.setBackgroundResource(R.drawable.nim_nim_action_bar_button_selector);
            imageView.setPadding(ScreenUtil.dip2px(10), 0, ScreenUtil.dip2px(10), 0);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button.onClick(BaseMessageActivity.this, v, sessionId);
                }
            });
            view.addView(imageView, params);
        }

        toolbar.addView(view, new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.RIGHT | Gravity.CENTER));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SubscribeManager.getInstance().cance(TAG);
    }

    private void queryTeamType() {  //1-粉团队；2-粉团队小组；3-玩家圈；4-店面讨论组；5-活动临时组；6-云店玩家圈;7-看脸圈
        FaceHelper.queryTeamInfo(this, sessionId, TAG, new FaceHelper.SubscriberNetCallBack<TeamTypeEntity>() {
            @Override
            protected void onFailure(Throwable throwable, String s, int code) {
                Toast.makeText(BaseMessageActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onSuccess(TeamTypeEntity entity) {
                if (null != entity) {
                    int detailType = entity.detailTypeId;
                    switch (detailType) {
                        case TeamType.TYPE_PLAY_CIRCLE:
                            try {
                                Uri uri = Uri.parse("smartCity://playCircle/circleDetailPage?circleId=" + entity.detailId + "");
                                Intent intent = new Intent("NewCircleDetailActivity", uri);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case TeamType.TYPE_FACE_CIRCLE:
                            try {
                                Uri uri = Uri.parse("smartcity://face/open_face_person_detail?otheruserid=" + entity.detailCreatePeople);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            applyJoinTeam(BaseMessageActivity.this, sessionId);
                            break;
                    }
                } else {
                    applyJoinTeam(BaseMessageActivity.this, sessionId);
                }
            }
        });
    }

    private void applyJoinTeam(final Context context, final String teamId) {
        NIMClient.getService(TeamService.class).applyJoinTeam(teamId, null).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {
                String toast = context.getString(R.string.team_join_success, team.getName());
                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code) {
                Log.e(TAG, "code=" + code);
                if (code == 808) {
                    Toast.makeText(context, R.string.team_apply_to_join_send_success,
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else if (code == 809) {
                    Toast.makeText(context, R.string.has_exist_in_team,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "failed, error code =" + code,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }
}
