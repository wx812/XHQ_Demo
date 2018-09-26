package com.xhq.demo.cmd;


import com.xhq.demo.base.bean.BaseBean;
import com.xhq.demo.tools.dateTimeTools.DateTimeUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Akmm at 2016-01-15 15:21
 * 指令基类
 */
public abstract class BaseCmd extends BaseBean{

    public BaseCmd() {
        setTime(DateTimeUtils.getCurrDateTime_yMdHms());
    }

    //解析指令
    public void decode(JSONObject jo) throws Exception{
        String sc = jo.getString("sc");
        JSONObject data = jo.getJSONObject("data");
        Iterator<String> keys = data.keys();
        while (keys.hasNext()) {
            final String key = keys.next();
            this.put(key, data.get(key));
        }
        this.put("sc", sc);
    }



    /**
     * 消息id，客户端来的消息id
     */
    public String getMsgId() {
        return getStr("mid");
    }

    /**
     * 消息id，客户端来的消息id
     */
    public void setMsgId(String mid) {
        put("mid", mid);
    }

    /**
     * 消息类型 参见ApiEnum.CmdType
     */
    public int getMsgType() {
        return getInt("mtype");
    }

    /**
     * 消息类型 参见ApiEnum.CmdType
     */
    public void setMsgType(int type) {
        put("mtype", type);
    }

    /**
     * 发送时间
     */
    public String getTime() {
        return getStr("time");
    }

    /**
     * 发送时间
     */
    public void setTime(String time) {
        put("time", time);
    }

    public String toString() {
        List<String> keys = new ArrayList<>(getData().keySet());
        if (keys.isEmpty()) return "";
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder(64);
        for (String key : keys) {
            if (!"sc".equals(key)) {
                sb.append("_").append(getStr(key));
            }
        }
        return sb.substring(1);
    }
}