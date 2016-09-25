package com.runzii.paintmooc.http;

import android.util.Base64;

import com.runzii.paintmooc.manage.AppSettings;
import com.runzii.paintmooc.model.AuthToken;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by runzii on 16-5-1.
 */
public class HttpMethods {

    private ApiService apiService;

    public static final String BASE_URL = "http://" + AppSettings.SERVER_IP + ":" + AppSettings.SERVER_PORT + "/";

    public static final String userAndPassword = "paintmooc:secret";
    public static final String base_authorization = "Basic " + Base64.encodeToString(userAndPassword.getBytes(), Base64.NO_WRAP);

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    /**
     * 获取基本认证的token,用于一些请求
     *  @param subscriber 由调用者传过来的观察者对象
     * @param phone
     * @param password
     */
    public Observable<AuthToken> getAuthToken(String phone, String password) {
        return apiService.getAuthToken(base_authorization, phone, password, "password")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void getUploadToken(Subscriber<String> subscriber, String accessToken) {
        apiService
                .getUploadToken(accessToken)
                .map(new Func1<ResponseBody, String>(){
                    @Override
                    public String call(ResponseBody responseBody) {
                        String body = null;
                        try {
                            body = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return body;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }


}
