package com.xhq.demo.tools.appTools;

import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;

import com.xhq.demo.HomeApp;
import com.xhq.demo.tools.ToastUtils;


public class DoubleClickExitApp {

    private boolean isOnKeyBack;    // 是否是再次点击back键
    private Handler mHandler;

    private Runnable onBackExitRunnable = new Runnable() {
        @Override
        public void run() {
            isOnKeyBack = false;
            ToastUtils.cancelToast();
        }
    };

    public DoubleClickExitApp() {
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 所在Activity中的点击返回事件处理
     */
    public boolean exit(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {//如果不是点击的返回键，则返回false
            return false;
        }
        if (isOnKeyBack) {
            mHandler.removeCallbacks(onBackExitRunnable);
            ToastUtils.cancelToast();
            HomeApp.exitApp();
            return true;
        } else {
            isOnKeyBack = true;
            ToastUtils.showToast("再按一次返回键退出应用");
            mHandler.postDelayed(onBackExitRunnable, 2000);
            return true;
        }
    }

}
