package com.nim.redpacket.api;

import android.content.Context;

import com.nim.redpacket.bean.ReceRedPacketEntity;

import java.util.HashMap;
import java.util.Map;

import common.commonsdk.Injection;
import common.commonsdk.http.RequestEntity;
import common.commonsdk.http.domain.CommonCase;
import rx.Observable;

/**
 * Administrator
 * 2018/2/2
 * 10:47
 */
public class CanReceRedPacketCase extends CommonCase<ReceRedPacketEntity> {

    public CanReceRedPacketCase(Context context) {
        super(context);
    }

    @Override
    protected Observable<ReceRedPacketEntity> buildUseCaseObservable(RequestEntity requestEntity) {
        return requestPars(requestEntity);
    }

    public RequestEntity createParams(String redPacketId) {
        Map<String, String> params = new HashMap<>(2);
        params.put("redPacketId", redPacketId);
        params.put("userId", Injection.provideSp().getUserId());
        return new RequestEntity(PacketApi.CAN_RECEIVE_RED_PACKET
                , params, PacketApi.MEATHOD_CAN_RECEIVE_RED_PACKET, PacketApi.NAME);
    }
}
