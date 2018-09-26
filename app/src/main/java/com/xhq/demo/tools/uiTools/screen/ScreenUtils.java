package com.xhq.demo.tools.uiTools.screen;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.DimenRes;
import android.support.annotation.IntRange;
import android.support.annotation.RequiresPermission;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xhq.demo.tools.appTools.AppUtils;

import java.lang.reflect.Method;

/**
 * <ul>
 * <li>DecorView的高度 ,包括:状态烂的高度和导航栏的高度</li>
 * <li>Window RootView的高度  ,(DecorView的高度)-(状态栏的高度)-(导航栏的高度)</li>
 * <li>heightPixels的高度 ,(DecorView的高度)-(导航栏的高度)</li>
 * </ul>
 * <p>
 * setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
 * 那么, (getRootView的高度) = (DecorView的高度)
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/9/1.
 *     Desc  : Screen Density Tools, Get screen information
 *     Updt  : Description.
 * </pre>
 */
public class ScreenUtils{

    public static final String TAG = ScreenUtils.class.getName();

    public static Window getWindow(Activity act){
        return act.getWindow();
    }


    public static Display getDisplay(){
        Context ctx = AppUtils.getAppContext();
        WindowManager wm = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
        if(wm == null) throw new RuntimeException("get WindowManager exception");
        return wm.getDefaultDisplay();  //获取屏幕尺寸，不包括虚拟功能高度
    }


    public static DisplayMetrics getDisplayMetrics(){
        Context ctx = AppUtils.getAppContext();
        return ctx.getResources().getDisplayMetrics();
    }


    public static int getDimenPx(@DimenRes int dimenResId){
        return AppUtils.getResources().getDimensionPixelSize(dimenResId);
    }


    /**
     * DecorView的高度代表的是: 整个装饰窗口的高度, 这个高度包括:状态烂的高度和导航栏的高度.
     * (状态栏和导航栏通常叫做装饰窗口, 而ActionBar不属于装饰窗口)<br>
     * 这个高度, 可以代表着整个玻璃屏幕的高度<br>
     */
    public static View getDecorView(Activity act){
        Window window = act.getWindow();
        return window.getDecorView();
    }



    public static void setUIFullScreen(Activity act){
        View decorView = getDecorView(act);
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }


    /**
     * 这个View对应的高度, 可以表示当前应用程序的有效高度.<br>
     * (getRootView的高度) = (DecorView的高度)-(状态栏的高度)-(导航栏的高度);<br>
     * 当setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
     * 那么, (getRootView的高度) = (DecorView的高度)
     *
     * @param act activity
     * @return view
     */
    public static View getRootView(Activity act){
        return act.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
    }


    /**
     * get phone screen width and height, px of unit, <br>including the virtual function keys height
     *
     * @return int array of width and height<br>
     * array[0] --> ScreenWidth<br>
     * array[1] --> ScreenHeight, see {@link #getScreenH()}
     */
    public static int[] getScreenWH(){
//        Display display = getDisplay();
//        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸

//        getWindowManager().getDefaultDisplay(); //获取屏幕尺寸，不包括虚拟功能高度

        // 方式1
//        Point outSize = new Point();
//        display.getSize(outSize);
//        int screenWPixels = outSize.x;
//        int screenHPixels = outSize.y;
        // 方式2
//        display.getMetrics(dm);// 给白纸设置宽高
//        int screenWPixels = dm.widthPixels;
//        int screenHPixels = dm.heightPixels;

        final int screenWPixels = getScreenW();
        final int screenHPixels = getScreenH();
        return new int[]{screenWPixels, screenHPixels};
    }


    /**
     * get Screen Width(px)
     */
    public static int getScreenW(){
        DisplayMetrics dm = getDisplayMetrics();
        return dm.widthPixels;
    }


    /**
     * get Screen Height(px) 这个高度表示…屏幕有效的高度
     * (getScreenH的高度) = DecorView的高度去掉导航栏的高度
     */
    public static int getScreenH(){
        DisplayMetrics dm = getDisplayMetrics();
        return dm.heightPixels;
    }


    /**
     * 通过反射，获取包含虚拟键的整体屏幕高度
     *
     * @return
     */
    private int getScreenHContainVirtualKey(){
        int dpi = 0;
        Display display = getDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try{
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        }catch(Exception e){
            e.printStackTrace();
        }
        return dpi;
    }


