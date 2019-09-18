package com.hik.dialyinterview.db;

import android.util.Log;

import com.hik.dialyinterview.bean.DialyBean;
import com.hik.dialyinterview.bean.KotlinBean;
import com.hik.dialyinterview.dao.DialyBeanDao;
import com.hik.dialyinterview.dao.KotlinBeanDao;

import java.util.List;

public class KotlinService extends BaseService<KotlinBean, String> {

    private static volatile KotlinService instance;//单例

    private KotlinService() {
    }

    public static KotlinService getInstance() {
        if (instance == null) {
            synchronized (KotlinService.class) {//保证异步处理安全操作
                if (instance == null) {
                    instance = new KotlinService();
                }
            }
        }
        return instance;
    }

    public KotlinBean queryByUrl (String url) {

        if (url == null) return null;
        List<KotlinBean> list = query(KotlinBeanDao.Properties.Url.eq(url));
        KotlinBean KotlinBean = null;
        if (list != null && list.size() > 0) {
            KotlinBean = list.get(0);
        }
        return KotlinBean;
    }
    public void insertOrUpdate(final KotlinBean KotlinBean) {
        if (getDao() == null || getDaoSession() == null) Log.d("aa", "aa");
        else {
            getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    try {
                        getDao().insert(KotlinBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                        getDao().delete(KotlinBean);
                        getDao().insert(KotlinBean);
                    }
                }
            });
        }
    }
}
