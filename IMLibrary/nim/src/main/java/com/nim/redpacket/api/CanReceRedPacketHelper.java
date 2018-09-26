package com.nim.redpacket.api;

import android.content.Context;

import com.nim.redpacket.bean.ReceRedPacketEntity;

import common.commonsdk.http.DefaultSubscriberNetCallBack;
import common.commonsdk.http.SubscribeManager;

/**
 * Administrator
 * 2018/2/2
 * 10:55
 */
public class CanReceRedPacketHelper {

    public static void canReceRedPacket(Context context, final String redPacketId, String tag
            , final SubscriberNetCallBack<ReceRedPacketEntity> callBack) {
        CanReceRedPacketCase redPacketCase = new CanReceRedPacketCase(context);
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
