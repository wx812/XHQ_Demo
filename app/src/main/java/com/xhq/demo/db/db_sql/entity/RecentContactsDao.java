package com.xhq.demo.db.db_sql.entity;

import com.xhq.demo.db.db_sql.entityConfig.AbsEntityDao;

/**
 * Created by zyj on 2017/4/13.
 * 最近联系人实体dao
 */

public class RecentContactsDao extends AbsEntityDao<RecentContactsEntity> {
    @Override
    public RecentContactsEntity getNewBean() {
        return new RecentContactsEntity();
    }

    @Override
    public String getEntityName() {
        return RecentContactsEntity.ENTITY_NAME;
    }
}
