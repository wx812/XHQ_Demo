package com.xhq.demo.db.db_greendao;

import com.xhq.demo.db.db_greendao.entry.UploadFile;

import org.greenrobot.greendao.AbstractDao;

public class UploadFileDbManager extends BaseManager<UploadFile,String> {
    @Override
    public AbstractDao<UploadFile,String> getAbstractDao() {
        return daoSession.getUploadFileDao();
    }
}

