package com.netease.nim.uikit.face.faceapi;

import android.content.Context;

import com.netease.nim.uikit.face.faceapi.FaceApi;

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
public class FaceTeamOperationCase extends CommonCase<String> {

    public FaceTeamOperationCase(Context context) {
        super(context);
    }

    @Override
    protected Observable<String> buildUseCaseObservable(RequestEntity requestEntity) {
        return requestPars(requestEntity);
    }

    /**
     * 解散群
     */
    public RequestEntity createDissolutionTeamParams(String Tid, String userCode) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("type", 1);
        params.put("Tid", Tid);
        params.put("usercode", userCode);
        return new RequestEntity(FaceApi.OPERATION_FACT_TEAM
                , params, FaceApi.METHOD_OPERATION_FACT_TEAM, FaceApi.NAME);
    }


    /**
     * 退出群
     */
    public RequestEntity createExitTeamParams(String Tid, String userCode) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("type", 2);
        params.put("Tid", Tid);
        params.put("usercode", userCode);
        return new RequestEntity(FaceApi.OPERATION_FACT_TEAM
                , params, FaceApi.METHOD_OPERATION_FACT_TEAM, FaceApi.NAME);
    }


    /**
     * 踢人
     *
     * @param userCode 被T人
     */
    public RequestEntity createTeamKickParams(String Tid, String userCode) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("type", 3);
        params.put("Tid", Tid);
        params.put("usercode", userCode);
        return new RequestEntity(FaceApi.OPERATION_FACT_TEAM
                , params, FaceApi.METHOD_OPERATION_FACT_TEAM, FaceApi.NAME);
    }

    /**
     * 修改名字
     */
    public RequestEntity createUpdataTeamNameParams(String Tid, String detailName) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("type", 4);
        params.put("Tid", Tid);
        params.put("detailName", detailName);
        return new RequestEntity(FaceApi.OPERATION_FACT_TEAM
                , params, FaceApi.METHOD_OPERATION_FACT_TEAM, FaceApi.NAME);
    }


}

