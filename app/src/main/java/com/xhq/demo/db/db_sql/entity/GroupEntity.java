package com.xhq.demo.db.db_sql.entity;

import com.xhq.demo.db.db_sql.entityConfig.AbsEntity;

/**
 * Created by zyj on 2017/4/11.
 * 群实体entity
 */

public class GroupEntity extends AbsEntity {
    public static final String ENTITY_NAME = "TB_IM_GROUP";

    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }

    //群id
    public String getGroupId() {
        return getStr("gid");
    }

    //群id
    public void setGroupId(String gid) {
        put("gid", gid);
    }

    //群号
    public String getGroupNo() {
        return getStr("gno");
    }

    //群号
    public void setGroupNo(String gno) {
        put("gno", gno);
    }

    //群名称
    public String getGroupName() {
        return getStr("gname");
    }

    //群名称
    public void setGroupName(String gname) {
        put("gname", gname);
    }

    //群头像
    public String getGroupPhoto() {
        return getStr("hid");
    }

    //群头像
    public void setGroupPhoto(String hid) {
        put("hid", hid);
    }

    //群人数
    public int getGroupNum() {
        return getInt("gnum");
    }

    //群人数
    public void setGroupNum(int gnum) {
        put("gnum", gnum);
    }

    //群简介
    public String getGroupDesc() {
        return getStr("gdesc");
    }

    //群简介
    public void setGroupDesc(String gdesc) {
        put("gdesc", gdesc);
    }

    //群创建时间
    public String getBuildTime() {
        return getStr("btime");
    }

    //群创建时间
    public void setBuildTime(String btime) {
        put("btime", btime);
    }


    //群屏蔽类型
    public int getMsgType() {
        return getInt("mt");
    }

    //群屏蔽类型
    public void setMsgType(int mt) {
        put("mt", mt);
    }

    //在线人数
    public int getCatalogOnLineNum() {
        return getInt("onum");
    }

    //在线人数
    public void setCatalogOnLineNum(int onum) {
        put("onum", onum);
    }

    //群类型
    public int getGroupType() {
        return getInt("group_type");
    }

    //群类型  多人聊天等
    public void setGroupType(int group_type) {
        put("group_type", group_type);
    }

    //群成员类型
    public int getGroupMemberType() {
        return getInt("gmt");
    }

    //群成员类型
    public void setGroupMemberType(int gmt) {
        put("gmt", gmt);
    }
}
