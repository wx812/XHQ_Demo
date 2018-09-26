package com.nim.redpacket.api;

import android.content.Context;

import com.nim.redpacket.bean.RedPacketDetailInfoEntity;

import java.util.HashMap;
import java.util.Map;

import common.commonsdk.Injection;
import common.commonsdk.http.RequestEntity;
import common.commonsdk.http.domain.CommonCase;
import rx.Observable;

/**
 * author：shiyang
 * date: 2018/1/15
 */

public class ChatGetRedPacketDetailCase extends CommonCase<RedPacketDetailInfoEntity> {

    public ChatGetRedPacketDetailCase(Context context) {
        super(context);
    }

    @Override
    protected Observable<RedPacketDetailInfoEntity> buildUseCaseObservable(RequestEntity requestEntity) {
        return requestPars(requestEntity);
    }

    public RequestEntity createParams(String redPacketId) {
        Map<String, String> params = new HashMap<>(2);
        params.put("redPacketId", redPacketId);
        params.put("userId", Injection.provideSp().getUserId());
        return new RequestEntity(PacketApi.GET_RED_PACKET_BY_ID
                , params, PacketApi.GETREDPACKETDETAILDATA, PacketApi.NAME);
    }
}
