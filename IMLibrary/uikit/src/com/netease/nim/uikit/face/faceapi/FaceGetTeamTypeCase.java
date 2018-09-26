package com.netease.nim.uikit.face.faceapi;

import android.content.Context;

import com.netease.nim.uikit.business.team.model.TeamTypeEntity;

import java.util.HashMap;
import java.util.Map;

import common.commonsdk.http.RequestEntity;
import common.commonsdk.http.domain.CommonCase;
import rx.Observable;

/**
 * Administrator
 * 2017/7/11
 * 9:44
 */
public class FaceGetTeamTypeCase extends CommonCase<TeamTypeEntity> {

    public FaceGetTeamTypeCase(Context context) {
        super(context);
    }

    @Override
    protected Observable<TeamTypeEntity> buildUseCaseObservable(RequestEntity requestEntity) {
        return requestPars(requestEntity);
    }

    public RequestEntity createParams(String teamId) {
        Map<String, String> params = new HashMap<>(1);
        params.put("teamId", teamId);
        return new RequestEntity(FaceApi.GET_TEAM_INFO
                , params, FaceApi.METHOD_GET_TEAM_INFO, FaceApi.NAME);
    }
}

