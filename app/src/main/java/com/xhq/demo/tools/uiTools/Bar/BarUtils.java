package com.xhq.demo.tools.uiTools.Bar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhq.demo.tools.appTools.AppUtils;
import com.xhq.demo.tools.uiTools.screen.ScreenUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2017/8/31.
 *     Desc  : bar(Status, tool, Navigation, Snack) tool
 *     Updt  : Description
 * </pre>
 */
public class BarUtils{


    /**
     * @return 获取标题栏高度
     */
    public static int getTitleBarH(Activity act){
        // 该方法获取到的View是程序不包括标题栏的部分，这样我们就可以计算出标题栏的高度了。
        int contentTop = act.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return contentTop - StatusBarUtil.getStatusBarH(act);
    }


    /**
     * add a title to the toolbar and center the title
     */
    public static void addMiddleTitle(Context uICtx, CharSequence title, Toolbar toolbar){

        TextView textView = new TextView(uICtx);
        textView.setText(title);
        int wh = Toolbar.LayoutParams.WRAP_CONTENT;
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(wh, wh);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        toolbar.addView(textView, params);
    }


    /**
     * @return ActionBar height
     */
    public static int getActionBarH(Activity act){
        TypedValue tv = new TypedValue();
        if(act.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){
            DisplayMetrics displayMetrics = act.getResources().getDisplayMetrics();
            return TypedValue.complexToDimensionPixelSize(tv.data, displayMetrics);
        }
        return 0;
    }


    public void initActionBar(Activity act) {
        ActionBar actionBar = act.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
//        View view = View.inflate(this, R.layout.actionbar_title, null);
//        mActionBarTitle = view.findViewById(R.id.tv_shimmer);
//        new Shimmer().start(mActionBarTitle);
//        actionBar.setCustomView(view);
    }

    //*************************************************Navigation***********************************************//


    /**
     * @return get the height of the virtual bar key
     */
    public static int getNavBarH(){
        int result = 0;
        if(hasNavBar()){
            Resources res = AppUtils.getResources();
            String navBarH = "navigation_bar_height";
            int dimenResId = res.getIdentifier(navBarH, "dimen", "android");
            if(dimenResId > 0) result = ScreenUtils.getDimenPx(dimenResId);
        }
        return result;
    }


    /**
     * check if there is a virtual key bar
     *
     * @return {@code true:} have<br>{@code false:} no have
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(){
        Context ctx = AppUtils.getAppContext();
        Resources res = ctx.getResources();
        final String showNavBar = "config_showNavigationBar";
        final int resourceId = res.getIdentifier(showNavBar, "bool", "android");
        if(resourceId != 0){
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if("1".equals(sNavBarOverride)) hasNav = false;
            if("0".equals(sNavBarOverride)) hasNav = true;
            return hasNav;
        }else{ // fallback
            return !ViewConfiguration.get(ctx).hasPermanentMenuKey();
        }
    }


    /**
     * 判断虚拟按键栏是否重写
     *
     * @return "0"(没有重写) or "1"(重写了)
     */
    private static String getNavBarOverride(){
        String sNavBarOverride = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try{
                final String clazzName = "android.os.SystemProperties";
                final String s = "qemu.hw.mainkeys";
                @SuppressLint("PrivateApi") Class c = Class.forName(clazzName);
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String)m.invoke(null, s);
            }catch(Throwable ignored){
            }
        }
        return sNavBarOverride;
    }


    /**
     * set the page status bar translucent styles
     * 和 {@link #setTranslucentNav(Activity)} 配合使用
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setTranslucentStatus(Activity act){
        act.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setTranslucentNav(Activity act){
        if(!hasNavBar())
            act.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }



    //********************************************snack bar*************************************************/



    private static WeakReference<Snackbar> snackBar_WR;


    /**
     * see {@link #showSnackBar(View, CharSequence, int, int, int, CharSequence, int, View.OnClickListener)}
     */
    public static void showLongSnackbar(View parent, CharSequence text, int duration,
                                        @ColorInt int textColor, @ColorInt int bgColor) {
        showSnackBar(parent, text, duration, textColor, bgColor, null, -1, null);
    }


    /**
     * 设置SnackBar文字和背景颜色
     *
     * @param parent          父视图(CoordinatorLayout或者DecorView)
     * @param text            文本
     * @param duration        显示时长 {@link Snackbar#LENGTH_INDEFINITE}<br>
     *                                 {@link Snackbar#LENGTH_LONG}<br>
     *                                 {@link Snackbar#LENGTH_SHORT}
     * @param textColor       文本颜色
     * @param bgColor         背景色
     * @param actionText      事件文本
     * @param actionTextColor 事件文本颜色
     * @param listener        监听器
     */
    private static void showSnackBar(View parent, CharSequence text, int duration,
                                     @ColorInt int textColor, @ColorInt int bgColor,
                                     CharSequence actionText, int actionTextColor,
                                     View.OnClickListener listener) {
        SpannableString textStr = new SpannableString(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(textColor);
        textStr.setSpan(colorSpan, 0, textStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        snackBar_WR = new WeakReference<>(Snackbar.make(parent, textStr, duration));
        Snackbar snackbar = snackBar_WR.get();
        View view = snackbar.getView();
        view.setBackgroundColor(bgColor);
        if (actionText != null && actionText.length() > 0 && listener != null) {
            snackbar.setActionTextColor(actionTextColor);
            snackbar.setAction(actionText, listener);
        }
        snackbar.show();
    }


    /**
     * add layout to snack bar
     * <p>called after show...SnackBar</p>
     *
     * @param layoutId layout xml file
     * @param index    the position at which to add the child or -1 to add last
     */
    public static void addView(@LayoutRes int layoutId, int index) {
        Snackbar snackBar = snackBar_WR.get();
        if (snackBar != null) {
            View view = snackBar.getView();
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) view;
            View child = LayoutInflater.from(view.getContext()).inflate(layoutId, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_VERTICAL;
            layout.addView(child, index, params);
        }
    }


    /**
     * dismiss SnackBar
     */
    public static void dismissSnackBar() {
        if (snackBar_WR != null && snackBar_WR.get() != null) {
            snackBar_WR.get().dismiss();
            snackBar_WR = null;
        }
    }


}
