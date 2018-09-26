package com.nim.common.util.crash;

import android.content.Context;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * app 异常处理类
 *
 * @author Administrator
 */
public class AppCrashHandler {

    private Context context;

    private UncaughtExceptionHandler uncaughtExceptionHandler;
    private static AppCrashHandler instance;

    private AppCrashHandler(Context context) {
        this.context = context;

        // get default
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        // install
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, final Throwable ex) {
                // save log
                saveException(ex, true);

                // uncaught
                uncaughtExceptionHandler.uncaughtException(thread, ex);
            }
        });
    }

    /**
     * 获取实例
     */
    public static AppCrashHandler getInstance(Context context) {
        if (instance == null) {
            instance = new AppCrashHandler(context);
        }

        return instance;
    }

    public final void saveException(Throwable ex, boolean uncaught) {
        CrashSaver.save(context, ex, uncaught);
    }

    /**
     * 错误回调
     */
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler handler) {
        if (handler != null) {
            this.uncaughtExceptionHandler = handler;
        }
    }
}
