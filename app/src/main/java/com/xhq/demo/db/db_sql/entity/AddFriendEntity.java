package com.xhq.demo.db.db_sql.entity;


import com.xhq.demo.db.db_sql.entityConfig.AbsEntity;

/**
 * Created by zyj on 2017/4/22.
 * 添加好友entity
 */

public class AddFriendEntity extends AbsEntity{
    public static final String ENTITY_NAME = "TB_IM_ADD_FRIEND";
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

/*    //用户昵称
    public String getUserName() {
        return getStr("name");
    }

    //用户昵称
    public void setUserName(String name) {
        put("name", name);
    }*/

    //群id
    public String getGoupId() {
        return getStr("gid");
    }

    //群id
    public void setGoupId(String gid) {
        put("gid", gid);
    }

    //附加信息
    public String getMsg() {
        return getStr("msg");
    }

    //附加信息
    public void setMsg(String msg) {
        put("msg", msg);
    }

    //添加结果
    public int getRet() {
        return getInt("ret");
    }

    //添加结果
    public void setRet(int ret) {
        put("ret", ret);
    }

    //消息发送时间
    public String getSendTime() {
        return getStr("st");
    }

    //消息发送时间
    public void setSendTime(String st) {
        put("st", st);
    }

    //添加结果
    public int getIsRead() {
        return getInt("is_read");
    }

    //添加结果
    public void setIsRead(int is_read) {
        put("is_read", is_read);
    }

    //添加类型
    public int getAddType() {
        return getInt("add_type");
    }

    //添加类型
    public void setAddType(int add_type) {
        put("add_type", add_type);
    }
}
