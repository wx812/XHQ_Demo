package com.nim.redpacket.helper;

import android.app.Activity;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.nim.DemoCache;
import com.nim.redpacket.GrabRpCallBack;
import com.nim.redpacket.NimOpenRpCallback;
import com.nim.redpacket.RpOpenedMessageFilter;
import com.nim.redpacket.UpdateRpCallBack;

/**
 * Administrator
 * 2018/1/24
 * 16:24
 */
public class NimRedPacketClient {
    private static final String TAG = NimRedPacketClient.class.getName();

    //初始化
    public static void init(boolean register) {
        RpOpenedMessageFilter.registerObserveFilter(register);
    }

    //启动拆红包dialog
    public static void startOpenRpDialog(final Activity activity, final SessionTypeEnum sessionTypeEnum, final String redPacketId, final NimOpenRpCallback cb, final UpdateRpCallBack updateRpCallBack) {
        final NimUserInfo selfInfo = (NimUserInfo) NimUIKit.getUserInfoProvider().getUserInfo(DemoCache.getAccount());
        if (selfInfo == null) {
            return;
        }
        GrabRpCallBack callback = new GrabRpCallBack() {

            @Override
            public void updateRpResult(int rpStatus) {
                updateRpCallBack.updateRp(rpStatus);
            }

            @Override
            public void grabRpResult(boolean isGetDone) {//只有抢到红包才会回调这个方法,返回0,表示抢到了最后一个红包;返回1,表示抢到了一个红包,但不是最后一个。
                cb.sendMessage(selfInfo.getAccount(), redPacketId, isGetDone);
            }
        };
        if (sessionTypeEnum == SessionTypeEnum.P2P) {
            RedPacketClient.openSingleRp(activity, redPacketId, callback);
        } else if (sessionTypeEnum == SessionTypeEnum.Team) {
            RedPacketClient.openGroupRp(activity, redPacketId, callback);
        }
    }

}
