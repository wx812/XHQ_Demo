package com.xhq.demo.model;

import android.util.ArrayMap;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.xhq.demo.constant.ConstantValue;
import com.xhq.demo.constant.apiconfig.ApiEnum;
import com.xhq.demo.constant.apiconfig.ApiKey;
import com.xhq.demo.constant.apiconfig.ApiUrl;
import com.xhq.demo.tools.MapUtils;
import com.xhq.demo.tools.fileTools.StorageUtil;
import com.xhq.demo.tools.spTools.SPKey;
import com.xhq.demo.tools.spTools.SPUtils;

import java.io.File;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Description -->
 * Auth --> Created by ${XHQ} on 2017/8/31.
 */

public class CrashModel{

    private File crashFile;


    public CrashModel(){
        uploadExceptionFile();
    }


    private void uploadExceptionFile(){

        String accountStr = SPUtils.get(SPKey.USER_CONFIG, SPKey.USER_ACCOUNT, "");
        final String fileName = "Crash-" + accountStr + ".log";

        File crashDir = StorageUtil.getAppCacheDir("crash");
        File[] crashFiles = crashDir.listFiles();
        for(File crashFile1 : crashFiles){
            if(crashFile1.getAbsoluteFile().isDirectory()) continue;

            String exceptionFile = crashFile1.getName();

            if(!fileName.equals(exceptionFile)) continue;

            crashFile = crashFile1.getAbsoluteFile();
            break;
        }

        if(null == crashFile) return;

        ArrayMap<String, String> exceptionMap = new ArrayMap<>();
        String fileType = String.valueOf(ApiEnum.FileType.FILE.value);
        Map<String, String> uploadFileParams = MapUtils.setFileNecessaryField(exceptionMap,
                                                                              ConstantValue.CRASH_LOG_PATH, fileType);
        OkGo.<String>post(ApiUrl.URL_HOST + ApiUrl.URL_UPIMAGE)
                .params(uploadFileParams)
                .params(ApiKey.UpImgKey.upload, crashFile)
                .converter(new StringConvert())
                .adapt(new ObservableResponse<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<String>>(){

                    @Override
                    public void onSubscribe(@NonNull Disposable d){
                    }


                    @Override
                    public void onNext(@NonNull Response<String> stringResponse){

                        String str = stringResponse.body();
                        Gson gson = new Gson();
//                        AllBean mAllBean = gson.fromJson(str, AllBean.class);
//                        if(mAllBean.isOk()){
//                            Logger.e(str + "....." + "上传成功");
//                            crashFile.delete();
////                                CatchCrashService.this.stopSelf();
//                        }
                    }


                    @Override
                    public void onError(@NonNull Throwable e){
//                            CatchCrashService.this.stopSelf();
                    }


                    @Override
                    public void onComplete(){

                    }
                });

        exceptionMap.clear();
        uploadFileParams.clear();
    }


}
