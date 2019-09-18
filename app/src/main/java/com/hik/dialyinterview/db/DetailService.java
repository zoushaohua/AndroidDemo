package com.hik.dialyinterview.db;

import com.hik.dialyinterview.bean.DetailBean;
import com.hik.dialyinterview.dao.DetailBeanDao;

import java.util.List;

public class DetailService extends BaseService<DetailBean, String> {

    private static volatile DetailService instance;//单例

    private DetailService() {
    }

    public static DetailService getInstance() {
        if (instance == null) {
            synchronized (DetailService.class) {//保证异步处理安全操作
                if (instance == null) {
                    instance = new DetailService();
                }
            }
        }
        return instance;
    }

    public DetailBean queryByText(String url,String text) {

        if (text == null) return null;
        List<DetailBean> list = query(DetailBeanDao.Properties.Url.eq(url),
                DetailBeanDao.Properties.Content.eq(text));
        DetailBean detailBean = null;
        if (list != null && list.size() > 0) {
            detailBean = list.get(0);
        }
        return detailBean;
    }

    public List<DetailBean> queryByUrl(String url) {

        if (url == null) return null;
        List<DetailBean> list = query(DetailBeanDao.Properties.Url.eq(url));
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }
}
