package com.netease.nim.uikit.face.faceapi;

import android.content.Context;

import common.commonsdk.http.RequestEntity;
import common.commonsdk.http.domain.CommonCase;
import rx.Observable;

/**
 * Administrator
 * 2017/7/11
 * 9:44
 */
public class UpdateFriendCase extends CommonCase<String> {

    public UpdateFriendCase(Context context) {
        super(context);
    }

    @Override
    protected Observable<String> buildUseCaseObservable(RequestEntity requestEntity) {
        return requestPars(requestEntity);
    }

    public RequestEntity createParams(UpdateFriendParams params) {
        return new RequestEntity(UpdateFriendApi.UPDATE_MY_FRIEND, params, UpdateFriendApi.MEATHOD_UPDATE_MY_FRIEND, UpdateFriendApi.NAME);
    }

}

