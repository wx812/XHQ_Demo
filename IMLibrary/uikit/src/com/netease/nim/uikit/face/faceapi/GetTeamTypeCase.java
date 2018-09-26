package com.netease.nim.uikit.face.faceapi;

import android.content.Context;

import com.netease.nim.uikit.business.recent.TeamTypeEntity;

import java.util.HashMap;
import java.util.Map;

import common.commonsdk.Injection;
import common.commonsdk.http.RequestEntity;
import common.commonsdk.http.domain.CommonCase;
import rx.Observable;

/**
 * Administrator
 * 2017/10/14
 * 15:33
 */
public class GetTeamTypeCase extends CommonCase<TeamTypeEntity> {

    public GetTeamTypeCase(Context context) {
        super(context);
    }

    @Override
    protected Observable<TeamTypeEntity> buildUseCaseObservable(RequestEntity requestEntity) {
        return requestPars(requestEntity);
    }

    public RequestEntity createTypeByIdParams(String detailtids) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("personUserId", Injection.provideSp().getUserId());
        params.put("detailtids", detailtids);
        return new RequestEntity(GetTeamTypeApi.GET_TYPE_BY_ID
                , params, GetTeamTypeApi.MEATHOD_GET_TYPE_BY_ID, GetTeamTypeApi.NAME);
    }

}