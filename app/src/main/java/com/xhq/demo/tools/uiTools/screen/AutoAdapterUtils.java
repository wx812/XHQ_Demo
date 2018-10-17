package com.xhq.demo.tools.uiTools.screen;

import android.app.Activity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhq.demo.tools.uiTools.Bar.StatusBarUtil;

/**
 * @author ZhengJingle
 * https://github.com/zhengjingle/Autolayout
 * https://blog.csdn.net/zhengjingle/article/details/51742839
 *
 * 1、屏幕的实际尺寸与设计图尺寸，计算出一个比例。
 * 2、然后按这个比例调整控件的size（宽高）、padding、margin。
 * 3、对于字体大小，则按对角线的比例来调整。
 * （其实对于字体大小，我认为比较完美的解决办法是让美工对于每种分辨率设定一个缩放比例。
 * 例如设计图是1280x720，这是标准的1倍；对于1920x1080是1.5倍，如此类推；我们按美工给的比例进行缩放就好了）
 */
public class AutoAdapterUtils{

    private static int mDisplayWidth;
    private static int mDisplayHeight;

    private static int mDesignWidth;
    private static int mDesignHeight;

    private static double textPixelsRate;


    public static void setSize(boolean hasStatusBar, int designWidth, int designHeight){
        if(designWidth < 1 || designHeight < 1) return;

        int width = ScreenUtils.getScreenWH()[0];
        int height = ScreenUtils.getScreenWH()[1];

        if(hasStatusBar) height -= StatusBarUtil.getStatusBarH();

        AutoAdapterUtils.mDisplayWidth = width;
        AutoAdapterUtils.mDisplayHeight = height;

        AutoAdapterUtils.mDesignWidth = designWidth;
        AutoAdapterUtils.mDesignHeight = designHeight;

        double displayDiagonal = Math.sqrt(Math.pow(AutoAdapterUtils.mDisplayWidth, 2)
                                                   + Math.pow(AutoAdapterUtils.mDisplayHeight, 2));
        double designDiagonal = Math.sqrt(Math.pow(AutoAdapterUtils.mDesignWidth, 2)
                                                  + Math.pow(AutoAdapterUtils.mDesignHeight, 2));
        AutoAdapterUtils.textPixelsRate = displayDiagonal / designDiagonal;
    }


    public static void auto(Activity act){
        if(act == null || mDisplayWidth < 1 || mDisplayHeight < 1) return;
        auto(ScreenUtils.getDecorView(act));
//        View view = act.getWindow().getDecorView();
//        auto(view);
    }


    public static void auto(View view){
        if(view == null || mDisplayWidth < 1 || mDisplayHeight < 1) return;

        AutoAdapterUtils.autoSize(view);
        AutoAdapterUtils.autoTextSize(view);
        AutoAdapterUtils.autoPadding(view);
        AutoAdapterUtils.autoMargin(view);

        if(view instanceof ViewGroup){
            auto((ViewGroup)view);
        }
    }


    public static void auto(ViewGroup viewGroup){
        int count = viewGroup.getChildCount();
        for(int i = 0; i < count; i++){
            View child = viewGroup.getChildAt(i);
            if(child != null){
                auto(child);
            }
        }
    }


    /**
     * 因为根据屏幕比例适配时，拉伸导致的。
     * 有时我们希望一些圆形图片适配时不变形，那么我们可以修改autoSize的代码如下：
     * @param view view
     */
    public static void autoSize(View view){
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if(lp == null) return;

        boolean isSquare = false;
        if(lp.width == lp.height){//注意美工给你的图片一定是正方形的
            isSquare = true;
        }

        if(lp.width > 0){
            lp.width = getDisplayWidthValue(lp.width);
        }

        if(lp.height > 0){
            lp.height = getDisplayHeightValue(lp.height);
        }

        if(isSquare){//设置它宽高一样，圆形就不变形了
            if(lp.width > lp.height){
                lp.width = lp.height;
            }else{
                lp.height = lp.width;
            }
        }
    }


    public static void autoTextSize(View view){
        if(view instanceof TextView){
            double designPixels = ((TextView)view).getTextSize();
            double displayPixels = textPixelsRate * designPixels;
            ((TextView)view).setIncludeFontPadding(false);
            ((TextView)view).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)displayPixels);
        }
    }


    public static void autoPadding(View view){
        int l = view.getPaddingLeft();
        int t = view.getPaddingTop();
        int r = view.getPaddingRight();
        int b = view.getPaddingBottom();

        l = getDisplayWidthValue(l);
        t = getDisplayHeightValue(t);
        r = getDisplayWidthValue(r);
        b = getDisplayHeightValue(b);

        view.setPadding(l, t, r, b);
    }


    public static void autoMargin(View view){
        if(!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams))return;
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        if(lp == null) return;
        lp.leftMargin = getDisplayWidthValue(lp.leftMargin);
        lp.topMargin = getDisplayHeightValue(lp.topMargin);
        lp.rightMargin = getDisplayWidthValue(lp.rightMargin);
        lp.bottomMargin = getDisplayHeightValue(lp.bottomMargin);
    }


    private static int getDisplayWidthValue(int designWidthValue){
        if(designWidthValue < 2) return designWidthValue;
        return designWidthValue * mDisplayWidth / mDesignWidth;
    }


    private static int getDisplayHeightValue(int designHeightValue){
        if(designHeightValue < 2) return designHeightValue;
        return designHeightValue * mDisplayHeight / mDesignHeight;
    }

    public static float getDisplayTextSize(float designTextSize){
        return (float) (AutoAdapterUtils.textPixelsRate*designTextSize);
    }
}
