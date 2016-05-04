package com.runzii.paintmooc.manage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by runzii on 16-5-1.
 */
public class AppPreferences {

    private static final String AUTH = "auth_paintmooc_self";

    private static AppPreferences instance;

    private SharedPreferences sh;

    private AppPreferences() {
    }

    private AppPreferences(Context ctx) {
        sh = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static AppPreferences getInstance() {
        if (instance == null) {
            instance = new AppPreferences(AppManager.getAppContext());
        }
        return instance;
    }

    public String getAuth() {
        return sh.getString(AUTH, "");
    }

    public boolean setAuth(String auth) {
        return sh.edit().putString(AUTH, auth).commit();
    }


    public boolean clearAll() {
        return sh.edit().clear().commit();
    }

}
