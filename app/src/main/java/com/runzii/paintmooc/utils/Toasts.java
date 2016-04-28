package com.runzii.paintmooc.utils;

import android.widget.Toast;

import com.runzii.paintmooc.manage.AppManager;

/**
 * Toast类，无需多说
 * Created by Wouldyou on 2015/5/29.
 */
public class Toasts {

    public static void show(String msg) {
        Toast.makeText(AppManager.getAppContext(), msg, Toast.LENGTH_LONG).show();
    }
}
