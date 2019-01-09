package com.xhq.demo.db.db_sql.entityConfig;


import android.util.Log;

import com.xhq.demo.db.SQLParam;
import com.xhq.demo.db.db_sql.DBHelper;
import com.xhq.demo.db.db_sql.UtilCursor;
import com.xhq.demo.db.db_sql.entityConfig.ModelConfig.ModelEntity;
import com.xhq.demo.db.db_sql.entityConfig.ModelConfig.ModelField;
import com.xhq.demo.db.db_sql.interf.IBeanWorker;
import com.xhq.demo.db.db_sql.interf.IDBCallback;
import com.xhq.demo.tools.StringUtils;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "UnusedDeclaration"})
public abstract class AbsEntityDao<T extends AbsEntity> {
    public static final String TAG = AbsEntityDao.class.getName();

    public abstract T getNewBean();

    public abstract String getEntityName();

    public ModelEntity getModelEntity() {
        return EntityDaoHelper.getModelEntity(getEntityName());
    }

    public String getPkFieldName() {
        ModelEntity entity = getModelEntity();
        return entity.getPkFieldName();
    }

    //读bean的方法
    protected void readEntityFromRs(T bean, UtilCursor urs, String... fieldnames){
        EntityDaoHelper.readEntityFromRs(bean, urs, fieldnames);
    }

    public void insert(T value){
        SQLParam sqlParam = EntityDaoHelper.buildInsertSql(value);
        if (sqlParam != null) {
            DBHelper.update(sqlParam.sql, sqlParam.paras);
        }
    }

    public void batchInsert(List<T> list){
        if (list.isEmpty()) return;
        T value = list.get(0);
        String entityName = value.getEntityName();
        if (entityName == null) {
            return;
        }
        ModelEntity modelEntity = getModelEntity();
        if (modelEntity == null) {
            return;
        }

        String sql = EntityDaoHelper.buildInsertSql(modelEntity);

        Object[] listArgs = new Object[list.size()];
        final List<String> allFieldNames = modelEntity.getAllFieldNames();
        for (int i = 0, len = list.size(); i < len; i++) {
            value = list.get(i);
            value.setLastTime(UtilCursor.nowDateTimeNumber());
            listArgs[i] = EntityDaoHelper.getFieldValueArgs(allFieldNames, value);
        }
        DBHelper.updateBatchSameSql(sql, listArgs);
    }

    //批量更新
    public void batchUpdate(List<T> list, String... fieldname){
        if (list.isEmpty()) return;
        T value = list.get(0);
        String entityName = value.getEntityName();
        if (entityName == null) {
            return;
        }
        ModelEntity modelEntity = getModelEntity();
        if (modelEntity == null) {
            return;
        }

        String sql;
        int[] types;
        final List<String> allFieldNames;
        if (fieldname.length == 0) {
            sql = EntityDaoHelper.buildUpdateSql(modelEntity);
            List<ModelField> fields = new ArrayList<>(modelEntity.getNoPkFields());
            fields.add(modelEntity.getPkField());
            allFieldNames = new ArrayList<>(modelEntity.getNoPkFieldNames());
            allFieldNames.add(modelEntity.getPkFieldName());
        } else {
            allFieldNames = new ArrayList<>();  //非主键字段；
            Collections.addAll(allFieldNames, fieldname); //把传递进来的字段转换成List;

            String s0 = modelEntity.getEntityName();
            String s1 = EntityDaoHelper.nameString(allFieldNames, "=?, ", "=? ");
            String s2 = modelEntity.getPkFieldName() + " =?";
            sql = MessageFormat.format("UPDATE {0} SET \n  {1}\n  WHERE {2}", s0, s1, s2);

            List<ModelField> fields = new ArrayList<>();
            for (String f : allFieldNames) {
                fields.add(modelEntity.getField(f));
            }
            fields.add(modelEntity.getPkField());

            //获取实体类字段对象；
            allFieldNames.add(modelEntity.getPkFieldName());  //主键字段；
        }
        Object[] listArgs = new Object[list.size()];
        for (int i = 0, len = list.size(); i < len; i++) {
            value = list.get(i);
            value.setLastTime(UtilCursor.nowDateTimeNumber());
            listArgs[i] = EntityDaoHelper.getFieldValueArgs(allFieldNames, value);
        }
        DBHelper.updateBatchSameSql(sql, listArgs);
    }

    public void update(T value){
        SQLParam sqlParam = EntityDaoHelper.buildUpdateSql(value);
        if (sqlParam == null) return;
        DBHelper.update(sqlParam.sql, sqlParam.paras);
    }

    public void insertOrUpdate (T value, AbsEntityBuffer absEntityBuffer){
        String id = value.getEntityId();
        AbsEntity absEntity = absEntityBuffer.get(id);
        if(absEntity != null){
            update(value);
        }else{
            insert(value);
        }
        absEntityBuffer.add(value);
    }

