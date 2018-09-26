package com.xhq.demo.db.db_greendao;

import com.xhq.demo.db.db_greendao.entry.RealVerify;

import org.greenrobot.greendao.AbstractDao;

public class RealVerifyDbManager extends BaseManager<RealVerify,String> {
    @Override
    public AbstractDao<RealVerify,String> getAbstractDao() {
        return daoSession.getRealVerifyDao();
    }
}

