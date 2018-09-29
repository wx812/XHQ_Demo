package com.xhq.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.xhq.demo.constant.ConstantValue;
import com.xhq.demo.tools.appTools.AppCrashUtil;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2016/6/10.
 *     Desc  : Description.
 *     Updt  : 2017/10/15.
 * </pre>
 */
public class HomeApp extends Application {
    private static Context appContext;
    private static LinkedList<Activity> allActivity = new LinkedList<>();
    @SuppressLint("StaticFieldLeak")
    private static Activity mCurrentActivity;
    @SuppressLint("StaticFieldLeak")
    private static HomeApp mInstance;
//    private DaoMaster.DevOpenHelper mDevOpenHelper;
//    private DaoMaster mDaoMaster;
//
//
//    public DaoMaster.DevOpenHelper getDevOpenHelper() {
//        return mDevOpenHelper;
//    }
//
//
//    public DaoMaster getDaoMaster() {
//        return mDaoMaster;
//    }
//
//
//    public DaoSession getDaoSession() {
//        return mDaoSession;
//    }
//
//
//    private DaoSession mDaoSession;

//    public static final boolean LOG_DEBUG = BuildConfig.LOG_DEBUG;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        appContext = this.getApplicationContext();

//        if (BuildConfig.LOG_DEBUG) {
////            Log.d(TAG, "hello world!");
//        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
//        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.NONE);
        builder.addInterceptor(loggingInterceptor);
        //设置超时
        builder.readTimeout(ConstantValue.READ_SECONDS, TimeUnit.SECONDS);
        builder.writeTimeout(ConstantValue.WRITE_SECONDS, TimeUnit.SECONDS);
        builder.connectTimeout(ConstantValue.CONNECT_SECONDS, TimeUnit.SECONDS);

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);

        //增强版Logger
        Logger.addLogAdapter(new AndroidLogAdapter());

        // Catch App Exception
        AppCrashUtil crashHandler = AppCrashUtil.getInstance();
        crashHandler.init();

        // GreenDao数据库初始化
//        GreenDaoManager.getInstance();

        Logger.t("xhq").e("HomeAPP 启动");

        //活体识别初始化
//        LivenessDetectionHelper.init();

        OkGo.getInstance()
                .init(this)                                     //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
//            .setCacheMode(CacheMode.NO_CACHE)             //全局统一缓存模式，默认不使用缓存，可以不传
//            .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE) //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(
                        0);                              //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
        //openDb();
    }

//    /**
//     * 打开greenDao
//     */
//    private void openDb() {
//        mDevOpenHelper = new DaoMaster.DevOpenHelper(this, getString(R.string.app_name) + ".db", null);
//        mDaoMaster = new DaoMaster(mDevOpenHelper.getWritableDb());
//        mDaoSession = mDaoMaster.newSession();
//    }


    public void add(Activity activity) {
        if (allActivity != null) {
            allActivity.add(activity);
        }
    }


    public static HomeApp getInstance() {
        // 自己new application 只能成为普通的对象
//        if (mInstance == null) {
//            mInstance = new HomeApp();
//        }
        return mInstance;
    }


    public static Context getAppContext() {
        return appContext;
    }


    public static void setCurrentActivity(Activity activity) {
        mCurrentActivity = activity;
    }


    public static Activity getCurrentActivity() {
        return mCurrentActivity;
    }


    public static void exitApp() {
        for (Activity activity : allActivity) {
            activity.finish();
        }
//        HomeFragment.resetFlag();
        allActivity.clear();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }


    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            allActivity.remove(activity);
            activity.finish();
        }
    }


    /**
     * 结束指定类名的Activity
     */
    public void finishActivityClass(Class<?> cls) {
        if (allActivity != null) {
            for (Activity activity : allActivity) {
                if (activity.getClass().equals(cls)) {
                    allActivity.remove(activity);
                    finishActivity(activity);
                    break;
                }
            }
        }
    }


}