    // 主键都要更新的
    public void updateWhere(T value, String where, Object... args){
        String entityName = value.getEntityName();
        if (entityName == null) {
            Log.e(TAG,"Entity Class:" + value.getClass() + "没有实现方法getEntityName");
        }
        ModelEntity modelEntity = getModelEntity();
        if (modelEntity == null) {
            Log.e(TAG,"Entity:" + entityName.toUpperCase() + "未找到实体定义！");
        }
        List<String> fieldNameList = new ArrayList<>();
        List<String> fieldnames = new ArrayList<>(modelEntity.getNoPkFieldNames());  //非主键字段；
        fieldnames.add(modelEntity.getPkFieldName());  //主键字段；

        String s1 = modelEntity.nameString(modelEntity.getNoPkFields(), "=?, ", "=?, ");
        String s2 = modelEntity.getPkFieldName() + " = ?"  ;
        String sql = MessageFormat.format("UPDATE {0} SET \n {1} {2} {3}", modelEntity.getEntityName(), s1, s2, where);

        //获取实体类字段对象；
        Object[] entity_values = EntityDaoHelper.getFieldValueArgs(fieldnames, value);
        for (Object arg : args) {
            entity_values[entity_values.length-1] = arg;
        }
        DBHelper.update(sql, entity_values);
    }


    public void updateWhereEx(String where, String fieldname , Object... args){
        String entityName = getEntityName();

        ModelEntity modelEntity = getModelEntity();
        if (modelEntity == null) {
            Log.e(TAG,"Entity:" + entityName.toUpperCase() + "未找到实体定义！");
        }
        String s1 = fieldname + " = ?";
        String sql = MessageFormat.format("UPDATE {0} SET  {1}  {2}", modelEntity.getEntityName(), s1, where);

        //获取实体类字段对象；
        List<String> fieldnames = new ArrayList<>(modelEntity.getNoPkFieldNames());  //非主键字段；
        Object[] entity_values = EntityDaoHelper.getFieldValueArgs(fieldnames, getNewBean());

        DBHelper.update(sql, args);
    }

    /**
     * 更新当前对象部分数据到数据库<BR><B>未包含事务</B></BR>；
     *
     * @param value     实体对象
     * @param fieldname 要更新的字段名称数组(不区分大小写)
     * @return int 更新条数；
     */
    public void update(T value, String... fieldname){
        if (fieldname.length == 0)  update(value);
        String entityName = value.getEntityName();
        if (entityName == null) {
            Log.e(TAG,"Entity Class:" + value.getClass() + "没有实现方法getEntityName");
        }
        ModelEntity entity = getModelEntity();
        if (entity == null) {
            Log.e(TAG,"Entity:" + entityName.toUpperCase() + "未找到实体定义！");
        }

        List<String> fieldNameList = new ArrayList<>();  //非主键字段；
        Collections.addAll(fieldNameList, fieldname); //把传递进来的字段转换成List;

        String s0 = entity.getEntityName();
        String s1 = EntityDaoHelper.nameString(fieldNameList, "=?, ", "=? ");
        String s2 = entity.getPkFieldName()+ "=? ";
        String sql = MessageFormat.format("UPDATE {0} SET \n  {1}\n  WHERE {2}", s0, s1, s2);

        //获取实体类字段对象；
        fieldNameList.add(entity.getPkFieldName());  //主键字段；
        Object[] args = EntityDaoHelper.getFieldValueArgs(fieldNameList, value);
        DBHelper.update(sql, args);
    }



    /**
     * 删除当前数据，以key为主；
     * 未包含事务；
     *
     * @param value 实体对象
     * @return int 删除条数；
     */
    public void delete(T value){
        String entityName = value.getEntityName();
        if (entityName == null) {
            return;
        }
        ModelEntity entity = getModelEntity();
        if (entity == null) {
            return;
        }
        String s0 = entity.getEntityName();
        String s1 = entity.getPkFieldName();
        String s2 = "?";
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = {2}", s0, s1, s2);

        List<String> list = new ArrayList<>();
        list.add(entity.getPkFieldName());
        Object[] args = EntityDaoHelper.getFieldValueArgs(list, value);
        Log.e(TAG,"==========sql + args  =======" + sql + "+++++++args.length:" + args.length + "ddddddddd+" + args[0]);
        DBHelper.update(sql, args);
    }

    public void deleteByKey(String key){
        String entityName = getEntityName();
        if (entityName == null) {
            return;
        }
        ModelEntity entity = getModelEntity();
        if (entity == null) {
            return;
        }
        final String pkFieldName = entity.getPkFieldName();
        DBHelper.update("delete from " + entityName + " where " + pkFieldName + "=?", key);
    }

