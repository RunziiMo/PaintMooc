package com.runzii.paintmooc.utils;

import android.net.Uri;
import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.utils.StringMap;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by runzii on 16-4-13.
 */
public class QiNiu implements UpCancellationSignal, UpProgressHandler {

    private static UploadManager uploadManager;

    private static UploadOptions options;

    {
        Map<String, String> params = new HashMap<>();
        params.put("x:cid", "1");
        params.put("x:videoname", "操逼课之如何插入");
        options = new UploadOptions(params, null, false, this, this);
    }

    public static void PUT(String token, Uri uri, UpCompletionHandler completionHandler, UpCancellationSignal signal, UpProgressHandler progressHandler) {
        if (uploadManager == null) {
            uploadManager = new UploadManager();
        }
        uploadManager.put(new File(uri.getPath()), "hello", token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + response);
                    }
                }, null);
    }


    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void progress(String key, double percent) {

    }
}
