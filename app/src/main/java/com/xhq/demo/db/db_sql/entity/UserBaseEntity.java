package com.xhq.demo.db.db_sql.entity;

import com.xhq.demo.db.db_sql.entityConfig.AbsEntity;

/**
 * Created by zyj on 2017/4/11.
 * im用户表
 */

public class UserBaseEntity extends AbsEntity {
    public static final String ENTITY_NAME = "TB_IM_USER_BASE";

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

    //居家号
    public String getUserCode() {
        return getStr("uc");
    }

    //居家号
    public void setUserCode(String uc) {
        put("uc", uc);
    }

    //昵称
    public String getNickName() {
        return getStr("name");
    }

    //昵称
    public void setNickName(String name) {
        put("name", name);
    }

    //昵称
    public String getContactNote() {
        return getStr("cnote");
    }

    //昵称
    public void setContactNote(String cnote) {
        put("cnote", cnote);
    }

    //个性签名
    public String getSignature() {
        return getStr("pl");
    }

    //个性签名
    public void setSignature(String pl) {
        put("pl", pl);
    }

    //用户在线状态
    public int getUserStatu() {
        return getInt("us");
    }

    //用户在线状态
    public void setUserStatu(int us) {
        put("us", us);
    }

    //头像id
    public String getPhotoId() {
        return getStr("hid");
    }

    //头像id
    public void setPhotoId(String hid) {
        put("hid", hid);
    }

    //分组id
    public String getCatalogId() {
        return getStr("cid");
    }

    //分组id
    public void setCatalogId(String cid) {
        put("cid", cid);
    }

    //身份类别
    public int getUserType() {
        return getInt("ut");
    }

    //身份类别
    public void setUserType(int ut) {
        put("ut", ut);
    }

    //屏蔽类型
    public int getMsgType() {
        return getInt("mt");
    }

    //屏蔽类型
    public void setMsgType(int mt) {
        put("mt", mt);
    }


    //手机号
    public String getTel() {
        return getStr("tel");
    }

    //手机号
    public void setTel(String tel) {
        put("tel", tel);
    }


    //性别
    public int getSex() {
        return getInt("sex");
    }

    //性别
    public void setSex(int sex) {
        put("sex", sex);
    }

    //生日
    public void setBirthday(String birthday) {
        put("birthday", birthday);
    }

    //生日
    public String getBirthday() {
        return getStr("birthday");
    }

    public String getPname() {
        return getStr("pname");
    }
    public void setPname(String pname) {
        put("pname", pname);
    }

    public String getPuid() {
        return getStr("puid");
    }
    public void setPuid(String puid) {
        put("puid", puid);
    }

    public String getCity() {
        return getStr("ac");
    }
    public void setCity(String ac) {
        put("ac", ac);
    }


    public int getIa() {
        return getInt("ia");
    }
    public void setIa(int ia) {
        put("ia", ia);
    }
}
