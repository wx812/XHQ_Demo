package com.xhq.demo.cmd.buffer;

import com.xhq.demo.db.db_sql.entityConfig.AbsEntity;
import com.xhq.demo.db.db_sql.entityConfig.AbsEntityDao;
import com.xhq.demo.db.db_sql.interf.IBeanWorker;
import com.xhq.demo.tools.choppedTools.UtilPub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by akmm on 2017/4/12.
 * 缓存
 */

public abstract class AbsEntityBuffer<E extends AbsEntity> {
    protected Map<String, E> mapAll = new HashMap<>();

    //读写锁，同步用
    protected final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    /**
     * 读锁
     */
    protected void readLock() {
        rwLock.readLock().lock();
    }

    /**
     * 读解锁
     */
    protected void readUnlock() {
        rwLock.readLock().unlock();
    }

    /**
     * 写锁
     */
    protected void writeLock() {
        rwLock.writeLock().lock();
    }

    /**
     * 写解锁
     */
    protected void writeUnlock() {
        rwLock.writeLock().unlock();
    }

    /**
     * 得到缓存对象的类型
     */
    protected abstract AbsEntityDao<E> getEntityDao();

    //查全部时，额外添加的sql
    protected String getSqlAddtion() {
        return null;
    }

    /**
     * 本地刷新，一般从数据库加载
     *
     * @param key 刷新指定key
     * @return T
     * @throws Exception
     */
    protected E localReload(String key) throws Exception{
        final AbsEntityDao<E> dao = getEntityDao();
        final E newV = dao.findOneByPK(key);
        if (newV == null || newV.isEmpty()) return null;
        add(newV);

        return newV;
    }

    /**
     * 本地刷新，所有数据；
     *
     * @throws Exception
     */
    protected void localReload() throws Exception{
                 clear();
        final AbsEntityDao<E> dao = getEntityDao();
        dao.findAll(getSqlAddtion(), new IBeanWorker<E>() {
            @Override
            public void doWork(E entity) throws Exception{
                try {
                    add(entity);
//                    put(entity.getEntityId(), entity);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    /**
     * @param key   被更新的缓存key
     * @param value 新的对象；
     */
    protected void doUpdate(String key, E value) {
        doRemove(key);
    }

    /**
     * 缓存移除时触发
     *
     * @param key   被移除的key
     */
    protected void doRemove(String key) {
        mapAll.remove(key);
    }

    //清除所有缓存；
    protected void doRemoveAll() {
    }

    public void init() {//初始化数据
        try {
            refresh();
        } catch (Exception e) {
//            LogUtil.e("缓存初始化出错", e);
        }
    }

    protected void innerPut(final String key, final E obj) {
        doUpdate(key, obj);
        mapAll.put(key, obj);
    }

    protected void innerRemove(final String key) throws Exception{
        doRemove(key);
        mapAll.remove(key);
    }

    //添加对象到缓存
    public void add(E bean){
        put(bean.getEntityId(), bean);
    }

    //按key插入
    public void put(String key, E obj) {
        writeLock();
        try {
            innerPut(key, obj);
        } finally {
            writeUnlock();
        }
    }

    //只从缓存读取，
    public E get(String key) {
        if (UtilPub.isEmpty(key)) return null;
        readLock();
        try {
            return mapAll.get(key);
        }  finally {
            readUnlock();
        }
    }

    /*//先从缓存读取，若没有，自动从数据库刷新
    public E get(String key) {
        if (UtilPub.isEmptyId(key)) return null;
        readLock();
        try {
            E o = mapAll.get(key);
            if (o != null) return o;
        } finally {
            readUnlock();
        }

        writeLock();
        try {
            E o = mapAll.get(key);;
            if (o != null) return o;
            o = localReload(key);
            return o;
        } catch (Exception e) {
//            UtilLogger.error("获取缓存出错(" + regionKey + ")", e);
            return null;
        } finally {
            writeUnlock();
        }
    }*/

    //删除缓存
    public void remove(String key) {
        writeLock();
        try {
            innerRemove(key);
        } catch (Exception e) {
//            UtilLogger.error("删除缓存出错(" + getRegionKey() + ")", e);
        } finally {
            writeUnlock();
        }
    }

    //清空缓存
    public void clear() {
        doRemoveAll();
        mapAll.clear();
    }

    //刷新缓存
    public void refresh() throws Exception{
        clear();
        localReload();
    }

    //刷新单个对象
    public void refresh(String key) throws Exception{
        remove(key);
        localReload(key);
    }

    //得到缓存的所有key列表
    public Set<String> getKeySet() {
        return mapAll.keySet();
    }

    /**
     * 得到缓存对象列表，必须开启isNeedKeySet
     *
     * @param c 对象比较，如果不排序，则传入null;
     * @return List
     */
    public List<E> getCacheList(Comparator<E> c) {
        List<E> list = new ArrayList<E>();
        readLock();
        try {
            list.addAll(mapAll.values());
            if (c != null) Collections.sort(list, c);
        } catch (Exception e) {
//            UtilLogger.error("获取缓存列表失败", e);
        } finally {
            readUnlock();
        }
        return list;
    }

    public boolean isEmpty() {
        return mapAll.isEmpty();
    }
}
