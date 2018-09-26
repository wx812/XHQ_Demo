package com.netease.nim.uikit.face.faceapi;

import common.commonsdk.http.HttpConfig;
import common.commonsdk.http.HttpResultEntity;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Administrator
 * 2017/7/11
 * 9:39
 */
public interface UpdateFriendApi {
    String NAME = UpdateFriendApi.class.getName();
    //泡圈聊天--云信数据--同步好友
    String UPDATE_MY_FRIEND = "im/updateMIInfoByuserCode";

    String MEATHOD_UPDATE_MY_FRIEND = "updateMyFriendsByuserCode";

    @FormUrlEncoded
    @POST(HttpConfig.REST)
    Observable<HttpResultEntity<String>> updateMyFriendsByuserCode(@Field(HttpConfig.CMD) String cmd, @Field(HttpConfig.DATA) String data);

}
