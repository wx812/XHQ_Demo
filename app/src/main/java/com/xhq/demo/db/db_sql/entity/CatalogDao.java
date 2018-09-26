package com.xhq.demo.db.db_sql.entity;


import com.xhq.demo.db.db_sql.entityConfig.AbsEntityDao;

/**
 * Created by zyj on 2017/4/11.
 * 分组表实体dao
 */

public class CatalogDao extends AbsEntityDao<CatalogEntity>{

    @Override
    public CatalogEntity getNewBean() {
        return new CatalogEntity();
    }

    @Override
    public String getEntityName() {
        return CatalogEntity.ENTITY_NAME;
    }
}
