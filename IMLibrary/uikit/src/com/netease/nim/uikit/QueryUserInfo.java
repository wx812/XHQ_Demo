package com.netease.nim.uikit;

import android.content.Context;
import android.text.TextUtils;

import common.commonsdk.Injection;

/**
 * Created by Yancy on 2017/7/11.
 * <p>
 * 查询用户信息
 */

public class QueryUserInfo {

    private static final String USER_ENTITY = "com.smartcity.commonbussiness.user.SysUserEntity";

    public static String getUserCode(Context context) {
        if (TextUtils.isEmpty(Injection.provideSp().getUserId())) {
            return null;
        }
        try {
            Object query = Injection.provideDao(context, Class.forName(USER_ENTITY)).query("userid", Injection.provideSp().getUserId());
            if (null != query) {
                return (String) query.getClass().getField("usercode").get(query);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
