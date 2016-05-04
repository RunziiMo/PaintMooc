package com.runzii.paintmooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.runzii.paintmooc.http.HttpMethods;
import com.runzii.paintmooc.manage.AppPreferences;
import com.runzii.paintmooc.ui.LoginActivity;
import com.runzii.paintmooc.ui.base.ActivityBase;
import com.runzii.paintmooc.utils.UploadUtils;
import com.runzii.paintmooc.utils.Toasts;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;


public class MainActivity extends ActivityBase implements UpCancellationSignal, UpCompletionHandler, UpProgressHandler {

    @BindView(R.id.iv_main)
    ImageView iv;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.upload_progress)
    ProgressBar progressBar;
    @BindView(R.id.tv_videoname)
    TextView videoname;
    // A request code's purpose is to match the result of a "startActivityForResult" with
    // the type of the original request.  Choose any value.
    private static final int READ_REQUEST_CODE = 1337;

    public static final String TAG = "MainActivity";

    public static Uri uri = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (TextUtils.isEmpty(AppPreferences.getInstance().getAuth())) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @OnClick(R.id.fab)
    void onClick(View view) {
        if (uri == null) {
            Toasts.show("未选择文件");
            return;
        }
        HttpMethods.getInstance()
                .getUploadToken(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        AppPreferences.getInstance().clearAll();
                    }

                    @Override
                    public void onNext(String s) {
//                        Toasts.show(s);
                        uploadFile(s);
                    }
                }, AppPreferences.getInstance().getAuth());
    }

    public void uploadFile(String token) {
        UploadUtils.PUT(token, uri, this, this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            performFileSearch();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a file (as opposed to a list
        // of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers, it would be
        // "*/*".
        intent.setType("video/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code READ_REQUEST_CODE.
        // If the request code seen here doesn't match, it's the response to some other intent,
        // and the below code shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.  Pull that uri using "resultData.getData()"
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(TAG, "Uri: " + uri.toString());
                Glide.with(this).load(uri).into(iv);
                this.uri = uri;
            }
        }
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void complete(String key, ResponseInfo info, JSONObject response) {
        Log.d(TAG,info.toString());
    }

    @Override
    public void progress(String key, double percent) {
        progressBar.setProgress((int) (percent * 100));
    }

}
