package com.xhq.demo.db.db_sql.entity;


import com.xhq.demo.db.db_sql.entityConfig.AbsEntityDao;

/**
 * Created by zyj on 2017/4/22.
 * 添加好友dao
 */

public class AddFriendDao extends AbsEntityDao<AddFriendEntity>{
    @Override
    public AddFriendEntity getNewBean() {
        return new AddFriendEntity();
    }

    @Override
    public String getEntityName() {
        return AddFriendEntity.ENTITY_NAME;
    }
}
