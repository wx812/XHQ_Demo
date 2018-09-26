package com.xhq.demo.db.db_sql.entityConfig.ModelConfig;

/**
 * Created by Akmm at 2017/4/13 11:19
 */
public interface ModelConsts {
    //错误码
    enum FieldType {
        CHAR(0, "CHAR", "字符"),
        INT(1, "INTEGER", "整型"),
        NUMERIC(2, "REAL", "浮点数");

        public int value;
        public String sqlType;
        public String name;

        FieldType(int value, String sqlType, String name) {
            this.value = value;
            this.sqlType = sqlType;
            this.name = name;
        }

        public static String getSqlTypeByValue(int value){
            for (ModelConsts.FieldType bs : ModelConsts.FieldType.values()) {
                if (bs.value == value) return bs.sqlType;
            }
            return null;
        }
    }
}
