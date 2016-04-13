package com.runzii.paintmooc.utils;

import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

/**
 * Created by runzii on 16-4-13.
 */
public class QiNiu {

    private static UploadManager uploadManager;

    public static void PUT(String token){
        if (uploadManager==null){
            uploadManager = new UploadManager();
        }
        uploadManager.put("Hello, World!".getBytes(), "hello", token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        Log.i("qiniu", info.error);
                    }
                }, null);
    }

}
