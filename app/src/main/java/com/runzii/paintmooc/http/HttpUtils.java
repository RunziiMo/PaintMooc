package com.runzii.paintmooc.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.TextHttpResponseHandler;
import com.runzii.paintmooc.model.UploadToken;
import com.runzii.paintmooc.utils.log.Log;

import cz.msebera.android.httpclient.Header;

/**
 * Created by runzii on 16-4-13.
 */
public class HttpUtils {

    private static AsyncHttpClient httpClient;
    private static ConnectivityManager connectivityManager;

    public static void init(Context context) {
        httpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
        httpClient.setCookieStore(cookieStore);
        httpClient.setEnableRedirects(true, true, true);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private static boolean check(Context context, HttpCallBack callBack) {
        if (httpClient == null) {
            init(context);
        }
        if (connectivityManager != null) {
            // 获取NetworkInfo对象
            boolean hasnet = false;
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            for (int i = 0; i < networkInfo.length; i++) {
                // 判断当前网络状态是否为连接状态
                if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                    hasnet = true;
                }
            }
            if (!hasnet) {
                callBack.onFailed("请检查网络连接");
                return true;
            }
        }
        return false;
    }

    public static void GET(ModelConstants modelConstants, Context context, final HttpCallBack callBack) {
        if (check(context, callBack)) {
            return;
        }
        switch (modelConstants) {
            case QINIUTOKEN:
                getToken(callBack);
                break;
        }
    }

    private static void getToken(final HttpCallBack callBack) {
        httpClient.get(APIs.generateUrl(APIs.UPLOADTOKEN), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("HttpUtils", "statisCode");
                callBack.onError(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.i("HttpUtils", responseString);
                callBack.onSuccess(responseString);
            }
        });
    }
}
