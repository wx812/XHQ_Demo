package com.netease.nim.uikit;

import android.content.Context;
import android.content.SharedPreferences;

import com.netease.nim.uikit.api.NimUIKit;

/**
 * Administrator
 * 2017/11/23
 * 10:29
 */
public class BackgroundPreferences {

    private static boolean getBoolean(String key, boolean value) {
        return getSharedPreferences().getBoolean(key, value);
    }

    private static void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getBackgroundUri(String key, String value) {
        return getSharedPreferences().getString(key, value);
    }

    public static void saveBackgroundUri(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    static SharedPreferences getSharedPreferences() {
        return NimUIKit.getContext().getSharedPreferences("NimUIKit." + NimUIKit.getAccount(), Context.MODE_PRIVATE);
    }
}
