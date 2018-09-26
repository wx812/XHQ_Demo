package com.xhq.demo.db.db_sql.entityConfig.ModelConfig;


import java.util.HashMap;

/**
 * 数据库配置信息： 库清单 + 实体定义文件 + 使用实体清单 + 字段类型映射 + 实体定义；
 * 库名中sys对应所配置的数据源，其他一律为jt_[库名]
 */
@SuppressWarnings("UnusedDeclaration")
public class ModelConfigUtil {
    private HashMap<String,ModelConsts.FieldType > fieldTypes = new HashMap<>();    //字段类型映射；
    private static ModelConfigUtil instance = null;

    static {
        instance = new ModelConfigUtil();
    }

    private ModelConfigUtil() {
    }

    public static ModelConfigUtil getInstance() {
        return instance;
    }

    /**
     * 获取指定类型的FieldType
     *
     * @param type 类型
     * @return null or ModelFieldType
     */
    public ModelConsts.FieldType getModelFieldType(String type) {
        if (type == null || type.length() == 0) return null;
        return fieldTypes.get(type);
    }

    public HashMap<String, ModelConsts.FieldType > getFieldTypes() {
        return fieldTypes;
    }

}
