package com.xhq.demo.db.db_sql.entityConfig;

import com.xhq.demo.base.bean.BaseBean;
import com.xhq.demo.db.db_sql.entityConfig.ModelConfig.ModelEntity;
import com.xhq.demo.db.db_sql.entityConfig.ModelConfig.ModelField;
import com.xhq.demo.tools.StringUtils;

import java.io.Serializable;

public abstract class AbsEntity extends BaseBean implements IEntity, Serializable, Cloneable{
    public ModelEntity getModel() {
        return EntityDaoHelper.getModelEntity(getEntityName());
    }

    private boolean newRecord = true;//是否新增记录

    //根据实体定义，设默认值
    public void init() {
        ModelEntity entity = getModel();
        if (entity == null) return;
        for (ModelField field : entity.getFields()) {
            if (data.containsKey(field.name)) continue;//有值了，不能动
            String s = field.defaultValue;
            if (!StringUtils.isEmpty(s)) put(field.name, s);
        }
    }

    @Override
    public String getEntityId() {
        return getStr(getPkFieldName());
    }

    @Override
    public void setEntityId(String _id) {
        data.put(getPkFieldName(), _id);
    }

    public String getPkFieldName() {
        return getModel().getPkFieldName();
    }

    public boolean isNewRecord() {
        return newRecord;
    }

    public void setNewRecord(boolean newRecord) {
        this.newRecord = newRecord;
    }

    public Long getLastTime() {
        return getLong("last_time");
    }

    public void setLastTime(long last_time) {
        put("last_time", last_time);
    }

    @Override
    public AbsEntity clone() throws CloneNotSupportedException{
        AbsEntity bean = (AbsEntity) super.clone();
        bean.getData().remove(getPkFieldName());
        return bean;
    }
}
