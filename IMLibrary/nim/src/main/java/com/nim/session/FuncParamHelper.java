package com.nim.session;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Administrator
 * 2017/9/30
 * 12:34
 */
public class FuncParamHelper {

    private static final String TAG = FuncParamHelper.class.getName();


    //根据参数取值
    public static String getStrValueByKey(String jumpParams, String param) {
        try {
            Log.e(TAG, "jumpParams=" + jumpParams);
            Map map = JSONObject.parseObject(jumpParams, Map.class);
            Log.e(TAG, "map=" + map);
            String value = (String) map.get(param);
            Log.e(TAG, "value=" + value);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static int getIntValueByKey(String jumpParams, String param) {
        try {
            Log.e(TAG, "jumpParams=" + jumpParams);
            Map map = JSONObject.parseObject(jumpParams, Map.class);
            Log.e(TAG, "map=" + map);
            int value = (int) map.get(param);
            Log.e(TAG, "value=" + value);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
