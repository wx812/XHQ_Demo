package com.netease.nim.uikit.face.faceapi;

import com.netease.nim.uikit.business.recent.TeamTypeEntity;

import common.commonsdk.http.HttpConfig;
import common.commonsdk.http.HttpResultEntity;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Administrator
 * 2017/10/14
 * 15:32
 */
public interface GetTeamTypeApi {

    String NAME = GetTeamTypeApi.class.getName();

    //根据云信群id列表获取群类型
    String GET_TYPE_BY_ID = "im/getTeamInfos";

    String MEATHOD_GET_TYPE_BY_ID = "getTeamInfos";

    @FormUrlEncoded
    @POST(HttpConfig.REST)
    Observable<HttpResultEntity<TeamTypeEntity>> getTeamInfos(@Field(HttpConfig.CMD) String cmd, @Field(HttpConfig.DATA) String data);
}
