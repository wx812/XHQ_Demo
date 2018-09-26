package com.xhq.demo.tools.uiTools.Bar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IntRange;
import android.support.annotation.RequiresPermission;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.xhq.demo.tools.appTools.AppUtils;
import com.xhq.demo.tools.uiTools.screen.ScreenUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/9/3.
 *     Desc  : status bar tool
 *     Updt  : Description
 * </pre>
 */
public class StatusBarUtil{

    public static final int FULLSCREEN = WindowManager.LayoutParams.FLAG_FULLSCREEN;
    public static final int TRANSLUCENT_STATUS = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
    public static final int TRANSLUCENT_NAVIGATION = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static final int SYSTEM_BAR_BACKGROUNDS = WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;

    public static final int DEFAULT_STATUS_BAR_ALPHA = 112;

    private static final boolean IS_HIGH_4DOT4_SYSTEM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    private static final boolean IS_HIGH_5DOT0_SYSTEM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;


    public static class StatusBarView extends View{

        public StatusBarView(Context context){
            this(context, null);
        }


        public StatusBarView(Context context, AttributeSet attrs){
            super(context, attrs);
        }
    }


    /**
     * 设置状态栏颜色
     *
     * @param act 需要设置的activity
     * @param color 状态栏颜色值
     * @param alpha 状态栏透明度 defaultV = {@link #DEFAULT_STATUS_BAR_ALPHA}
     */
    public static void setStatusBarColor(Activity act, int color, int alpha){
        if(IS_HIGH_5DOT0_SYSTEM){
            act.getWindow().addFlags(SYSTEM_BAR_BACKGROUNDS);
            act.getWindow().clearFlags(TRANSLUCENT_STATUS);
            act.getWindow().setStatusBarColor(modifyColorAlpha(color, alpha));
        }else if(IS_HIGH_4DOT4_SYSTEM){
            act.getWindow().addFlags(TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup)act.getWindow().getDecorView();
            int count = decorView.getChildCount();
            if(count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView){
                decorView.getChildAt(count - 1).setBackgroundColor(modifyColorAlpha(color, alpha));
            }else{
                StatusBarView statusView = createStatusBarView(act, color, alpha);
                decorView.addView(statusView);
            }
            setRootFitsView(act);
        }
    }


    /**
     * 修改状态栏颜色，支持4.4以上版本<br>
     * 使用SystemBarTint库
     */
    public static void setStatusBarColor(Activity act, @ColorRes int colorResId){

        if(IS_HIGH_5DOT0_SYSTEM){ // 5.0
            Window window = act.getWindow();
            window.addFlags(SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(act.getResources().getColor(colorResId));
            // 以市场上现有的手机系统大概都在5.0以上, 所以效率上考虑 4.4 判断写在下面
        }else if(IS_HIGH_4DOT4_SYSTEM){
            setTransparentStatusBar(act); //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
            SystemBarTintManager tintManager = new SystemBarTintManager(act);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(colorResId);
        }
    }


    /**
     * 为滑动返回界面设置状态栏颜色
     *
     * @param act 需要设置的activity
     * @param color 状态栏颜色值
     * @param alpha 状态栏透明度  defaultV = {@link #DEFAULT_STATUS_BAR_ALPHA}
     */
    public static void setColorForSwipeBack(Activity act, int color, int alpha){
        if(IS_HIGH_4DOT4_SYSTEM){
            ViewGroup contentView = act.findViewById(android.R.id.content);
            contentView.setPadding(0, getStatusBarH(), 0, 0);
            contentView.setBackgroundColor(modifyColorAlpha(color, alpha));
            setTransparentStatusBar(act);
        }
    }


    /**
     * 使状态栏半透明
     * <p>
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param act 需要设置的activity
     * @param statusBarAlpha 状态栏透明度 defaultV = {@link #DEFAULT_STATUS_BAR_ALPHA}
     */
    public static void setTranslucent(Activity act, int statusBarAlpha){
        if(!IS_HIGH_4DOT4_SYSTEM) return;
        setTransparent(act);
        addStatusAlphaView(act, statusBarAlpha);
    }


    /**
     * 设置状态栏全透明
     */
    public static void setTransparent(Activity act){
        if(!IS_HIGH_4DOT4_SYSTEM) return;
        setTranslucentStatusBar(act);
        setRootFitsView(act);
    }


    /**
     * 为DrawerLayout 布局设置状态栏变色
     *
     * @param act 需要设置的activity
     * @param layout DrawerLayout
     * @param color 状态栏颜色值
     * @param alpha 状态栏透明度 defaultV = {@link #DEFAULT_STATUS_BAR_ALPHA}
     */
    public static void setColorForDrawerLayout(Activity act, DrawerLayout layout, int color, int alpha){
        if(!IS_HIGH_4DOT4_SYSTEM) return;
        Window w = act.getWindow();
        if(IS_HIGH_5DOT0_SYSTEM){
            w.addFlags(SYSTEM_BAR_BACKGROUNDS);
            w.clearFlags(TRANSLUCENT_STATUS);
            w.setStatusBarColor(Color.TRANSPARENT);
        }else{
            w.addFlags(TRANSLUCENT_STATUS);
        }
        // 生成一个状态栏大小的矩形 添加 statusBarView 到布局中
        ViewGroup vg0 = (ViewGroup)layout.getChildAt(0);
        if(vg0.getChildCount() > 0 && vg0.getChildAt(0) instanceof StatusBarView){
            vg0.getChildAt(0).setBackgroundColor(modifyColorAlpha(color, alpha));
        }else{
            StatusBarView statusBarView = createStatusBarView(act);
            statusBarView.setBackgroundColor(color);
            vg0.addView(statusBarView, 0);
        }
        // 内容布局不是 LinearLayout 时,设置padding top
        View vg1 = vg0.getChildAt(1);
        if(!(vg0 instanceof LinearLayout) && vg1 != null){
            vg1.setPadding(vg0.getPaddingLeft(), getStatusBarH() + vg0.getPaddingTop(),
                           vg0.getPaddingRight(), vg0.getPaddingBottom());
        }
        // 设置DrawerLayout属性
        ViewGroup drawer = (ViewGroup)layout.getChildAt(1);
        layout.setFitsSystemWindows(false);
        vg0.setFitsSystemWindows(false);
        vg0.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);
//        addStatusAlphaView(act, alpha);
    }