    /**
     * 删除实体的所有数据
     */
    public void deleteAll(){
        deleteAll(null);
    }

    /**
     * 根据类型和条件，删除实体的数据
     * example: user = EntityDao.getInstance().deleteAll(UserEntity.class, " Where login_account=?", user_name_real);
     *
     * @param where SQL的where条件(需 where)
     * @param args  SQL的相关参数
     * @return T 如果SQL没有结果，会返回空
     */
    public void deleteAll(String where, Object... args){
        deleteAllEx("", where, args);
    }

    public void deleteAllEx(String tb_alias, String where, Object... args){
        final String entityName = getEntityName();
        ModelEntity entity = getModelEntity();
        StringBuilder sql = new StringBuilder(64);
        sql.append("DELETE from ").append(entity.getEntityName());
        if (StringUtils.isEmpty(tb_alias)) sql.append(" ").append(tb_alias);
        if (where != null) sql.append(" ").append(where);
        if (args != null && args.length > 0)
            DBHelper.update(sql.toString(), args);
        else
            DBHelper.update(sql.toString());
    }

    public boolean isExist(String id) throws Exception{
        if (StringUtils.isEmptyId(id)) return false;
        String entityName = getEntityName();
        ModelEntity entity = getModelEntity();
        String sql = "select 0 from " + entityName + " where " + entity.getPkFieldName() + "=?";
        return DBHelper.recordIsExists(sql, id);
    }

    /**
     * 根据当前对象的PK值，从数据库填充当前对象；
     *
     * @param value 实体对象
     */
    public T findMeByPK(T value) throws Exception{
        if (StringUtils.isEmptyId(value.getEntityId())) return value;
        //计算
        String entityName = value.getEntityName();
        if (entityName == null) {
            return value;
        }
        ModelEntity entity = getModelEntity();
        if (entity == null) {
            return value;
        }
        String sql = EntityDaoHelper.getSQL_select_from_wherePK(entityName);
        if (sql == null) return value;
        //获取主键值；
        List<String> list = new ArrayList<>();
        list.add(entity.getPkFieldName());
        Object[] args = EntityDaoHelper.getFieldValueArgs(list, value);
        //查询并获取数据，注意其中的对象要使用Bean对象；
        DBHelper.query(sql, new ResultSetExtractorOneRowImpl(entity, value), args);
        return value;
    }

    public T findOneByPK(String id) throws Exception{
        if (StringUtils.isEmptyId(id)) return null;
        String entityName = getEntityName();
        //计算
        ModelEntity entity = getModelEntity();
        if (entity == null) {
            return null;
        }
        String sql = EntityDaoHelper.getSQL_select_from_wherePK(entityName);
        if (sql == null) return null;
        T bean = getNewBean();
        DBHelper.query(sql, new ResultSetExtractorOneRowImpl(entity, bean), id);
        return bean;
    }

    /**
     * 根据类型和条件，找出一条对象
     * example: user = EntityDao.getInstance().findOne(UserEntity.class, " Where login_account=?", user_name_real);
     *
     * @param where SQL的where条件(需 where)
     * @param args  SQL的相关参数
     * @return T 如果SQL没有结果，会返回空
     */
    public T findOne(String where, Object... args) throws Exception{
        String entityName = getEntityName();
        ModelEntity entity = getModelEntity();
        if (entity == null) {
            return null;
        }

        StringBuilder sql = new StringBuilder(128);
        sql.append(EntityDaoHelper.getSQL_select_from(entityName));
        if (where != null) sql.append(' ').append(where);

        T bean = getNewBean();
        if (args != null && args.length > 0)
            DBHelper.query(sql.toString(), new ResultSetExtractorOneRowImpl(entity, bean), args);
        else
            DBHelper.query(sql.toString(), new ResultSetExtractorOneRowImpl(entity, bean));
        return bean;
    }

    public T findOneEx(String tb_alias, String where, Object... args) throws Exception{
        String entityName = getEntityName();
        ModelEntity entity = getModelEntity();
        if (entity == null) {
            return null;
        }

        StringBuilder sql = new StringBuilder(128);
        sql.append(EntityDaoHelper.getSQL_select_from(entityName, tb_alias));
        if (where != null) sql.append(' ').append(where);

        T bean = getNewBean();
        if (args != null && args.length > 0)
            DBHelper.query(sql.toString(), new ResultSetExtractorOneRowImpl(entity, bean), args);
        else
            DBHelper.query(sql.toString(), new ResultSetExtractorOneRowImpl(entity, bean));
        return bean;
    }

    /**
     * 从数据库加载所有数据，并返回一个List
     */
    public List<T> findAllList() throws Exception{
        return findAllListEx("", null);
    }

