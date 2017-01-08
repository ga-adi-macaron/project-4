package com.colinbradley.xboxoneutilitiesapp;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by colinbradley on 1/4/17.
 */

public class DownloaderActivity extends AppCompatActivity{
    public static final String TAG = "DownloaderActivity";
    DownloadManager mDownloadManager;
    BroadcastReceiver mReciever;
    long enqueue;
    String mBaseURL;
    String mFinalURL;
    String mFileName = "testinggg.jpg";
    String dirName = "Download";


    ProgressBar mProgressBar;
    Button mButton;
    Button mTest;
    ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader);
        Log.d(TAG, "onCreate: got to downloader activity");

        mProgressBar = (ProgressBar)findViewById(R.id.dl_progressbar);
        mTest = (Button)findViewById(R.id.dl_back);
        mButton = (Button)findViewById(R.id.show_dl);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDownload();
            }
        });

        mDownloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);

        Intent intent = getIntent();
        mFileName = intent.getStringExtra("title");


        if (intent.hasExtra("imgURL")) {
            mFileName = mFileName + ".jpg";
            mBaseURL = intent.getStringExtra("imgURL");
            int index = mBaseURL.indexOf("?");
            mFinalURL = mBaseURL.substring(0, index);

            Log.d(TAG, "onCreate: recieved img URL --- " + mBaseURL);
            Log.d(TAG, "onCreate: edited url --- " + mFinalURL);
            Log.d(TAG, "onCreate: ");


        } else if (intent.hasExtra("clipURL")){
            mFileName = mFileName + ".mp4";
            mBaseURL = intent.getStringExtra("clipURL");
            mFinalURL = mBaseURL;
            Log.d(TAG, "onCreate: videoURL");
        } else {
            Toast.makeText(this, "Error while locating downloadable object", Toast.LENGTH_SHORT).show();
        }






        mReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor cursor = mDownloadManager.query(query);
                    if (cursor.moveToFirst()){
                        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                            Log.d(TAG, "onReceive: successfull");

                            mProgressBar.setVisibility(View.INVISIBLE);

                            if (mFileName.endsWith(".jpg")) {
                                Toast.makeText(context, "Image Download Complete!", Toast.LENGTH_SHORT).show();
                            }else if (mFileName.endsWith(".mp4")){
                                Toast.makeText(context, "Video Download Complete!", Toast.LENGTH_SHORT).show();
                            }
                        }else if (DownloadManager.STATUS_FAILED == cursor.getInt(columnIndex)){
                            Log.d(TAG, "onReceive: error");

                            mProgressBar.setVisibility(View.INVISIBLE);
                            if (mFileName.endsWith(".jpg")) {
                                Toast.makeText(context, "Error Downloading Image", Toast.LENGTH_SHORT).show();
                            }else if (mFileName.endsWith(".mp4")){
                                Toast.makeText(context, "Error Downloading Video", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        };

        registerReceiver(mReciever, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        Log.d(TAG, "onCreate: registerReciever");

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mFinalURL));
        request.setTitle(mFileName)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PICTURES, mFileName);
        Log.d(TAG, "onCreate: create request");
        enqueue = mDownloadManager.enqueue(request);
        Log.d(TAG, "onCreate: enqueue request");

        mTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void showDownload(){
        Log.d(TAG, "showDownload: ");
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);

        startActivity(i);
    }

    public void addImageToGallery(String filepath, Context context){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filepath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReciever);

    }

    //--------------------------------------------------------------------------------
    public long DownloadData(Uri uri, View view, String title){
        long downloadReference;
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(title);

        if (view.getId() == R.id.ss_download){
            request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "screenshot.jpg");
        }

        downloadReference = mDownloadManager.enqueue(request);
        return downloadReference;
    }


    public void receiverForDownload(Uri uri, View view, String title) {
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        registerReceiver(downloadReceiver,filter);
    }
    //--------------------------------------------------------------------
    // ^^^^ Don't Use ^^^^
    //--------------------------------------------------------------------


}
