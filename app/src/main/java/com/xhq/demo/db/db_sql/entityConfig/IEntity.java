package com.xhq.demo.db.db_sql.entityConfig;

/**
 * 实体类型.
 */
public interface IEntity {
    /**
     * 获取实体主键值
     *
     * @return String pk value
     */
    String getEntityId();

    /**
     * 设置主键值
     *
     * @param _id 主键值
     */
    void setEntityId(String _id);

    /**
     * 实体名称；add by huchb in 2013.11.08
     */
    String getEntityName();
}