package com.xhq.demo.cmd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.xhq.demo.tools.netTools.NetUtils;

/**
 * Created by cheng on 2016/11/28.
 */
public class NetBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
           int netWorkState = com.xhq.demo.tools.netTools.NetUtils.getActiveNetworkType();
            if(netWorkState != NetUtils.NET_TYPE_NO){
                SocketManager.getInstance().reconnect();
            }
            ImEventFactory.getInstance().postNetStatuEvent(netWorkState);
        }
    }
}
