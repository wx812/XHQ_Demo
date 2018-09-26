package com.xhq.demo.cmd;

/**
 * Created by zyj on 2017/1/11.
 */
public class BaseReturnCmd extends BaseCmd {

    /**
     * 错误码
     */
    public int getStatu() {
        return getInt("ec");
    }

    /**
     * 错误码
     */
    public void setStatu(int statu) {
        put("ec", statu);
    }

    /**
     * 附加信息
     */
    public String getInfo() {
        return getStr("et");
    }

    /**
     * 附加信息
     */
    public void setInfo(String info) {
        put("et", info);
    }

}
