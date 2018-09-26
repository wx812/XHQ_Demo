package com.netease.nim.uikit.business.recent;

import android.content.Context;

import com.netease.nim.uikit.face.faceapi.GetTeamTypeCase;

import common.commonsdk.http.DefaultSubscriberNetCallBack;
import common.commonsdk.http.SubscribeManager;

/**
 * Administrator
 * 2017/10/23
 * 16:08
 */
public class TeamTypeHelper {

    public static void queryTeamType(Context context, String detailtids, String tag, final SubscriberNetCallBack<TeamTypeEntity> callBack) {
        GetTeamTypeCase getTeamTypeCase = new GetTeamTypeCase(context);
        getTeamTypeCase.execute(getTeamTypeCase.createTypeByIdParams(detailtids), callBack);
        SubscribeManager.getInstance().add(tag, getTeamTypeCase);
    }

    public static class SubscriberNetCallBack<T> extends DefaultSubscriberNetCallBack<T> {

        @Override
        protected void onFailure(Throwable throwable, String s, int httpResultEntity) {

        }

        @Override
        protected void onSuccess(T o) {

        }
    }
}
