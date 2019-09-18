package com.hik.dialyinterview.db;

import android.util.Log;

import com.hik.dialyinterview.dao.DaoSession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;
import org.greenrobot.greendao.rx.RxDao;

import java.util.List;

public abstract class BaseService<T, K> {

    public static final String TAG = "BaseService";
    public static final String CHECK_INIT = "Please check for the initialization";

    private DaoSession daoSession;
    private AbstractDao<T, K> dao;

    public void init(DaoSession daoSession, AbstractDao<T, K> dao) {
        this.daoSession = daoSession;
        this.dao = dao;
    }

    public synchronized void insert(T item) {
        if (dao == null) {
        } else {
            dao.insert(item);
        }
    }

    public synchronized void insert(T... items) {
        if (dao == null) {
        } else {
            dao.insertInTx(items);
        }
    }

    public synchronized void insert(List<T> items) {
        if (dao == null) {
        } else {
            dao.insertInTx(items);
        }
    }

    public synchronized void insertOrReplace(T item) {
        if (dao == null) {
        } else {
            dao.insertOrReplace(item);
        }
    }

    public synchronized void insertOrReplace(T... items) {
        if (dao == null) {
        } else {
            dao.insertOrReplaceInTx(items);
        }
    }

    public synchronized void insertOrReplace(List<T> items) {
        if (dao == null) {
        } else {
            dao.insertOrReplaceInTx(items);
        }
    }

    public synchronized void insertOrIgnore(final Iterable<T> entities) {
        if (dao == null || daoSession == null) {
        } else {
            daoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T entity : entities) {
                        try {
                            dao.insert(entity);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }


    public synchronized void insertOrIgnore(final T... entities) {
        if (dao == null || daoSession == null) Log.d(TAG,  "");
        else {
            daoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T entity : entities) {
                        try {
                            dao.insert(entity);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public synchronized void deleteByKey(K key) {
        dao.deleteByKey(key);
    }

    public synchronized void delete(T item) {
        if (item == null) return;
        else dao.delete(item);
    }

    public synchronized void delete(T... items) {
        if (items == null) return;
        else dao.deleteInTx(items);
    }

    public synchronized void delete(List<T> items) {
        if (items == null) return;
        else dao.deleteInTx(items);
    }

    public synchronized void deleteAll() {
      deleteAll();
    }

    public synchronized void update(T item) {
         dao.update(item);
    }

    public synchronized void update(T... items) {
         dao.updateInTx(items);
    }

    public synchronized void update(List<T> items) {
         dao.updateInTx(items);
    }

    public T load(K key) {
        if (dao == null) {
            return null;
        }
        return dao.load(key);
    }

    public List<T> loadAll() {
        if (dao == null) {
            return null;
        }
        return dao.loadAll();
    }

    public List<T> query(WhereCondition cond1, WhereCondition... condMore) {
        if (dao == null) {
            return null;
        }
        return dao.queryBuilder().where(cond1, condMore).build().list();
    }

    public List<T> queryRaw(String where, String... params) {
        if (dao == null) {
            return null;
        }
        return dao.queryRaw(where, params);
    }

    public QueryBuilder<T> queryBuilder() {
        if (dao == null) {
            return null;
        }
        return dao.queryBuilder();
    }

    public long count() {
        if (dao == null) {
            return 0;
        }
        return dao.count();
    }

    public void refresh(T item) {
         dao.refresh(item);
    }

    public void detach(T item) {
        dao.detach(item);
    }

    public AbstractDao<T, K> getDao() {
        return dao;
    }

    public RxDao<T, K> getRxDao() {
        if (dao == null) {
            return null;
        }
        return dao.rx();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public QueryBuilder<T> getQueryBuilder() {
        if (dao == null) {
            return null;
        }
        return dao.queryBuilder();
    }



}
