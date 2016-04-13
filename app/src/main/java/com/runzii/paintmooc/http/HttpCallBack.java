package com.runzii.paintmooc.http;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public interface HttpCallBack<T> {

    //操作成功，达到预期目标
    void onSuccess(T t);

    //操作失败
    void onFailed(String reason);

    //网络连接错误
    void onError(int statusCode);
}
