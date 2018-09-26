package com.xhq.demo.widget;

import android.app.Activity;
import android.os.Handler;

import java.lang.ref.WeakReference;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2018/7/4.
 *     Desc  : Description.
 *     Updt  : Description.
 * </pre>
 */
public class BaseHandler <T extends Activity> extends Handler{

    public final WeakReference<T> act;

    public BaseHandler(T activity) {
        act = new WeakReference<>(activity);
    }


    static class MHandler<T extends Activity> extends BaseHandler {

        public MHandler(T activity) {
            super(activity);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            Activity act = (Activity)super.act.get();
            switch (msg.what) {
                case 0: {
                    //使用theClass访问外部类成员和方法
                    break;
                }
                default: {
//					Log.w(TAG, "未知的Handler Message:" + msg.what);
                }
            }

        }
    }

}
