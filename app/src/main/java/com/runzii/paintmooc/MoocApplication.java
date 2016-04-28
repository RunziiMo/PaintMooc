package com.runzii.paintmooc;

import android.app.Application;

import com.runzii.paintmooc.manage.AppManager;

public class MoocApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

       AppManager.init(this);

    }
}
