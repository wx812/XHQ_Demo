package com.xhq.demo.db.db_sql.entity;


import com.xhq.demo.db.db_sql.entityConfig.AbsEntityDao;

/**
 * Created by zyj on 2017/4/11.
 * 附件实体dao
 */

public class AttachDao extends AbsEntityDao<AttachEntity>{

    @Override
    public AttachEntity getNewBean() {
        return new AttachEntity();
    }

    @Override
    public String getEntityName() {
        return AttachEntity.ENTITY_NAME;
    }
}
