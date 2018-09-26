package com.xhq.demo.db.db_sql.entity;

import com.xhq.demo.db.db_sql.entityConfig.AbsEntityDao;

/**
 * Created by zyj on 2017/4/11.
 * im用户表
 */

public class UserBaseDao extends AbsEntityDao<UserBaseEntity>{

    @Override
    public UserBaseEntity getNewBean() {
        return new UserBaseEntity();
    }

    @Override
    public String getEntityName() {
        return UserBaseEntity.ENTITY_NAME;
    }
}
