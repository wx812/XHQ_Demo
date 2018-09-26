package com.netease.nim.uikit.face.faceapi;

import com.netease.nim.uikit.business.team.model.TeamTypeEntity;
import com.netease.nim.uikit.business.team.model.UserEntity;

import common.commonsdk.http.HttpConfig;
import common.commonsdk.http.HttpResultEntity;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Author: YuJunKui
 * Time:2017/9/5 19:43
 * Tips:
 */

public interface FaceApi {




    String NAME = FaceApi.class.getName();
    String FIND_USER_BY_USER_CODE = "user/findUserByUsercode";
    String Method_FIND_USER_BY_USER_CODE = "findUserByUsercode";

    @FormUrlEncoded
    @POST(HttpConfig.REST)
    Observable<HttpResultEntity<UserEntity>> findUserByUsercode(@Field(HttpConfig.CMD) String cmd, @Field(HttpConfig.DATA) String data);


    String GET_TEAM_INFO = "im/getTeamInfo";
    String METHOD_GET_TEAM_INFO = "getTeamInfo";

    @FormUrlEncoded
    @POST(HttpConfig.REST)
    Observable<HttpResultEntity<TeamTypeEntity>> getTeamInfo(@Field(HttpConfig.CMD) String cmd, @Field(HttpConfig.DATA) String data);


    String OPERATION_FACT_TEAM = "game/findCircleByTid";
    String METHOD_OPERATION_FACT_TEAM = "operationFactTeam";

    @FormUrlEncoded
    @POST(HttpConfig.REST)

    Observable<HttpResultEntity<String>> operationFactTeam(@Field(HttpConfig.CMD) String cmd, @Field(HttpConfig.DATA) String data);



}
