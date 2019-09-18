package com.hik.dialyinterview.db;

import android.content.Context;


import com.hik.dialyinterview.dao.DaoMaster;
import com.hik.dialyinterview.dao.DaoSession;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

public class DBHelper {

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static volatile DBHelper instance;//单例

    private DBHelper() {
    }

    public static DBHelper getInstance() {
        if (instance == null) {
            synchronized (DBHelper.class) {//保证异步处理安全操作
                if (instance == null) {
                    instance = new DBHelper();
                }
            }
        }
        return instance;
    }

    public void init (Context context) {
        if (instance != null) {
            //此处devOpenHelper为自动生成开发所使用，发布版本需自定义
            DatabaseOpenHelper databaseOpenHelper = new DaoMaster.OpenHelper(context.getApplicationContext(), "Dialy") {
                @Override
                public void onCreate(Database db) {
                    super.onCreate(db);
                }

                @Override
                public void onUpgrade(Database db, int oldVersion, int newVersion) {

                }
            };

            mDaoMaster = new DaoMaster(databaseOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession(IdentityScopeType.None);

            DialyService.getInstance().init(mDaoSession, mDaoSession.getDialyBeanDao());
            DetailService.getInstance().init(mDaoSession, mDaoSession.getDetailBeanDao());
        }
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }


}
