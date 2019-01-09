package com.xhq.demo.http;

/**
 * Created by mazhuang on 2017/5/22.
 */

public class BaseResponse<T> {
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_AUTH_FAILED = -200;
    public static final int RESULT_OPERATION_REPEATED = -301;
    private int result; // 准备废弃的字段
    private String code;
    private String message;
    private T data;

    public boolean isSuccess() {
        return result == RESULT_SUCCESS;
    }

    public int getResult() {
        return result;
    }

    public String getCode(){
        return code;
    }


    public void setCode(String code){
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
