package com.runzii.paintmooc.http;

import com.runzii.paintmooc.manage.AppSettings;

/**
 * Created by runzii on 16-4-13.
 */
public class APIs {


    public static final String UPLOADTOKEN = "uploadtoken";

    public static String generateUrl(String api){
        return baseUrl+api;
    }

    public static final String baseUrl = "http://" + AppSettings.SERVER_IP + ":" + AppSettings.SERVER_PORT + "/";
}
