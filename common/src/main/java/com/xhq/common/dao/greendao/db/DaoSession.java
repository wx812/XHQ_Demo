package com.xhq.common.dao.greendao.db;

import com.xhq.common.bean.ImageInfoBean;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig imageInfoBeanDaoConfig;

    private final ImageInfoBeanDao imageInfoBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        imageInfoBeanDaoConfig = daoConfigMap.get(ImageInfoBeanDao.class).clone();
        imageInfoBeanDaoConfig.initIdentityScope(type);

        imageInfoBeanDao = new ImageInfoBeanDao(imageInfoBeanDaoConfig, this);

        registerDao(ImageInfoBean.class, imageInfoBeanDao);
    }
    
    public void clear() {
        imageInfoBeanDaoConfig.clearIdentityScope();
    }

    public ImageInfoBeanDao getImageInfoBeanDao() {
        return imageInfoBeanDao;
    }

}
