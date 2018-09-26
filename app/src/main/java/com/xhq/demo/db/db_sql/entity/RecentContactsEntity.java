package com.xhq.demo.db.db_sql.entity;

import com.xhq.demo.db.db_sql.entityConfig.AbsEntity;

/**
 * Created by zyj on 2017/4/13.
 * 最近联系人entity
 */

public class RecentContactsEntity extends AbsEntity{
    public static final String ENTITY_NAME = "TB_IM_RECENT_CONTACTS";
    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }

    //id
    public String getId() {
        return getStr("id");
    }

    //id
    public void setId(String id) {
        put("id", id);
    }

    //用户id
    public String getUserId() {
        return getStr("uid");
    }

    //用户id
    public void setUserId(String uid) {
        put("uid", uid);
    }

    //群id
    public String getGoupId() {
        return getStr("gid");
    }

    //群id
    public void setGoupId(String gid) {
        put("gid", gid);
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

    //未读消息个数
    public int getUnreadNum() {
        return getInt("unread_num");
    }

    //未读消息个数
    public void setUnreadNum( int unread_num) {
        put("unread_num", unread_num);
    }
}
