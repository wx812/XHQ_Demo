package com.xhq.demo.db.db_sql.entity;

import com.xhq.demo.db.db_sql.entityConfig.AbsEntityDao;

/**
 * Created by zyj on 2017/4/11.
 * 群实体dao
 */

public class GroupDao extends AbsEntityDao<GroupEntity>{


    @Override
    public GroupEntity getNewBean() {
        return new GroupEntity();
    }

    @Override
    public String getEntityName() {
        return GroupEntity.ENTITY_NAME;
    }
}
