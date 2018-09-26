package com.nim.redpacket.api;

import android.content.Context;

import com.nim.redpacket.bean.RedPacketDetailInfoEntity;

import common.commonsdk.http.DefaultSubscriberNetCallBack;
import common.commonsdk.http.SubscribeManager;

/**
 * author：shiyang
 * date: 2018/1/15
 */

public class RedPacketHelper {

    public static void getRedPacketDetail(Context context, final String redPacketId, String tag
            , final SubscriberNetCallBack<RedPacketDetailInfoEntity> callBack) {
        ChatGetRedPacketDetailCase redPacketCase = new ChatGetRedPacketDetailCase(context);
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
