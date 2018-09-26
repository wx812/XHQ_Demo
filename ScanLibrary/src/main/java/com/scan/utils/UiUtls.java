package com.scan.utils;

import android.content.Context;

/**
 * Administrator
 * 2018/1/29
 * 10:29
 */
public class UiUtls {

    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
