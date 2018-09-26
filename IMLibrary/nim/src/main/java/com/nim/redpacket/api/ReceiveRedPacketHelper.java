package com.nim.redpacket.api;

import android.content.Context;

import com.nim.redpacket.bean.ReceRedPacketEntity;

import common.commonsdk.http.DefaultSubscriberNetCallBack;
import common.commonsdk.http.SubscribeManager;

/**
 * author：shiyang
 * date: 2018/1/15
 */

public class ReceiveRedPacketHelper {

    public static void receiveRedPacket(Context context, final String redPacketId, String tag
            , final SubscriberNetCallBack<ReceRedPacketEntity> callBack) {
        ReceiveRedPacketCase redPacketCase = new ReceiveRedPacketCase(context);
        redPacketCase.execute(redPacketCase.createParams(redPacketId), callBack);
        SubscribeManager.getInstance().add(tag, redPacketCase);
    }

    public static class SubscriberNetCallBack<T> extends DefaultSubscriberNetCallBack<T> {
        @Override
        protected void onSuccess(T t) {

        }

        @Override
        protected void onFailure(Throwable throwable, String s, int httpResultEntity) {

        }
    }
}
