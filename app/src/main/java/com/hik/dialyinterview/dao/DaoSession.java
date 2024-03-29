package com.hik.dialyinterview.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.hik.dialyinterview.bean.DetailBean;
import com.hik.dialyinterview.bean.DialyBean;
import com.hik.dialyinterview.bean.KotlinBean;

import com.hik.dialyinterview.dao.DetailBeanDao;
import com.hik.dialyinterview.dao.DialyBeanDao;
import com.hik.dialyinterview.dao.KotlinBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig detailBeanDaoConfig;
    private final DaoConfig dialyBeanDaoConfig;
    private final DaoConfig kotlinBeanDaoConfig;

    private final DetailBeanDao detailBeanDao;
    private final DialyBeanDao dialyBeanDao;
    private final KotlinBeanDao kotlinBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        detailBeanDaoConfig = daoConfigMap.get(DetailBeanDao.class).clone();
        detailBeanDaoConfig.initIdentityScope(type);

        dialyBeanDaoConfig = daoConfigMap.get(DialyBeanDao.class).clone();
        dialyBeanDaoConfig.initIdentityScope(type);

        kotlinBeanDaoConfig = daoConfigMap.get(KotlinBeanDao.class).clone();
        kotlinBeanDaoConfig.initIdentityScope(type);

        detailBeanDao = new DetailBeanDao(detailBeanDaoConfig, this);
        dialyBeanDao = new DialyBeanDao(dialyBeanDaoConfig, this);
        kotlinBeanDao = new KotlinBeanDao(kotlinBeanDaoConfig, this);

        registerDao(DetailBean.class, detailBeanDao);
        registerDao(DialyBean.class, dialyBeanDao);
        registerDao(KotlinBean.class, kotlinBeanDao);
    }
    
    public void clear() {
        detailBeanDaoConfig.clearIdentityScope();
        dialyBeanDaoConfig.clearIdentityScope();
        kotlinBeanDaoConfig.clearIdentityScope();
    }

    public DetailBeanDao getDetailBeanDao() {
        return detailBeanDao;
    }

    public DialyBeanDao getDialyBeanDao() {
        return dialyBeanDao;
    }

    public KotlinBeanDao getKotlinBeanDao() {
        return kotlinBeanDao;
    }

}
