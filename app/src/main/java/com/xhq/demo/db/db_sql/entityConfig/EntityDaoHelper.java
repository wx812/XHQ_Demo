package com.xhq.demo.db.db_sql.entityConfig;


import com.xhq.demo.db.SQLParam;
import com.xhq.demo.db.db_sql.UtilCursor;
import com.xhq.demo.db.db_sql.entityConfig.ModelConfig.ModelConsts;
import com.xhq.demo.db.db_sql.entityConfig.ModelConfig.ModelEntity;
import com.xhq.demo.db.db_sql.entityConfig.ModelConfig.ModelEntityFactory;
import com.xhq.demo.db.db_sql.entityConfig.ModelConfig.ModelField;
import com.xhq.demo.tools.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@SuppressWarnings({"unchecked", "UnusedDeclaration"})
public abstract class EntityDaoHelper {
    private final static ConcurrentMap<String, String> sqlBuffer_select_from = new ConcurrentHashMap<>(128);
    private final static ConcurrentMap<String, String> sqlBuffer_select_from_wherePK = new ConcurrentHashMap<>(128);


    /**
     * 得到实体的实体定义
     */
    public static ModelEntity getModelEntity(String entityName) {
        return ModelEntityFactory.getInstance().getModelEntity(entityName);
    }

    /**
     * 得到实体的SELECT FROM语句
     */
    public static String getSQL_select_from(String entityName) {
        return getSQL_select_from(entityName, "");
    }

    public static String getSQL_select_from_order_by_pk(String entityName) {
        String sql = sqlBuffer_select_from_wherePK.get(entityName);
        if (sql == null) {
            final ModelEntity entity = getModelEntity(entityName);
            if (entity == null) {
//                logger.error("实体定义对象[" + entityName.toUpperCase() + "]未找到");
                return "";
            }
            String s0 = entity.nameString(entity.getFields(), ", ", "");  //选取所有字段；
            String s1 = entityName;
            String s2 = entity.nameString(entity.getPkFieldName(), ",", "");
            sql = MessageFormat.format("SELECT {0} \n  FROM {1} \n  Order By {2}", s0, s1, s2);
            sqlBuffer_select_from_wherePK.putIfAbsent(entityName, sql);
        }
        return sql;
    }


    /**
     * 得到实体的SELECT FROM语句
     */
    public static String getSQL_select_from(String entityName, String tb_alias) {
        String key = entityName + tb_alias;
        String sql = sqlBuffer_select_from.get(key);
        if (sql == null) {
            final ModelEntity entity = getModelEntity(entityName);
            if (entity == null) {
//                logger.error("实体定义对象[" + entityName.toUpperCase() + "]未找到");
                return "";
            }
            String s0 = entity.nameString(entity.getFields(), !StringUtils.isEmpty(tb_alias) ? tb_alias + "." : "", ", ", "");  //选取所有字段；
            String s1 = entityName + " " + tb_alias;
            sql = MessageFormat.format("SELECT {0} \n  FROM {1}", s0, s1);
            sqlBuffer_select_from.putIfAbsent(key, sql);
        }
        return sql;
    }


