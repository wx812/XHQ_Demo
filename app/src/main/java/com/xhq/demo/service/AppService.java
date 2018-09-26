package com.xhq.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class AppService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        SocketManager.getInstance().init(this);
        return START_STICKY_COMPATIBILITY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        SocketManager.getInstance().close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
