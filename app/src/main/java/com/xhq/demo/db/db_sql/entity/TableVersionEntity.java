package com.xhq.demo.db.db_sql.entity;


import com.xhq.demo.db.db_sql.entityConfig.AbsEntity;

/**
 * Created by zyj on 2017/5/8.
 * 表版本号entity
 */

public class TableVersionEntity extends AbsEntity{
    public static final String ENTITY_NAME = "TB_IM_TABLE_DBVERSION";
    @Override
    public String getEntityName() {
      return ENTITY_NAME;
    }

    //表名
    public String getTableName() {
        return getStr("table_name");
    }

    //表名
    public void setTableName(String table_name) {
        put("table_name", table_name);
    }

    //版本号
    public int getVersion() {
        return getInt("version");
    }

    //版本号
    public void setVersion(int version) {
        put("version", version);
    }
}
