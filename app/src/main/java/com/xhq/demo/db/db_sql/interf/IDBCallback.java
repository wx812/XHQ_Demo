package com.xhq.demo.db.db_sql.interf;

import com.xhq.demo.db.db_sql.UtilCursor;

public interface IDBCallback<T>  {
    T extractData(UtilCursor uc) throws Exception;
}


