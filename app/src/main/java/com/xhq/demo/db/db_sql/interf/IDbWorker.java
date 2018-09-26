package com.xhq.demo.db.db_sql.interf;

/**
 * 数据库事务操作方法类
 */
public interface IDbWorker {
    void work() throws Exception;

    //数据库提交完后的业务或缓存处理
    void doAfterDbCommit() throws Exception;

    //数据库回滚后的业务或缓存处理
    void doAfterDbRollback() throws Exception;
}
