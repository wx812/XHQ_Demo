package com.xhq.demo.db.db_sql.entity;

import com.xhq.demo.db.db_sql.entityConfig.AbsEntity;

/**
 * Created by zyj on 2017/4/11
 * 群成员实体entity
 */

public class GroupMemberEntity extends AbsEntity {
    public static final String ENTITY_NAME = "TB_IM_GROUP_MEMBER";

    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }

    //群成员id  key
    public String getMemberId() {
        return getStr("member_id");
    }

    //群成员id
    public void setMemberId(String member_id) {
        put("member_id", member_id);
    }

    //群id
    public String getGoupId() {
        return getStr("gid");
    }

    //群id
    public void setGoupId(String gid) {
        put("gid", gid);
    }

    //用户id
    public String getUserId() {
        return getStr("uid");
    }

    //用户id
    public void setUserId(String uid) {
        put("uid", uid);
    }

    //群成员类别
    public int getGroupMemberType() {
        return getInt("gmt");
    }

    //群成员类别
    public void setGroupMemberType(int gmt) {
        put("gmt", gmt);
    }

    //群名片
    public String getMemberName() {
        return getStr("gnote");
    }

    //群名片
    public void setMemberName(String gnote) {
        put("gnote", gnote);
    }



}
