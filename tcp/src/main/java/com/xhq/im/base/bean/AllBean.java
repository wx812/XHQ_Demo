package com.xhq.im.base.bean;

/**
 * 作者:zhaoge
 * 时间:2017/7/25.
 */

public class AllBean {
    private boolean isOk;
    private String sc;
    private String msg; // failure information
    private String data;

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }
}
