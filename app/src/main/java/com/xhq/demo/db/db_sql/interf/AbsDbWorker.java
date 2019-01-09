package com.xhq.demo.db.db_sql.interf;

/**
 * 数据库事务操作方法类
 */
public abstract class AbsDbWorker implements IDbWorker {
    @Override
    public void doAfterDbCommit(){}

    @Override
    public void doAfterDbRollback(){}
}
