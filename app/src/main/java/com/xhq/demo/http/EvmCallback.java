package com.xhq.demo.http;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.xhq.demo.constant.ConstantValue;
import com.xhq.demo.constant.apiconfig.ApiKey;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;


/**
 * Created by xiongan on 2017/2/20.
 */

public abstract class EvmCallback<T> extends StringCallback{
    public static final String TAG = EvmCallback.class.getName();
    private Activity activity;
    private static final Gson GSON = new Gson();

    public EvmCallback() {
    }

    private EvmCallback(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onStart(Request<String, ? extends Request> request) {
        super.onStart(request);
        if (activity != null) {
//            activity.runOnUiThread(() -> DialogUtils.getInstance().showLoadingDialog(activity, activity.getResources().getString(R.string.loading)));
        }
    }

    @Override
    public void onSuccess(Response<String> response) {
        try {
            BaseResponse baseResponse;
            String dataStatusCode;
            String statusMsg;
            T data;

            String jsonStr = response.body();

            Type superclassType = this.getClass().getGenericSuperclass();
            if (superclassType instanceof ParameterizedType) {
                Type[] types = ((ParameterizedType) superclassType).getActualTypeArguments();
                Type type = new ParameterizedTypeImpl(BaseResponse.class, new Type[]{types[0]}, null);
                baseResponse = GSON.fromJson(jsonStr, type);
                dataStatusCode = baseResponse.getCode();
                if (TextUtils.isEmpty(dataStatusCode)) {
                    dataStatusCode = String.valueOf(baseResponse.getResult());
                }
                statusMsg = baseResponse.getMessage();
                data = (T) baseResponse.getData();
            } else {
                JSONObject object = new JSONObject(jsonStr);
                dataStatusCode = object.optString(ApiKey.STATUS_CODE);
                if (TextUtils.isEmpty(dataStatusCode)) {
                    dataStatusCode = object.optString(ApiKey.STATUS_RESULT);
                }
                statusMsg = object.optString(ApiKey.STATUS_MSG);
                data = (T) object.optString(ApiKey.DATA);
            }

            if (ConstantValue.SUCCESS_CODE.equals(dataStatusCode)) {
                OnEvmSuccess(data);
            } else {
                onEvmFailure(dataStatusCode, statusMsg);
            }
            if (activity != null) {
                Toast.makeText(activity, statusMsg, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            String parseError = "JSON解析失败";
            onEvmFailure(parseError, parseError);
            if (activity != null) {
                Toast.makeText(activity, parseError, Toast.LENGTH_LONG).show();
            }
//            LogUtil.e(TAG, parseError, e);
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Response<String> response) {
        super.onError(response);
        onEvmFailure(String.valueOf(response.code()), response.getException().getMessage());
//        LogUtil.e(TAG, "网络请求失败 --> " + "错误码 : " + response.code(), response.getException());
    }

    protected abstract void OnEvmSuccess(T baseData);

    protected abstract void onEvmFailure(String dataStatusCode, String errorMsg);

    @Override
    public void onFinish() {
        super.onFinish();
        if (activity != null) {
//            activity.runOnUiThread(() -> DialogUtils.getInstance().disLoadingDialog());
        }
    }

    /**
     * 用于Gson解析泛型, 实现ParameterizedType的类, 必须重写 equals和 hashCode方法.
     */
    private static class ParameterizedTypeImpl implements ParameterizedType {
        private final Type[] actualTypeArguments;
        private final Type ownerType;
        private final Type rawType;


        ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments, Type ownerType) {
            this.actualTypeArguments = actualTypeArguments;
            this.ownerType = ownerType;
            this.rawType = rawType;
        }


        public Type[] getActualTypeArguments() {
            return this.actualTypeArguments;
        }


        public Type getOwnerType() {
            return this.ownerType;
        }


        public Type getRawType() {
            return this.rawType;
        }


        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                ParameterizedTypeImpl that = (ParameterizedTypeImpl) o;
                if (!Arrays.equals(this.actualTypeArguments, that.actualTypeArguments)) {
                    return false;
                } else {
                    if (this.ownerType != null) {
                        if (this.ownerType.equals(that.ownerType)) {
                            return this.rawType != null ? this.rawType.equals(that.rawType) : that.rawType == null;
                        }
                    } else if (that.ownerType == null) {
                        return this.rawType != null ? this.rawType.equals(that.rawType) : that.rawType == null;
                    }

                    return false;
                }
            } else {
                return false;
            }
        }


        public int hashCode() {
            int result = this.actualTypeArguments != null ? Arrays.hashCode(this.actualTypeArguments) : 0;
            result = 31 * result + (this.ownerType != null ? this.ownerType.hashCode() : 0);
            result = 31 * result + (this.rawType != null ? this.rawType.hashCode() : 0);
            return result;
        }
    }

}
