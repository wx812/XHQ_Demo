package com.xhq.demo.tools.spTools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.xhq.demo.HomeApp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2016/8/10.
 *     Desc  : SharedPreferences utils
 *     Updt  : 2017/7/3 update.
 * </pre>
 */
public final class SPUtils{


    /**
     * @return if spFileName null, see {@link #getDefaultSP()}
     */
    public static SharedPreferences getSP(@Nullable String spFileName){
        return TextUtils.isEmpty(spFileName) ? getDefaultSP() : getSharedPreferences(spFileName);
    }

    /**
     * the Context of the Application --> HomeApp.getAppContext()
     * <p>
     * get the sharedPreferences of the spFileName
     *
     * @param spFileName file name of the sharedPreferences, If a preferences file by this name does not exist,
     * it will be created when you retrieve an editor (SharedPreferences.edit())
     * and then commit changes (Editor.commit()).
     *
     * @return SharedPreferences
     */
    private static SharedPreferences getSharedPreferences(String spFileName){
        Context ctx = HomeApp.getAppContext();
        return ctx.getSharedPreferences(spliceFileName(spFileName), Context.MODE_PRIVATE);
    }


    private static String spliceFileName(String fileName){
        return fileName + "_preferences";
    }


    /**
     * @return the name(packageName + "_preferences") used for storing default shared preferences.
     */
    public static SharedPreferences getDefaultSP(){
        Context ctx = HomeApp.getAppContext();
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }


    /**
     * @param defValue If the key does not have a value it returns the default value
     * @return get data from the default shared preferences - auth xhq
     */
    public static Object getDefaultSP_KV(String key, Object defValue) {
        SharedPreferences sp = getDefaultSP();
        if (defValue instanceof String) {
            return sp.getString(key, (String) defValue);
        } else if (defValue instanceof Integer) {
            return sp.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Float) {
            return sp.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Long) {
            return sp.getLong(key, (Long) defValue);
        }
        return defValue;
//        return get("", key, defValue);
    }


    /**
     * save data to the default shared preferences
     */
    public static void putDefaultSP_KV(String key, Object value){
        put("", key, value);
    }


//    /**
//     * get data from sharedPreferences of the spFileName - auth xhq
//     *
//     * @param spFileName
//     * @param key
//     * @param defValue
//     * @return
//     */
//    public static Object get(String spFileName, String key, Object defValue) {
//        SharedPreferences sp = getSharedPreferences(spFileName);
//        if (defValue instanceof String) {
//            return sp.getString(key, (String) defValue);
//        } else if (defValue instanceof Integer) {
//            return sp.getInt(key, (Integer) defValue);
//        } else if (defValue instanceof Boolean) {
//            return sp.getBoolean(key, (Boolean) defValue);
//        } else if (defValue instanceof Float) {
//            return sp.getFloat(key, (Float) defValue);
//        } else if (defValue instanceof Long) {
//            return sp.getLong(key, (Long) defValue);
//        }
//        return defValue;
//    }


    /**
     * get data from sharedPreferences of the spFileName - auth xhq
     *
     * @param spFileName Store the key-value in this file
     * @param key the key-value stored in the file of the SharedPreferences
     * @param defValue --> default value of the key when the key haven't value
     * @param <T> The default value for each data type, the defvalue Can be set
     * @return the value of The default value for each data type, return defValue when is null the key
     */
    public static <T> T get(String spFileName, String key, T defValue){
        SharedPreferences sp = getSP(spFileName);
        if(defValue instanceof String){
            return (T)sp.getString(key, (String)defValue);
        }else if(defValue instanceof Integer){
            return (T)(Integer)sp.getInt(key, (Integer)defValue);
        }else if(defValue instanceof Boolean){
            return (T)(Boolean)sp.getBoolean(key, (Boolean)defValue);
        }else if(defValue instanceof Float){
            return (T)(Float)sp.getFloat(key, (Float)defValue);
        }else if(defValue instanceof Long){
            return (T)(Long)sp.getLong(key, (Long)defValue);
        }
        return defValue;
    }


    /**
     * save data to sharedPreferences of the spFileName - auth xhq
     *
     * @param spFileName Store the key-value in this file
     * @param key key. the key-value stored in the file of the SharedPreferences
     * @param value value. key corresponds to the value
     */
    public static void put(String spFileName, String key, Object value){
        SharedPreferences sp = getSP(spFileName);
        Editor editor = sp.edit();
        if(value instanceof String){
            editor.putString(key, (String)value);
        }else if(value instanceof Integer){
            editor.putInt(key, (Integer)value);
        }else if(value instanceof Boolean){
            editor.putBoolean(key, (Boolean)value);
        }else if(value instanceof Float){
            editor.putFloat(key, (Float)value);
        }else if(value instanceof Long){
            editor.putLong(key, (Long)value);
        }else{
            editor.putString(key, value.toString());
        }
        editor.apply();
    }


    /**
     * query key is exist from the spFileName - auth xhq
     *
     * @param spFileName @see {@link #put(String, String, Object)}
     * @param key @see {@link #put(String, String, Object)}
     * @return if true, the key specified in the specified file exists
     */
    public static boolean isExistKey(@Nullable String spFileName, String key){
        return getSP(spFileName).contains(key);
    }


    /**
     * remove key data from the spFileName - auth xhq
     *
     * @param spFileName @see {@link #put(String, String, Object)}
     * @param key @see {@link #put(String, String, Object)}
     */
    public static void deleteKV(@Nullable String spFileName, String key){
        if(!isExistKey(spFileName, key)) return;
        SharedPreferences sp = getSP(spFileName);
        Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }


    /**
     * clear all data from the spFileName - auth xhq
     *
     * @param spFileName @see {@link #put(String, String, Object)}
     */
    public static void clear(@Nullable String spFileName){
        SharedPreferences sp = getSP(spFileName);
        Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }


    /**
     * get all data from the spFileName - auth xhq
     *
     * @param spFileName see {@link #put(String, String, Object)}
     * @return get all key/value pair in the specified file
     */
    public static Map<String, ?> getAll(@Nullable String spFileName){
        return getSP(spFileName).getAll();
    }


    private static class SharedPreferencesCompat{
        private static final Method sApplyMethod = findApplyMethod();


        /**
         * Reflection search apply
         *
         * @return Method
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod(){
            try{
                Class clz = Editor.class;
                return clz.getMethod("apply");
            }catch(NoSuchMethodException ignored){
            }

            return null;
        }


        public static void apply(Editor editor){
            try{
                if(sApplyMethod != null){
                    sApplyMethod.invoke(editor);
                    return;
                }
            }catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException ignored){
            }
            editor.commit();
        }
    }

}
