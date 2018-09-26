package com.netease.nim.uikit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Administrator
 * 2017/10/17
 * 14:55
 */
public class UserProfileHelper {

    //个人名片
    public static void startPersonDetail(Context context, String usercode) {
        try {
            if (context instanceof Activity) {
                Uri uri = Uri.parse("smartcity://chat/open_personcard?usercode=" + usercode);
                Activity currentActivity = (Activity) context;
                Intent intent = new Intent("personalcard", uri);
                currentActivity.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
