//package com.xhq.MyDemo.Service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Binder;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//import android.util.ArrayMap;
//
//import com.google.gson.Gson;
//import com.lzy.okgo.OkGo;
//import com.lzy.okgo.convert.StringConvert;
//import com.lzy.okgo.model.Response;
//import com.lzy.okrx2.adapter.ObservableResponse;
//import com.orhanobut.logger.Logger;
//import com.xhq.MyDemo.Tools.JJKJUtils;
//import com.xhq.MyDemo.constant.ConstantValue;
//import com.xhq.MyDemo.constant.apiconfig.ApiEnum;
//import com.xhq.MyDemo.constant.apiconfig.ApiKey;
//import com.xhq.MyDemo.constant.apiconfig.ApiUrl;
//
//import java.io.File;
//import java.util.Map;
//
//import io.reactivex.Observer;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.annotations.NonNull;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
//
//
///**
// * <pre>
// *     Auth  : ${XHQ}.
// *     Time  : 2017/11/6.
// *     Desc  : Description.
// *     Updt  : Description.
// * </pre>
// */
//public class CatchCrashService extends Service{
//
//    private MyBinder iBinder = new MyBinder();
//
//
//    @Override
//    public void onCreate(){
//        super.onCreate();
//    }
//
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return iBinder;
//    }
//
//
//    @Override
//    public boolean onUnbind(Intent intent){
//        return super.onUnbind(intent);
//    }
//
//
//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//    }
//
//
//    public class MyBinder extends Binder implements IUploadExceptionFile{
//
//
//        @Override
//        public void uploadExceptionFile(final File errorFile){
//
//            ArrayMap<String, String> mExceptionMap =  new ArrayMap<>();
//            String fileType = String.valueOf(ApiEnum.FileType.FILE.value);
//            Map<String, String> uploadFileParams = JJKJUtils.setFileNecessaryField(mExceptionMap,
//                                                                                   ConstantValue.CRASH_LOG_PATH, fileType);
//            OkGo.<String>post(ApiUrl.URL_HOST + ApiUrl.URL_UPIMAGE)
//                    .params(uploadFileParams)
//                    .params(ApiKey.UpImgKey.upload, errorFile)
//                    .converter(new StringConvert())
//                    .adapt(new ObservableResponse<>())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<Response<String>>(){
//
//                        @Override
//                        public void onSubscribe(@NonNull Disposable d){
//                        }
//
//
//                        @Override
//                        public void onNext(@NonNull Response<String> stringResponse){
//
//                            String str = stringResponse.body();
////                            Gson gson = new Gson();
////                            AllBean mAllBean = gson.fromJson(str, AllBean.class);
////                            if(mAllBean.isOk()){
////                                Logger.e(str + "....." + "上传成功");
////                                errorFile.delete();
//                                CatchCrashService.this.stopSelf();
//                            }
//                        }
//
//
//                        @Override
//                        public void onError(@NonNull Throwable e){
//                            CatchCrashService.this.stopSelf();
//                        }
//
//
//                        @Override
//                        public void onComplete(){
//
//                        }
//                    });
//
//
////            HashMap<String, String> mExceptionMap = new HashMap<>();
////            String fileType = String.valueOf(ApiEnum.FileType.FILE.value);
////            HashMap<String, String> uploadFileParams = JJKJUtils.setFileNecessaryField(mExceptionMap,
////                                                                                       ConstantValue.CRASH_LOG_PATH, fileType);
////
////            Logger.t("xhq").e(errorFile.getAbsolutePath());
////            OkGo.<String>post(ApiUrl.URL_HOST + ApiUrl.URL_UPIMAGE)
////                    .params(uploadFileParams)
////                    .params(ApiKey.UpImgKey.upload, errorFile)
////                    .converter(new StringConvert())
////                    .adapt(new ObservableResponse<String>())
////                    .subscribeOn(Schedulers.io())
////                    .observeOn(AndroidSchedulers.mainThread())
////                    .subscribe(new Observer<Response<String>>(){
////
////                        @Override
////                        public void onSubscribe(@NonNull Disposable d){
////                        }
////
////
////                        @Override
////                        public void onNext(@NonNull Response<String> stringResponse){
////
////                            String str = stringResponse.body();
////                            Gson gson = new Gson();
////                            AllBean mAllBean = gson.fromJson(str, AllBean.class);
////                            if(mAllBean.isOk()){
////                                Logger.e(str + "....." + "上传成功");
////                                errorFile.delete();
////                            }
////                        }
////
////
////                        @Override
////                        public void onError(@NonNull Throwable e){
////                        }
////
////
////                        @Override
////                        public void onComplete(){
////
////                        }
////                    });
//
//
//
//
//
//        }
//    }
//
//
//    public interface IUploadExceptionFile{
//        void uploadExceptionFile(File errorFile);
//    }
//
//
//}
