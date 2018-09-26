package com.xhq.demo.db.db_sql.entityConfig.ModelConfig;


/**
 * 一条字段定义；
 */
public class ModelField implements Cloneable{

    public String name = "";
    public String title = "";
    public int type;
    public int length = 0;
    public int precision = 0;
    public String defaultValue = null;

}
