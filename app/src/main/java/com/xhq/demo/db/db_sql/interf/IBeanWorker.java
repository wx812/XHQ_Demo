package com.xhq.demo.db.db_sql.interf;

//Bean Work接口；
public interface IBeanWorker<T> {
    void doWork(T bean) throws Exception;
}