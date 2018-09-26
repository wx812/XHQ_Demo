package com.xhq.demo.db.db_sql.entity;

import com.xhq.demo.db.db_sql.entityConfig.AbsEntityDao;

/**
 * Created by zyj on 2017/4/11.
 * 群成员实体dao
 */

public class GroupMemberDao extends AbsEntityDao<GroupMemberEntity>{


    @Override
    public GroupMemberEntity getNewBean() {
        return new GroupMemberEntity();
    }

    @Override
    public String getEntityName() {
        return GroupMemberEntity.ENTITY_NAME;
    }
}
