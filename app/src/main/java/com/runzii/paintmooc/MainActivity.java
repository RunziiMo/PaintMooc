package com.runzii.paintmooc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.utils.StringMap;
import com.runzii.paintmooc.http.HttpCallBack;
import com.runzii.paintmooc.http.HttpUtils;
import com.runzii.paintmooc.http.ModelConstants;
import com.runzii.paintmooc.model.UploadToken;
import com.runzii.paintmooc.ui.painter.fragments.base.AvtivityBase;
import com.runzii.paintmooc.utils.QiNiu;
import com.runzii.paintmooc.utils.log.Log;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.IOException;

import butterknife.Bind;


public class MainActivity extends AvtivityBase implements UpCancellationSignal, UpCompletionHandler, UpProgressHandler {

    @Bind(R.id.iv_main)
    ImageView iv;

    // A request code's purpose is to match the result of a "startActivityForResult" with
    // the type of the original request.  Choose any value.
    private static final int READ_REQUEST_CODE = 1337;

    public static final String TAG = "StorageClientFragment";

    public static Uri uri = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        uploadDialog =
                new MaterialDialog.Builder(this).
                        title("正在上传").
                        content("上传进度").
                        progress(false, 100, showMinMax).
                        build();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                HttpUtils.GET(ModelConstants.QINIUTOKEN, MainActivity.this, new HttpCallBack<String>() {
                    @Override
                    public void onSuccess(String uploadToken) {
                        if (uri != null) {
                            Toast.makeText(MainActivity.this, "token = " + uploadToken, Toast.LENGTH_SHORT).show();
                            QiNiu.PUT(uploadToken, uri, MainActivity.this, MainActivity.this, MainActivity.this);
                        }
                    }

                    @Override
                    public void onFailed(String reason) {

                    }

                    @Override
                    public void onError(int statusCode) {
                        Toast.makeText(MainActivity.this, String.valueOf(statusCode), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        Log.i(TAG, "Received an \"Activity Result\"");
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
                Picasso.with(this).load(uri).into(iv);
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

        uploadDialog.setContent(response.toString());
        Toast.makeText(this, "上传完成", Toast.LENGTH_LONG).show();
    }

    // Create and show a non-indeterminate dialog with a max value of 150
// If the showMinMax parameter is true, a min/max ratio will be shown to the left of the seek bar.
    boolean showMinMax = true;

    private MaterialDialog uploadDialog;

    @Override
    public void progress(String key, double percent) {

        if (!uploadDialog.isShowing()) {
            uploadDialog.show();
        }

        uploadDialog.setProgress((int) (100 * percent));

    }


}
