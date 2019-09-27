package com.hik.dialyinterview;

import android.app.Application;

import com.hik.dialyinterview.db.DBHelper;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;


public class DialyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        DBHelper.getInstance().init(this);
        BGASwipeBackHelper.init(this, null);
    }
}