    /**
     * hide virtual key
     *
     * @param act activity
     */
    public static void hideVirtualKey(Activity act){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getDecorView(act).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);//API19
        }else{
            getDecorView(act).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }



    /**
     * @return Gets the current screen capture, including the status bar
     */
    public static Bitmap screenshotWithStatusBar(Activity act){
        View view = getDecorView(act);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();

        int width = getScreenW();
        int height = getScreenH();
        Bitmap bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }


    /**
     * @return Gets the current screen shot, does not contain the status bar
     */
    public static Bitmap screenshotWithoutStatusBar(Activity act){
        View decorView = getDecorView(act);
        Rect frame = new Rect();
        decorView.getWindowVisibleDisplayFrame(frame);
        int statusBarH = frame.top;

        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();

        int width = getScreenW();
        int height = getScreenH();
        Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarH, width, height - statusBarH);
        decorView.destroyDrawingCache();
        return bp;
    }


    /**
     * extinguish screen
     */
    public static void extinguishScreen(){
        PowerManager pm = (PowerManager) AppUtils.getAppContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK,TAG);
        mWakeLock.acquire();
        //做你想做的时。。。
        mWakeLock.release();
    }


    /**
     * judgement the screen is locked
     */
    public static boolean isLockScreen(){
        Context ctx = AppUtils.getAppContext();
        KeyguardManager km = (KeyguardManager)ctx.getSystemService(Context.KEYGUARD_SERVICE);
        if(km == null) throw new RuntimeException("get KeyguardManager exception");
        return km.inKeyguardRestrictedInputMode();
    }


    /**
     * set the screen to enter sleep time
     *
     * @param duration time
     */
    @RequiresPermission(android.Manifest.permission.WRITE_SETTINGS)
    public static void setScreenSleepTime(int duration){
        ContentResolver cr = AppUtils.getAppContext().getContentResolver();
        Settings.System.putInt(cr, Settings.System.SCREEN_OFF_TIMEOUT, duration);
    }


    /**
     * get the screen into sleep state time
     */
    public static int getScreenSleepTime(){
        ContentResolver cr = AppUtils.getAppContext().getContentResolver();
        try{
            return Settings.System.getInt(cr, Settings.System.SCREEN_OFF_TIMEOUT);
        }catch(Settings.SettingNotFoundException e){
            e.printStackTrace();
            return -123;
        }
    }


    /**
     * @return get the screen rotate angle
     */
    public static int getScreenRotateAngle(){
        switch(getDisplay().getRotation()){
            default:
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
    }


    /**
     * 强制设置屏幕为方向
     * <p>还有一种就是在Activity中加属性android:screenOrientation="landscape"</p>
     * <p>不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次</p>
     * <p>设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次</p>
     * <p>设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时
     * 切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法</p>
     * <p>
     * SCREEN_ORIENTATION_UNSET,                = -2<br>
     * SCREEN_ORIENTATION_UNSPECIFIED,          = -1<br>
     * SCREEN_ORIENTATION_LANDSCAPE,            = 0<br>
     * SCREEN_ORIENTATION_PORTRAIT,             = 1<br>
     * SCREEN_ORIENTATION_USER,                 = 2<br>
     * SCREEN_ORIENTATION_BEHIND,               = 3<br>
     * SCREEN_ORIENTATION_SENSOR,               = 4<br>
     * SCREEN_ORIENTATION_NOSENSOR,             = 5<br>
     * SCREEN_ORIENTATION_SENSOR_LANDSCAPE,     = 6<br>
     * SCREEN_ORIENTATION_SENSOR_PORTRAIT,      = 7<br>
     * SCREEN_ORIENTATION_REVERSE_LANDSCAPE,    = 8<br>
     * SCREEN_ORIENTATION_REVERSE_PORTRAIT,     = 9<br>
     * SCREEN_ORIENTATION_FULL_SENSOR,          = 10<br>
     * SCREEN_ORIENTATION_USER_LANDSCAPE,       = 11<br>
     * SCREEN_ORIENTATION_USER_PORTRAIT,        = 12<br>
     * SCREEN_ORIENTATION_FULL_USER,            = 13<br>
     * SCREEN_ORIENTATION_LOCKED                = 14<br>
     *
     * @param activity activity
     * @param orientation 0 --> landscape<br>1 --> portrait
     */
    public static void setScreenOrientation(Activity activity, @IntRange(from = 0, to = 1) int orientation){
        if(0 == orientation){
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else if(1 == orientation){
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


    /**
     * judgment screen orientation is landscape
     */
    public static boolean isLandscape(){
        return AppUtils.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


    /**
     * judgment screen orientation is portrait
     */
    public static boolean isPortrait(){
        return AppUtils.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

}
