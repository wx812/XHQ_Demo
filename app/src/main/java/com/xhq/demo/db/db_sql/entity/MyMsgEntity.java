package com.xhq.demo.db.db_sql.entity;

import com.xhq.demo.db.db_sql.entityConfig.AbsEntity;

/**
 * Created by zyj on 2017/4/11.
 * 消息指令表实体entity
 */

public class MyMsgEntity extends AbsEntity {
    public static final String ENTITY_NAME = "TB_IM_MY_MSG";

    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }

    //用户id
    public String getUserId() {
        return getStr("uid");
    }

    //用户id
    public void setUserId(String uid) {
        put("uid", uid);
    }

    //指令id
    public String getMsgId() {
        return getStr("mid");
    }

    //指令id
    public void setMsgId(String mid) {
        put("mid", mid);
    }

    //群id
    public String getGroupId() {
        return getStr("gid");
    }

    //群id
    public void setGroupId(String gid) {
        put("gid", gid);
    }

    //发送类型，0自己发的，1别人发的
    public int getSendType() {
        return getInt("send_type");
    }

    //发送类型，0自己发的，1别人发的
    public void setSendType(int send_type) {
        put("send_type", send_type);
    }

    //具体消息
    public String getMsg() {
        return getStr("msg");
    }

    //具体消息
    public void setMsg(String msg) {
        put("msg", msg);
    }

    //消息发送时间
    public String getSendTime() {
        return getStr("st");
    }

    //消息发送时间
    public void setSendTime(String st) {
        put("st", st);
    }

    //消息类型
    public int getMsgType() {
        return getInt("mtype");
    }

    //消息类型
    public void setMsgType(int mtype) {
        put("mtype", mtype);
    }

    //发送成功标记 0 未成功 ， 1 成功
    public int getSendSign() {
        return getInt("send_sign");
    }

    //发送成功标记
    public void setSendSign(int send_sign) {
        put("send_sign", send_sign);
    }

    //发送成功标记 0 未读 ， 1 已读
    public int getReadType() {
        return getInt("read_type");
    }

    //发送成功标记
    public void setReadType(int read_type) {
        put("read_type", read_type);
    }


}