    /**
     * 为 DrawerLayout 布局设置状态栏透明
     *
     * @param act 需要设置的activity
     * @param layout DrawerLayout
     * @param alpha 透明度 default = {@link #DEFAULT_STATUS_BAR_ALPHA}
     */
    public static void setTranslucentForDrawerLayout(Activity act, DrawerLayout layout, int color, int alpha){
        if(!IS_HIGH_4DOT4_SYSTEM) return;
        setColorForDrawerLayout(act, layout, color, alpha);
        addStatusAlphaView(act, alpha);
    }


    /**
     * 通过自定义一个与状态栏高度一样的view, 隐藏掉系统状态栏, 然后添加到content布局中<br>
     * 来实现 侵入的半透明状态栏
     *
     * @param act 需要设置的activity
     * @param statusBarAlpha 状态栏透明度
     * @param offsetView 需要向下偏移的 View
     */
    public static void setTranslucentForImageView(Activity act, int statusBarAlpha, View offsetView){
        if(!IS_HIGH_4DOT4_SYSTEM) return;
        setTransparentStatusBar(act);
        addStatusAlphaView(act, statusBarAlpha);
        if(offsetView != null){
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)offsetView.getLayoutParams();
            mlp.setMargins(0, getStatusBarH(), 0, 0);
        }
    }


    /**
     * 为 fragment 头部是 ImageView 的设置状态栏透明
     *
     * @param act fragment 对应的 activity
     * @param statusBarAlpha 状态栏透明度
     * @param offsetView 需要向下偏移的 View
     */
    public static void setTranslucentForImageViewInFragment(Activity act, int statusBarAlpha, View offsetView){
        setTranslucentForImageView(act, statusBarAlpha, offsetView);
        if(IS_HIGH_4DOT4_SYSTEM && !IS_HIGH_5DOT0_SYSTEM){ // 4.4<= sdk <= 5.0
            clearPreviousSetting(act);
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void clearPreviousSetting(Activity act){
        ViewGroup decorView = (ViewGroup)act.getWindow().getDecorView();
        int count = decorView.getChildCount();
        if(count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView){
            decorView.removeViewAt(count - 1);
            ViewGroup content = act.findViewById(android.R.id.content);
            ViewGroup child = (ViewGroup)content.getChildAt(0);
            child.setPadding(0, 0, 0, 0);
        }
    }


    /**
     * 添加半透明矩形条
     *
     * @param statusBarAlpha 透明值
     */
    private static void addStatusAlphaView(Activity act, int statusBarAlpha){
        ViewGroup content = act.findViewById(android.R.id.content);
        int alphaColor = Color.argb(statusBarAlpha, 0, 0, 0);
        if(content.getChildCount() > 1){
            content.getChildAt(1).setBackgroundColor(alphaColor);
        }else{
            StatusBarView statusBarView = createStatusBarView(act);
            statusBarView.setBackgroundColor(alphaColor);
            content.addView(statusBarView);
        }
    }


    /**
     * @return 绘制一个和状态栏一样大小的矩形条
     */
    public static StatusBarView createStatusBarView(Activity act){
        StatusBarView statusBarView = new StatusBarView(act);
        final int w = ViewGroup.LayoutParams.MATCH_PARENT;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, getStatusBarH());
        statusBarView.setLayoutParams(params);
        return statusBarView;
    }


    /**
     * see {@link #createStatusBarView(Activity)}
     *
     * @param act 需要设置的activity
     * @param statusBarColor 状态栏颜色值
     * @param alpha 透明值
     * @return 绘制一个和状态栏一样高的矩形
     */
    private static StatusBarView createStatusBarView(Activity act, @ColorInt int statusBarColor,
                                                     @IntRange(from = 0, to = 255) int alpha){
        StatusBarView statusBarView = createStatusBarView(act);
        statusBarView.setBackgroundColor(modifyColorAlpha(statusBarColor, alpha));
        return statusBarView;
    }


    /**
     * 修改颜色的透明度
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的颜色
     */
    private static int modifyColorAlpha(@ColorInt int color, @IntRange(from = 0, to = 255) int alpha){
        if(alpha == 0) return color;
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int)(red * a + 0.5);
        green = (int)(green * a + 0.5);
        blue = (int)(blue * a + 0.5);
        return (0xff << 24) | (red << 16) | (green << 8) | blue;
    }


    /**
     * 设置根布局参数
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setRootFitsView(Activity act){
        ViewGroup content = act.findViewById(android.R.id.content);
        ViewGroup root = (ViewGroup)content.getChildAt(0);
        root.setFitsSystemWindows(true);
        root.setClipToPadding(true);
    }


    /**
     * 设置透明状态栏（api大于19方可使用）
     * <p>可在Activity的onCreate()中调用</p>
     * <p>需在顶部控件布局中加入以下属性让内容出现在状态栏之下</p>
     * <p>android:clipToPadding="true"</p>
     * <p>android:fitsSystemWindows="true"</p>
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setTranslucentStatusBar(Activity act){
        Window w = act.getWindow();
        if(IS_HIGH_5DOT0_SYSTEM){ // >=5.0
            w.addFlags(SYSTEM_BAR_BACKGROUNDS);
            w.clearFlags(TRANSLUCENT_STATUS);
            w.addFlags(TRANSLUCENT_NAVIGATION);
            w.setStatusBarColor(Color.TRANSPARENT);
        }else if(IS_HIGH_4DOT4_SYSTEM){ // >=4.4
            w.addFlags(TRANSLUCENT_STATUS);  //半透明状态栏
            w.addFlags(TRANSLUCENT_NAVIGATION);  //半透明导航栏
        }
    }


    /**
     * Set the status bar to be fully transparent(sdk>5.0) or translucent(sdk>4.4)
     */
    @TargetApi(19)
    public static void setTransparentStatusBar(Activity act){
        Window w = act.getWindow();
        if(IS_HIGH_5DOT0_SYSTEM){ // >=5.0
            w.clearFlags(TRANSLUCENT_STATUS);
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            w.getDecorView().setSystemUiVisibility(visibility);
            w.addFlags(SYSTEM_BAR_BACKGROUNDS);
            w.setStatusBarColor(Color.TRANSPARENT);
        }else if(IS_HIGH_4DOT4_SYSTEM){  // >=4.4
            w.setFlags(TRANSLUCENT_STATUS, TRANSLUCENT_STATUS);
        }
    }



    /*--------------------------------************************************--------------------------------*/


    /**
     * use {@link #getStatusBarH()} replace
     * get the status bar height through the reflection of the way
     *
     * @return status bar height
     */
    @Deprecated
    public static int getStatusHByReflex(){
        final String h = "status_bar_height";
        int statusHeight = -1;
        try{
            @SuppressLint("PrivateApi")
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField(h).get(object).toString());
            statusHeight = ScreenUtils.getDimenPx(height);
        }catch(Exception e){
            e.printStackTrace();
        }
        return statusHeight;
    }


    public static int getStatusBarH(){
        int statusHeight = 0;
        final String h = "status_bar_height";
        Resources res = AppUtils.getResources();
        try{
            int resourceId = res.getIdentifier(h, "dimen", "android");
            if(resourceId > 0) statusHeight = ScreenUtils.getDimenPx(resourceId);
        }catch(Resources.NotFoundException e){
            e.printStackTrace();
        }
        return statusHeight;
    }


    /**
     * Get the height of the status bar
     * getWindow().getDecorView()得到的View是Window中的最顶层View，可以从Window中获取到该View，
     * 然后该View有个getWindowVisibleDisplayFrame()方法可以获取到程序显示的区域，包括标题栏，但不包括状态栏。
     */
    public static int getStatusBarH(Activity act){
        View decorView = ScreenUtils.getDecorView(act);
        Rect outRect = new Rect();
        decorView.getWindowVisibleDisplayFrame(outRect);
        return outRect.top;
    }


    public static boolean isStatusBarExists(Activity act){
        WindowManager.LayoutParams params = act.getWindow().getAttributes();
        return (params.flags & FULLSCREEN) != FULLSCREEN;
    }


    /**
     * 隐藏状态栏
     * <p>也就是设置全屏，一定要在setContentView之前调用，否则报错</p>
     * <p>在配置文件中Activity加属性android:theme="@android:style/Theme.NoTitleBar.Fullscreen"</p>
     * <p>如加了以上配置Activity不能继承AppCompatActivity，会报错</p>
     */
    public static void hideStatusBar(Activity act){
        act.requestWindowFeature(Window.FEATURE_NO_TITLE);
        act.getWindow().setFlags(FULLSCREEN, FULLSCREEN);
    }


    /**
     * 如果不希望 APP 的内容被上拉到状态列 (Status bar) 的话，要记得在介面 (Layout) XML 档中，
     * 最外面的那层，要再加上一个属性 fitsSystemWindows为true
     */
    public static void setTranslucentStatusNaviBar(Activity act){
        if(IS_HIGH_4DOT4_SYSTEM){
            Window window = act.getWindow();
            window.setFlags(TRANSLUCENT_STATUS, TRANSLUCENT_STATUS);  // Translucent status bar
            window.setFlags(TRANSLUCENT_NAVIGATION, TRANSLUCENT_NAVIGATION);  // Translucent navigation bar
        }

    }


    // This method hides the system bars and resize the content
    public static void hideSystemUIBar(Activity act){
        View v = act.getWindow().getDecorView();
        v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                        // remove the following flag for version < API 19
                                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }


    /**
     * show notification bar
     *
     * @param isSettingPanel {@code true}: open settings panel<br>{@code false}: open notification panel
     */
    @RequiresPermission(Manifest.permission.EXPAND_STATUS_BAR)
    public static void showNotifBar(Context ctx, boolean isSettingPanel){
        @SuppressLint("ObsoleteSdkInt")
        boolean sdk_V = Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN;
        String expandPanel = isSettingPanel ? "expandSettingsPanel" : "expandNotificationsPanel";
        String methodName = sdk_V ? "expand" : expandPanel;
        invokeSTBarByReflex(ctx, methodName);
    }


    /**
     * hide notification bar
     */
    @RequiresPermission(Manifest.permission.EXPAND_STATUS_BAR)
    public static void hideNotifBar(Context ctx){
        @SuppressLint("ObsoleteSdkInt")
        boolean sdk_V = Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN; // 4.1
        String methodName = sdk_V ? "collapse" : "collapsePanels";
        invokeSTBarByReflex(ctx, methodName);
    }


    /**
     * 反射调用Status bar 的methodName 方法
     *
     * @param ctx 上下文
     * @param methodName 方法名
     */
    private static void invokeSTBarByReflex(Context ctx, String methodName){
        try{
            @SuppressLint("WrongConstant")
            Object service = ctx.getSystemService("statusbar");
            @SuppressLint("PrivateApi")
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method method = statusBarManager.getMethod(methodName);
            method.invoke(service);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 设置状态栏黑色字体图标，
     * see {@link #setStatusBarLightMode}
     *
     * @return 1:MIUI 2:Flyme 3:android6.0
     */
    public static int getSystmeROMType(Activity act){
        int result = 0;
        if(IS_HIGH_4DOT4_SYSTEM){
            if(setMIUIStatusBarLightMode(act.getWindow(), true)){
                result = 1;
            }else if(setFlymeStatusBarLightMode(act.getWindow(), true)){
                result = 2;
            }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ // 6.0
                int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                act.getWindow().getDecorView().setSystemUiVisibility(visibility);
                result = 3;
            }
        }
        return result;
    }


    /**
     * 已知系统类型时，设置状态栏风格。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param phoneType 1:MIUUI 2:Flyme 3:other android6.0 phone
     */
    public static void setStatusBarLightMode(Activity act, int phoneType, boolean isDark){
        if(phoneType == 1){
            setMIUIStatusBarLightMode(act.getWindow(), isDark);
        }else if(phoneType == 2){
            setFlymeStatusBarLightMode(act.getWindow(), isDark);
        }else if(phoneType == 3){
            View decorView = act.getWindow().getDecorView();
            if(isDark){
                int visibility = 0;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ // 6.0
                    visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(visibility);
            }else{
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }

    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * see {@link #setMIUIStatusBarLightMode(Window, boolean)}
     */
    public static boolean setFlymeStatusBarLightMode(Window window, boolean isDark){
        boolean result = false;
        if(window != null){
            try{
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if(isDark){
                    value |= bit;
                }else{
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param isDark 是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean setMIUIStatusBarLightMode(Window window, boolean isDark){
        boolean result = false;
        if(window != null){
            Class clazz = window.getClass();
            try{
                int darkModeFlag;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(isDark){
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }
}