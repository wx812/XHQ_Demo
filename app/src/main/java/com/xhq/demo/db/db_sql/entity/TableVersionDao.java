package com.xhq.demo.db.db_sql.entity;

import com.xhq.demo.db.db_sql.entityConfig.AbsEntityDao;

/**
 * Created by zyj on 2017/5/8.
 版本号实体DAO
 */

public class TableVersionDao extends AbsEntityDao<TableVersionEntity>{
    @Override
    public TableVersionEntity getNewBean() {
        return new TableVersionEntity();
    }

    @Override
    public String getEntityName() {
        return TableVersionEntity.ENTITY_NAME;
    }
}
