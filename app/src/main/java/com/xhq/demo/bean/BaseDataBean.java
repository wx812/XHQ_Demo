package com.xhq.demo.bean;

/**
 * <pre>
 *     Auth  : ${XHQ}.
 *     Time  : 2016/9/1.
 *     Desc  : base bean
 *     Updt	 : Description
 * </pre>
 */
public class BaseDataBean{
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
