package com.nim.session;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Administrator
 * 2018/1/18
 * 9:05
 */
public class RedPacketRouter {
    private static final String TAG = RedPacketRouter.class.getName();

    //群聊红包详情
    public static void startRedPacketDetailActivity(Activity activity, String redPacketId ,String state) {
        Uri uri = Uri.parse("smartcity://chat/redpacket_detail?redPacketId=" +
                redPacketId+"&state="+state);
        Intent intent = new Intent("RedPacketDetailActivity", uri);
        activity.startActivity(intent);
    }

    //P2P红包详情
    public static void startP2PRedPacketDetailActivity(Activity activity, String redPacketId,String state) {
        Uri uri = Uri.parse("smartcity://chat/p2p_redpacket_detail?redPacketId=" +
                redPacketId+"&state="+state);
        Intent intent = new Intent("P2PRedPacketDetailActivity", uri);
        activity.startActivity(intent);
    }


}
