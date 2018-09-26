package src;

import android.app.Application;

import com.smartcity.common.commonprovider.AppProvider;


/**
 * Author: YuJunKui
 * Time:2017/9/26 14:22
 * Tips:
 */

public class DeBugApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppProvider.init(getApplicationContext());

    }








}

