package com.runzii.paintmooc.http;

import com.runzii.paintmooc.model.AuthToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by runzii on 16-4-13.
 */
public interface ApiService {

    @GET("oauth/token")
    Observable<AuthToken> getAuthToken(@Header("Authorization") String authorization, @Query("username") String phone, @Query("password") String password, @Query("grant_type") String grant_type);

    @GET("courses/gettoken")
    Observable<ResponseBody> getUploadToken(@Header("Authorization") String authorization);

}
