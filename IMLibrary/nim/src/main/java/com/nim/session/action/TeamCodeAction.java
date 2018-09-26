package com.nim.session.action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.nim.R;

/**
 * Administrator
 * 2017/10/20
 * 10:08
 */
public class TeamCodeAction extends BaseAction {

    public TeamCodeAction() {
        super(R.drawable.message_plus_qunerweima_selector, R.string.input_panel_team_code);
    }

    @Override
    public void onClick() {
        startTeamCode(getActivity(), getAccount());
    }

    private void startTeamCode(Context context, String teamid) {
        try {
            if (context instanceof Activity) {
                Uri uri = Uri.parse("smartcity://team/open_groupcode?teamid=" + teamid);
                Activity currentActivity = (Activity) context;
                Intent intent = new Intent("ShareTeamCodeActivity", uri);
                currentActivity.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}