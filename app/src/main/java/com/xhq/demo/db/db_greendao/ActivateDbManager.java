package com.xhq.demo.db.db_greendao;


import com.xhq.demo.db.db_greendao.entry.Activate;

import org.greenrobot.greendao.AbstractDao;

public class ActivateDbManager extends BaseManager<Activate,String> {
    @Override
    public AbstractDao<Activate,String> getAbstractDao() {
        return daoSession.getActivateDao();
    }
}

