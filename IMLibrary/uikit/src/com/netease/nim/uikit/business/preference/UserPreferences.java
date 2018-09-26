package com.netease.nim.uikit.business.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.netease.nim.uikit.api.NimUIKit;

/**
 * Created by hzxuwen on 2015/10/21.
 */
public class UserPreferences {

    private final static String KEY_EARPHONE_MODE = "KEY_EARPHONE_MODE";

    private final static String KEY_EXTRA_ACCOUNT = "KEY_EXTRA_ACCOUNT";

    public static void setEarPhoneModeEnable(boolean on) {
        saveBoolean(KEY_EARPHONE_MODE, on);
    }

    public static boolean isEarPhoneModeEnable() {
        return getBoolean(KEY_EARPHONE_MODE, true);
    }

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
        return NimUIKit.getContext().getSharedPreferences("UIKit." + NimUIKit.getAccount(), Context.MODE_PRIVATE);
    }
}