    public static String getSQL_select_from(String entityName, String[] fields, String tb_alias) {
//        entity.nameString(entity.getFields(), !StringUtils.isEmpty(tb_alias) ? tb_alias + ".": "", ", ", "");  //选取所有字段；
        StringBuilder s0 = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            if (i > 0) s0.append(",");
            s0.append(!StringUtils.isEmpty(tb_alias) ? tb_alias + "." : "").append(field);
        }
        String s1 = entityName + " " + tb_alias;
        return MessageFormat.format("SELECT {0} FROM {1}", s0.toString(), s1);
    }


    public static String getSQL_select_fields(String entityName, String tb_alias) {

        final ModelEntity entity = getModelEntity(entityName);
        if (entity == null) {
//            logger.error("实体定义对象[" + entityName.toUpperCase() + "]未找到");
            return "";
        }
        return entity.nameString(entity.getFields(), !StringUtils.isEmpty(tb_alias) ? tb_alias + "." : "", ", ", "");  //选取所有字段；
    }

    /**
     * 得到实体的SELECT FROM语句, otherFields其他附加字段，如有，以,开头
     */
    public static String getSQL_select_from(String entityName, String otherFields, String tb_alias) {

        final ModelEntity entity = getModelEntity(entityName);
        if (entity == null) {
//            logger.error("实体定义对象[" + entityName.toUpperCase() + "]未找到");
            return "";
        }
        String s0 = entity.nameString(entity.getFields(), !StringUtils.isEmpty(tb_alias) ? tb_alias + "." : "", ", ", "") + otherFields;  //选取所有字段；
        String s1 = entityName + " " + tb_alias;
        return MessageFormat.format("SELECT {0} \n  FROM {1} \n", s0, s1);

    }

    /**
     * 得到实体的SELECT FROM WHERE PK语句
     */
    public static String getSQL_select_from_wherePK(String entityName) {
        String sql = sqlBuffer_select_from_wherePK.get(entityName);
        if (sql == null) {
            final ModelEntity entity = getModelEntity(entityName);
            if (entity == null) {
//                logger.error("实体定义对象[" + entityName.toUpperCase() + "]未找到");
                return "";
            }
            String s0 = entity.nameString(entity.getFields(), ", ", "");  //选取所有字段；
            String s1 = entityName;
            String pk = entity.getPkFieldName();
            ModelField modelField = entity.getField(pk);
            List<ModelField> list = new ArrayList<>();
            list.add(modelField);
            String s2 = entity.nameString(list, "=? And ", "=?");
            sql = MessageFormat.format("SELECT {0} \n  FROM {1} \n  WHERE {2}", s0, s1, s2);
            sqlBuffer_select_from_wherePK.putIfAbsent(entityName, sql);
        }
        return sql;
    }

    public static String getEntityPkName(String entityName) {
        final ModelEntity entity = getModelEntity(entityName);
        if (entity == null) {
//            logger.error("实体定义对象[" + entityName.toUpperCase() + "]未找到");
            return "";
        }
        return entity.getPkFieldName();
    }

    public static void readEntityFromMap(final AbsEntity bean, Map<String, Object> map) {
        ModelEntity modelEntity = getModelEntity(bean.getEntityName());
        if (modelEntity == null) return;
        //循环实体所有字段，从前端获取值塞入字段；
        for (String fieldName : modelEntity.getAllFieldNames()) {
            if (!map.containsKey(fieldName)) continue;
//            ModelConsts mft = modelEntity.getField(fieldName).type;
            ModelField field = modelEntity.getField(fieldName);
            bean.getData().put(fieldName, getFieldValue(map.get(fieldName), field.type));
        }
    }

    public static void readEntityFromJson(final AbsEntity bean, JSONObject json) {
        Map<String, Object> map = new HashMap<>();
        try {
            for (Iterator i = json.keys(); i.hasNext(); ) {
                String s = (String) i.next();
                map.put(s, json.getString(s));
            }
        } catch (JSONException e) {
//            UtilLogger.error("Json数据转换出错", e);
        }
        bean.getData().putAll(map);
    }

    /**
     * 从记录集中取出一条记录(新建一个Entity实例)
     */
    public static void readEntityFromRs(AbsEntity bean, UtilCursor urs, String... fieldnames) throws SQLException{
        bean.setNewRecord(false);
        ModelEntity modelEntity = getModelEntity(bean.getEntityName());
        if (modelEntity == null) return;
        if (fieldnames.length == 0) {
            fieldnames = modelEntity.getAllFieldNames().toArray(fieldnames);
        }
        //循环实体所有字段，从数据库获取值塞入字段；
        for (String fieldName : fieldnames) {
            String fname = fieldName.toLowerCase();  //转换为大写，字段定义在读取时一律转为了大写
            int t = modelEntity.getField(fieldName).type;

            bean.getData().put(fname, getFieldValue(urs.getStringIgnoreNull(fieldName), t));
        }
    }

    /**
     * 从请求中中取出一条记录(新建一个Entity实例)
     */
    @SuppressWarnings("unchecked")
    public static void readEntityFromRQ(final AbsEntity bean, Map<String, Object> pageMap) {
        ModelEntity modelEntity = getModelEntity(bean.getEntityName());
        if (modelEntity == null) return;
        //循环实体所有字段，从前端获取值塞入字段；
        for (String fieldName : modelEntity.getAllFieldNames()) {
            if (!pageMap.containsKey(fieldName)) continue;
            bean.getData().put(fieldName, getFieldValue(pageMap.get(fieldName), modelEntity.getField(fieldName).type));
        }
    }


    //=================================================================================================================
    public static String nameString(List<String> fieldNameList, String separator, String afterLast) {
        StringBuilder returnString = new StringBuilder();
        if (fieldNameList.size() < 1) {
            return "";
        }
        int i = 0;
        for (; i < fieldNameList.size() - 1; i++) {
            returnString.append(fieldNameList.get(i));
            returnString.append(separator);
        }
        returnString.append(fieldNameList.get(i));
        returnString.append(afterLast);
        return returnString.toString();
    }

    public static Object[] getFieldValueArgs(List<String> fieldNames, AbsEntity bean) {
        Object[] args = new Object[fieldNames.size()];
        fillFieldValueArgs(fieldNames, bean, args);
        return args;
    }

    //sql 语句参数值
    public static void fillFieldValueArgs(List<String> fieldNames, AbsEntity bean, Object[] args) {
        ModelEntity modelEntity = getModelEntity(bean.getEntityName());
        if (modelEntity == null) return;
        //获取实体类字段对象；
        int i = 0;
        StringBuilder fieldVal = null;
        boolean isFirst = true;
        for (String fieldName : fieldNames) {
            String fname = fieldName.toLowerCase();  //转换为小写，因此在bean中定义的字段都是小写的；
            Object v = bean.getData().get(fname);
            String value = v != null ? v.toString() : "";
            final ModelField field = modelEntity.getField(fname);
            if (field != null) {
                if (StringUtils.isEmpty(value) && !StringUtils.isEmpty(field.defaultValue)) {
                    value = field.defaultValue;
                }
                int mft = field.type;
                if (mft == ModelConsts.FieldType.CHAR.value) {
                    args[i++] = value;
                }
                if (mft == ModelConsts.FieldType.INT.value) {
                    args[i++] = StringUtils.toInt(value);
                }
                if (mft == ModelConsts.FieldType.NUMERIC.value) {
                    args[i++] = StringUtils.toDouble(value);
                }
            } else {
                args[i++] = v;
            }
        }
//        if(SysParams.isdev) UtilLogger.debug(bean.getEntityName() + "[" + fieldVal.toString() + "]");
    }

    //构建Sql语句的Sql。Type
    public static int[] buildTypes(ModelEntity modelEntity, List<ModelField> fields) {
        int[] types = new int[fields.size()];
        for (int i = 0, len = fields.size(); i < len; i++) {
            int type = fields.get(i).type;
            if(type == ModelConsts.FieldType.CHAR.value){
                types[i] = Types.VARCHAR;
            }
            if(type == ModelConsts.FieldType.INT.value){
                types[i] = Types.INTEGER;
            }
            if(type == ModelConsts.FieldType.NUMERIC.value){
                types[i] = Types.NUMERIC;
            }
        }
        return types;
    }

    //构建insert语句
    public static String buildInsertSql(ModelEntity modelEntity) {
        String s0 = modelEntity.getEntityName();
        String s1 = modelEntity.nameString(modelEntity.getFields(), ", ", "");
        String s2 = UtilCursor.cutStringRight(UtilCursor.repeatString("?,", modelEntity.getFieldsSize()), ",");
        return MessageFormat.format("INSERT INTO {0}\n ({1})\n VALUES \n ({2})", s0, s1, s2);
    }

    //构建create 语句
    public static String buildCreateSql(ModelEntity modelEntity) {
        String s0 = modelEntity.getEntityName();
        String s1 = modelEntity.getPkFieldName();
        String s2 = modelEntity.typeNameString(modelEntity.getNoPkFields());
        return MessageFormat.format("CREATE TABLE IF NOT EXISTS {0}({1}  TEXT PRIMARY KEY NOT NULL ,{2});", s0, s1, s2);
    }

    //构建update语句
    public static String buildUpdateSql(ModelEntity modelEntity) {
        String s0 = modelEntity.getEntityName();
        String s1 = modelEntity.nameString(modelEntity.getNoPkFields(), "=?, ", "=? ");
        String s2 = modelEntity.getPkFieldName() + " = ?";
        return MessageFormat.format("UPDATE {0} SET \n {1}\n  WHERE {2}", s0, s1, s2);
    }

    //构建insert语句
    public static SQLParam buildInsertSql(AbsEntity value) throws Exception{
        value.setLastTime(UtilCursor.nowDateTimeNumber());

        String entityName = value.getEntityName();
        if (entityName == null) {
            return null;
        }
        ModelEntity modelEntity = EntityDaoHelper.getModelEntity(entityName);
        if (modelEntity == null) {
            return null;
        }

        String sql = EntityDaoHelper.buildInsertSql(modelEntity);
        Object[] args = EntityDaoHelper.getFieldValueArgs(modelEntity.getAllFieldNames(), value);
        return new SQLParam(sql, args);
    }

    //构建update语句
    public static SQLParam buildUpdateSql(AbsEntity value) throws Exception{
        String entityName = value.getEntityName();
        if (entityName == null) {
//            logger.e("Entity Class:" + value.getClass() + "没有实现方法getEntityName");
            return null;
        }
        ModelEntity modelEntity = EntityDaoHelper.getModelEntity(entityName);
        if (modelEntity == null) {
//            logger.error("Entity:" + entityName.toUpperCase() + "未找到实体定义！");
            return null;
        }
        String sql = EntityDaoHelper.buildUpdateSql(modelEntity);

        long lastTime = value.getLastTime();
        value.setLastTime(UtilCursor.getLastTime());
        //获取实体类字段对象；
        List<String> fieldnames = new ArrayList<>(modelEntity.getNoPkFieldNames());  //非主键字段；
        fieldnames.add(modelEntity.getPkFieldName());  //主键字段；
        Object[] args;
        if (lastTime != 0) {
            sql += " and last_time=?";
            int fs = fieldnames.size();
            args = new Object[fs + 1];
            EntityDaoHelper.fillFieldValueArgs(fieldnames, value, args);
            args[fs] = lastTime; //乐观锁
        } else {
            args = new Object[fieldnames.size()];
            EntityDaoHelper.fillFieldValueArgs(fieldnames, value, args);
        }
        return new SQLParam(sql, args);
    }

    /**
     * 根据字段类型，转换值
     * @param value
     * @param fieldType
     * @return
     */
    private static Object getFieldValue(Object value, int fieldType) {
        String strValue = value != null ? value.toString() : "";
        if (fieldType == ModelConsts.FieldType.INT.value) {
            return StringUtils.toInt(strValue);
        }
        if (fieldType == ModelConsts.FieldType.NUMERIC.value) {
            return StringUtils.toDouble(strValue);
        }
        return value;
    }
}
