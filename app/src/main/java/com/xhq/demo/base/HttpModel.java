package com.xhq.demo.base;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.convert.BitmapConvert;
import com.lzy.okgo.convert.FileConvert;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableBody;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.orhanobut.logger.Logger;
import com.xhq.demo.bean.BaseDataBean;
import com.xhq.demo.constant.apiconfig.ApiKey;
import com.xhq.demo.tools.ErrorMessage;
import com.xhq.demo.tools.encodeTools.EncryptUtils;
import com.xhq.demo.tools.fileTools.StorageUtils;

import java.io.File;
import java.net.ConnectException;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huangshikun Administrator on 2017/8/29.
 * 说明：作为共用的HttpModel
 * 首先m层不应该有v的东西不能用toast dialog activity,请求道的数据需要显示的请回掉p层，p层来告诉具体的view去显示，view来确定具体的显示方式
 */

public abstract class HttpModel {

    public GsonBuilder mGsonBuilder;


    protected HttpModel() {
        if (null == mGsonBuilder) {
            mGsonBuilder = new GsonBuilder();
        }
    }

    protected void downloadBitmap(String url, Map<String, String> params, final String requestType) {
        OkGo.<Bitmap>get(url)
                .params(params)
                .converter(new BitmapConvert())
                .adapt(new ObservableResponse<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmapResponse -> {
                    Bitmap bitmap = bitmapResponse.body();
                    HttpModel.this.mDownloadBitmapForResult(bitmap, requestType);
//                    Logger.t(requestType + "-解密之后的data数据").d(bitmap);
                }, throwable -> HttpModel.this.mDownloadBitmapFailForResult(throwable.getMessage(), requestType));
    }


    protected void downloadFile(String url, Map<String, String> params, final String requestType) {

        String jmzw_download = StorageUtils.getAppCacheDir("JMZW_DOWNLOAD").getAbsolutePath();

        OkGo.<File>get(url)
                .cacheMode(CacheMode.NO_CACHE)
//                .cacheKey(requestType)
//                .cacheTime(0)
                .params(params)
                .converter(new FileConvert(jmzw_download, null))
                .adapt(new ObservableBody<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> {
//                    Logger.t(requestType + "-解密之后的data数据").d(file.getTotalSpace()/1024/1024);
                    HttpModel.this.mDownloadFileForResult(requestType, file);
                }, throwable -> HttpModel.this.mDownloadBitmapFailForResult(throwable.getMessage(), requestType));
    }


    //http的get请求
    protected void httpGet(String url, Map<String, String> params, final String requestType) {
        Observable<Response<String>> observable = OkGo.<String>get(url)
                .params(params)
                .converter(new StringConvert())
                .adapt(new ObservableResponse<>());
        commonHandle(requestType, observable, params);
    }


    //http的post请求
    protected void httpPost(String url, Map<String, String> params, final String requestType,
                            File mFile_small) {
        Observable<Response<String>> observable = OkGo.<String>post(url)
                .params(params)
                .params(ApiKey.UpImgKey.upload, mFile_small)
                .converter(new StringConvert())
                .adapt(new ObservableResponse<>());
        commonHandle(requestType, observable, params);
    }


    private void commonHandle(String requestType, Observable<Response<String>> observable, Map<String, String>
            params) {
        observable.observeOn(Schedulers.computation())
                .map((Response<String> stringResponse) -> {
                    String str = stringResponse.body();
                    Gson gson = mGsonBuilder.create();
                    BaseDataBean dataBean = gson.fromJson(str, BaseDataBean.class);
                    Logger.t(requestType + "-带安全码原json数据").d(str);
                    if (dataBean.isOk()) {
                        if (dataBean.getSc().equals(EncryptUtils.getSecurityCode(dataBean.getData()))) {
                            return EncryptUtils.decrypt(dataBean.getData());
                        } else {
                            throw new RuntimeException("未授权用户");
                        }
                    } else {
                        throw new RuntimeException(dataBean.getMsg());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    Logger.t(requestType + "-解密之后的data数据").json(s);
                    Logger.t(requestType + "-解密之后的data数据").d(s);
                    HttpModel.this.mHttpForResult(s, requestType);
                    params.clear();
                }, throwable -> {
                    String errorMsg = throwable.getMessage();
                    Logger.t(requestType + "-ERROR").e(throwable, "http请求失败,具体为: " + requestType + errorMsg);
                    if (throwable instanceof ConnectException) {
                        HttpModel.this.mServiceFailForResult("无法连接服务器，请检查网络", requestType);
                        return;
                    }
                    if (TextUtils.isEmpty(errorMsg)) {
                        HttpModel.this.mServiceFailForResult("服务器错误, 错误信息为空", requestType);
                        return;
                    }
                    if ("@DFP_LOGIN_NULL@".equals(errorMsg)) {
                        HttpModel.this.mServiceFailForResult(ErrorMessage.getErrorService(errorMsg + "正在重连"),
                                                             requestType);
//                        TcpServiceSender.startActionReconnect();
                    } else {
                        HttpModel.this.mServiceFailForResult(ErrorMessage.getErrorService(errorMsg),
                                requestType);
                    }
                    params.clear();
                });
    }


    public void httpGetData(String url, Map<String, String> params, final String requestType) {
        OkGo.<String>get(url)
                .params(params)
                .converter(new StringConvert())
                .adapt(new ObservableResponse<>())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe((Response<String> stringResponse) -> {
                    String str = stringResponse.body();
                    Gson gson = mGsonBuilder.create();
                    BaseDataBean mAllBean = gson.fromJson(str, BaseDataBean.class);

                    if (mAllBean.isOk()) {
                        if (mAllBean.getSc().equals(EncryptUtils.getSecurityCode(mAllBean.getData()))) {

                            Observable.just(mAllBean.getData())
                                    .map(txt -> EncryptUtils.decrypt(txt))
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(Schedulers.io())
                                    .subscribe(s -> {
//                                        Logger.t(requestType + "-解密之后的data数据").json(s);
                                        HttpModel.this.mHttpForResult(s, requestType);
                                    });
                        }
                    }
                    params.clear();
                });
    }



    //http请求的结果
    protected abstract void mHttpForResult(String jsonStr, String mtype);

    //下载文件
    protected void mDownloadFileForResult(String requestType, File file) {}

    //下载图片
    protected void mDownloadBitmapForResult(Bitmap bitmap, String requestType) {}

    //服务器返回的错误信息
    protected abstract void mServiceFailForResult(String jsonStr, String mtype);

    //服务器返回的错误信息
    protected void mDownloadBitmapFailForResult(String jsonStr, String mtype) {

    }
}