    /**
     * 从数据库加载所有数据，并返回一个List
     *
     * @param where SQL的where条件(需 where)
     * @param args  SQL的相关参数
     */
    public List<T> findAllList(String where, Object... args) throws Exception{
        return findAllListEx("", where, args);
    }

    //获取实体对象
    public List<T> findAllListEx(String tb_alias, String where, Object... args) throws Exception{
        final List<T> list = new ArrayList<>();
        findAll(tb_alias, where, new IBeanWorker<T>() {
            @Override
            public void doWork(T bean){
                list.add(bean);
            }
        }, args);
        return list;
    }

    /**
     * 自定义处理从数据库加载所有数据
     */
    public void findAll(final IBeanWorker<T> extractor) throws Exception{
        findAll("", (String) null, extractor);
    }

    /**
     * 自定义处理从数据库加载所有数据
     */
    public void findAll(String where, final IBeanWorker<T> extractor, Object... args) throws Exception{
        findAll("", where, extractor, args);
    }


    /**
     * 自定义处理从数据库加载所有数据
     *
     * @param tb_alias  表别名
     * @param where     追加where，可关联表追加where
     * @param extractor 解析器
     * @param args      sql参数
     */
    public void findAll(String tb_alias, String where, final IBeanWorker<T> extractor, Object... args) throws
            Exception{
        String entityName = getEntityName();
        final ModelEntity entity = getModelEntity();
        if (entity == null) {
            return;
        }
        StringBuilder sql = new StringBuilder(128);
        sql.append(EntityDaoHelper.getSQL_select_from(entityName, tb_alias));
        if (where != null) sql.append(' ').append(where);

        IDBCallback<Object> rse = new IDBCallback<Object>() {
            @Override
            public Object extractData(UtilCursor urs) throws Exception{

                while (urs.next()) {
                    try {
                        T newV = getNewBean();
                        readEntityFromRs(newV, urs);
                        extractor.doWork(newV);
                    } catch (Exception e) {
                        throw new SQLException(e);
//                        logger.error("处理每行实体出错：", e);
                    }
                }
                return null;
            }
        };
        if (args != null && args.length > 0) {
            DBHelper.query(sql.toString(), rse, args);
        } else {
            DBHelper.query(sql.toString(), rse);
        }
    }

    public List<T> findAllListFx(String[] fieldnames) throws Exception{
        return findAllListFxEx(fieldnames, "", null);
    }

    public List<T> findAllListFx(String[] fieldnames, String where, Object... args) throws Exception{
        return findAllListFxEx(fieldnames, "", where, args);
    }

    /**
     * 获取对象，但只填充对象特定字段；
     *
     * @param fieldnames 只读取及填充的字段
     * @param tb_alias   别名
     * @param where      附加where条件
     * @param args       sql 参数；
     * @return 只填充了部分字段的对象；
     */
    public List<T> findAllListFxEx(String[] fieldnames, String tb_alias, String where, Object... args) throws
            Exception{
        final List<T> list = new ArrayList<>();
        findAllFx(fieldnames, tb_alias, where, new IBeanWorker<T>() {
            @Override
            public void doWork(T bean){
                list.add(bean);
            }
        }, args);
        return list;
    }

    /**
     * 自定义处理从数据库加载所有数据
     *
     * @param fieldnames 只读取及填充的字段
     * @param tb_alias   表别名
     * @param where      追加where，可关联表追加where
     * @param extractor  解析器
     * @param args       sql参数
     */
    public void findAllFx(final String[] fieldnames, String tb_alias, String where, final IBeanWorker<T> extractor, Object... args) throws
            Exception{
        IDBCallback<Object> rse = new IDBCallback<Object>() {
            @Override
            public Object extractData(UtilCursor urs) throws Exception{
                while (urs.next()) {
                    try {
                        T newV = getNewBean();
                        readEntityFromRs(newV, urs, fieldnames);
                        extractor.doWork(newV);
                    } catch (Exception e) {
                        throw new SQLException(e);
//                        logger.error("处理每行实体出错：", e);
                    }
                }
                return null;
            }
        };
        StringBuilder sql = new StringBuilder(128);
        sql.append(EntityDaoHelper.getSQL_select_from(getEntityName(), fieldnames, tb_alias));
        if (where != null) sql.append(' ').append(where);
        if (args != null && args.length > 0) {
            DBHelper.query(sql.toString(), rse, args);
        } else {
            DBHelper.query(sql.toString(), rse);
        }
    }

    private class ResultSetExtractorOneRowImpl implements IDBCallback<T> {
        private T entity;
        private ModelEntity modelEntity;

        private ResultSetExtractorOneRowImpl(ModelEntity modelEntity, T entity) {
            this.modelEntity = modelEntity;
            this.entity = entity;
        }

        @Override
        public T extractData(UtilCursor rs){
            if (rs.next()) {
                readEntityFromRs(entity, rs);
            }
            return entity;
        }
    }
}
