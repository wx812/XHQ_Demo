package com.xhq.demo.db.db_sql.entityConfig.ModelConfig;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("UnusedDeclaration")
public class ModelIndex {
    protected ModelEntity mainEntity;
    protected String name; //index name
    protected List<String> fieldNames = new ArrayList<>();  //索引字段；

    public ModelIndex(ModelEntity mainEntity) {
        this.mainEntity = mainEntity;
    }


    //以所包含的字段名作为key，保证不要重复定义
    public String getKey() {
        StringBuilder sb = new StringBuilder(128);
        for (String field: fieldNames) {
            sb.append(",").append(field);
        }
        return sb.substring(1);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ModelEntity getMainEntity() {
        return this.mainEntity;
    }

    public void setMainEntity(ModelEntity mainEntity) {
        this.mainEntity = mainEntity;
    }

    public Iterator<String> getIndexFieldsIterator() {
        return this.fieldNames.iterator();
    }

    public int getIndexFieldsSize() {
        return this.fieldNames.size();
    }

    public String getIndexField(int index) {
        return this.fieldNames.get(index);
    }

    public void addIndexField(String fieldName) {
        this.fieldNames.add(fieldName);
    }

    public String removeIndexField(int index) {
        return this.fieldNames.remove(index);
    }

}