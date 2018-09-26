package com.nim.redpacket.api;

import com.nim.redpacket.bean.ReceRedPacketEntity;
import com.nim.redpacket.bean.RedPacketDetailInfoEntity;

import common.commonsdk.http.HttpConfig;
import common.commonsdk.http.HttpResultEntity;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * author：shiyang
 * date: 2018/1/15
 */

public interface PacketApi {
    String NAME = PacketApi.class.getName();
    //红包-查看红包
    String GET_RED_PACKET_BY_ID = "redPacket/selRedPacketById";
    String GETREDPACKETDETAILDATA = "getRedPacketDetailData";

    @FormUrlEncoded
    @POST(HttpConfig.REST)
    Observable<HttpResultEntity<RedPacketDetailInfoEntity>> getRedPacketDetailData(@Field(HttpConfig.CMD) String cmd, @Field(HttpConfig.DATA) String data);


    //红包-领取红包
    String RECEIVE_RED_PACKET = "redPacket/receiveRedPacket";
    String RECEIVEREDPACKET = "receiveRedPacket";

    @FormUrlEncoded
    @POST(HttpConfig.REST)
    Observable<HttpResultEntity<ReceRedPacketEntity>> receiveRedPacket(@Field(HttpConfig.CMD) String cmd, @Field(HttpConfig.DATA) String data);

    //红包-用户是否可以领取红包(点开聊天信息的红包，进入打开红包页面)
    String CAN_RECEIVE_RED_PACKET = "redPacket/canReceRedPacket";
    String MEATHOD_CAN_RECEIVE_RED_PACKET = "canReceRedPacket";

    @FormUrlEncoded
    @POST(HttpConfig.REST)
    Observable<HttpResultEntity<ReceRedPacketEntity>> canReceRedPacket(@Field(HttpConfig.CMD) String cmd, @Field(HttpConfig.DATA) String data);

}
