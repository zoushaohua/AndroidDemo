package com.hik.dialyinterview.db;

import android.util.Log;

import com.hik.dialyinterview.bean.DialyBean;
import com.hik.dialyinterview.dao.DialyBeanDao;

import java.util.List;

public class DialyService extends BaseService<DialyBean, String> {

    private static volatile DialyService instance;//单例

    private DialyService() {
    }

    public static DialyService getInstance() {
        if (instance == null) {
            synchronized (DialyService.class) {//保证异步处理安全操作
                if (instance == null) {
                    instance = new DialyService();
                }
            }
        }
        return instance;
    }

    public DialyBean queryByUrl (String url) {

        if (url == null) return null;
        List<DialyBean> list = query(DialyBeanDao.Properties.Url.eq(url));
        DialyBean dialyBean = null;
        if (list != null && list.size() > 0) {
            dialyBean = list.get(0);
        }
        return dialyBean;
    }

    public void insertOrUpdate(final DialyBean dialyBean) {
        if (getDao() == null || getDaoSession() == null) Log.d("aa", "aa");
        else {
            getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    try {
                        getDao().insert(dialyBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                        getDao().delete(dialyBean);
                        getDao().insert(dialyBean);
                    }
                }
            });
        }
    }
}
