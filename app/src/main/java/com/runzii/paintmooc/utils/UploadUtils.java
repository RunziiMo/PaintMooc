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
public class UploadUtils {

    private static UploadManager uploadManager;

    public static void PUT(String token, Uri uri, UpCompletionHandler completionHandler, UpCancellationSignal signal, UpProgressHandler progressHandler) {
        if (uploadManager == null) {
            uploadManager = new UploadManager();
        }
        Map<String, String> params = new HashMap<>();
        params.put("x:cid", "2");
        params.put("x:videoname", "操逼课之如何插入");
        UploadOptions options = new UploadOptions(params, null, false, progressHandler, signal);
        uploadManager.put(FileUtils.getRealPathFromURI(uri), null, token,
                completionHandler, options);
    }

}
