package com.xhq.demo.http;


import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.xhq.demo.tools.appTools.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;


/**
 * Created by xionggan on 2017/2/17.
 */

public class HttpClientUtils{

    private static final String TAG = "HttpClientUtils";
    private static OkHttpClient client;

    static {
        initOkHttpClient();
    }

    private static void initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BASIC);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
        OkHttpClient okHttpClient = builder.build();
        OkGo.getInstance().init((Application)AppUtils.getAppContext()).setRetryCount(1).setOkHttpClient(okHttpClient);
    }

    private static OkHttpClient getCusTimeoutClient(int timeout) {
        if(client == null || client.connectTimeoutMillis() != timeout){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BASIC);
            loggingInterceptor.setColorLevel(Level.INFO);
            builder.addInterceptor(loggingInterceptor);

            builder.readTimeout(timeout, TimeUnit.MILLISECONDS);
            builder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
            builder.connectTimeout(timeout, TimeUnit.MILLISECONDS);

            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
            builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
            builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
            client = builder.build();
        }
        return client;
    }

    public static void get(String url, HttpParams params, AbsCallback callback) {
        OkGo.<String>get(url).params(params).execute(callback);
    }


    public static void evmGet(String urlInterfName, HttpParams params, AbsCallback callback) {
        String baseUrl = getBaseUrl();
        OkGo.<String>get(baseUrl + urlInterfName).headers(getHttpHeaders()).params(params).execute(callback);
    }


    public static void post(String url, HttpParams params, AbsCallback callback) {
        OkGo.<String>post(url).params(params).execute(callback);
    }


    /**
     * eg: http://139.196.103.190:8080/transactionService/uploadTransactionRecord <br>
     *
     * @param urlInterfName 服务器接口名  urlInterfName = /transactionService/uploadTransactionRecord
     */
    public static void evmPost(String urlInterfName, Object jsonObjOrArray, AbsCallback callback) {
        if (!(jsonObjOrArray instanceof JSONObject || jsonObjOrArray instanceof JSONArray)) {
//            LogUtil.d(TAG, "evmPost jsonObjOrArray format error,request cancel");
            return;
        }
        String baseUrl = getBaseUrl();
        OkGo.<String>post(baseUrl + urlInterfName).headers(getHttpHeaders()).upJson(jsonObjOrArray.toString()).execute(callback);
    }

    public static void evmPost(String urlInterfName, HttpHeaders headers, Object jsonObjOrArray, AbsCallback callback) {
        if (!(jsonObjOrArray instanceof JSONObject || jsonObjOrArray instanceof JSONArray)) {
//            LogUtil.d(TAG, "evmPost jsonObjOrArray format error,request cancel");
            return;
        }
        String baseUrl = getBaseUrl();
        OkGo.<String>post(baseUrl + urlInterfName).headers(headers).upJson(jsonObjOrArray.toString()).execute(callback);
    }


    public static void evmPost(String urlInterfName, Object jsonObjOrArray, AbsCallback callback, int timeout) {
        if (!(jsonObjOrArray instanceof JSONObject || jsonObjOrArray instanceof JSONArray)) {
//            LogUtil.d(TAG, "evmPost jsonObjOrArray format error,request cancel");
            return;
        }
        String baseUrl = getBaseUrl();
        OkGo.<String>post(baseUrl + urlInterfName).client(getCusTimeoutClient(timeout)).headers(getHttpHeaders()).upJson(jsonObjOrArray.toString()).execute(callback);
    }


    public static void evmPost(String urlInterfName, String jsonStr, AbsCallback callback) {
        String baseUrl = getBaseUrl();
        OkGo.<String>post(baseUrl + urlInterfName).headers(getHttpHeaders()).upJson(jsonStr).execute(callback);
    }

    private static String getBaseUrl() {
        String baseUrl = null;
//        if (Constant.SERVER_URL_RELEASE.equals(PrefsUtil.getString(PrefsUtil.KEY_SERVER_URL, ""))) {
//            baseUrl = ApiUrl.URL_HOST_RELEASE;
//        } else {
//            baseUrl = ApiUrl.URL_HOST_DEBUG;
//        }
        return baseUrl;
    }

    public static HttpHeaders getHttpHeaders(String customerCode, String vendingCode) {
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.put(ApiKey.CUSTOMER_CODE, customerCode);
//        httpHeaders.put(ApiKey.VENDING_CODE, vendingCode);
//        String requestTime = DateTimeUtils.getCurrDateTime("yyyyMMdd");
//        String encryptToken = MD5Util.getMD5(customerCode + vendingCode + ApiKey.SALT + requestTime);
//        LogUtil.e(TAG, ApiKey.AUTH_TOKEN + " : " + encryptToken);
//        httpHeaders.put(ApiKey.AUTH_TOKEN, encryptToken);
        return httpHeaders;
    }


    private static HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
//        final String customerCode = SPUtils.get(SPKey.VENDING_SP_NAME, SPKey.CUSTOMER_CODE, "").toUpperCase();
//        final String vendingCode = SPUtils.get(SPKey.VENDING_SP_NAME, SPKey.VENDING_CODE, "").toUpperCase();
//        httpHeaders.put(ApiKey.CUSTOMER_CODE, customerCode);
//        httpHeaders.put(ApiKey.VENDING_CODE, vendingCode);
//        String requestTime = DateTimeUtils.getCurrDateTime("yyyyMMdd");
//        String encryptToken = MD5Util.getMD5(customerCode + vendingCode + ApiKey.SALT + requestTime);
//        LogUtil.e(TAG, ApiKey.AUTH_TOKEN + " : " + encryptToken);
//        httpHeaders.put(ApiKey.AUTH_TOKEN, encryptToken);
        return httpHeaders;
    }
}
