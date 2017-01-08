package com.korbkenny.peoplesplaylist.playlist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.korbkenny.peoplesplaylist.R;
import com.korbkenny.peoplesplaylist.UserSingleton;
import com.korbkenny.peoplesplaylist.objects.Song;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecordingActivity extends AppCompatActivity {
    public static final int RequestPermissionCode = 11335;
    private static final String TAG = "RECORDING";
    private String SONG_ID;
    private String PLAYLIST_ID;

    EditText mSongTitle;
    Button mRecord, mStopRecord, mPlay, mPause, mContinue, mSubmit;
    TextView mRedo;
    private String mAudioSavePath;
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mRecordPlayer;
    private Song mSongToUpload;

    private StorageReference mStorageRef;
    private DatabaseReference mSongListRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        PLAYLIST_ID = getIntent().getStringExtra("PlaylistId");

        mSongListRef = FirebaseDatabase.getInstance().getReference("SongLists").child(PLAYLIST_ID);
        mStorageRef = FirebaseStorage.getInstance().getReference("songs");

        SONG_ID = mSongListRef.child(PLAYLIST_ID).push().getKey();

        mSongTitle = (EditText) findViewById(R.id.record_edittext);
        mRecord = (Button) findViewById(R.id.record_button);
        mStopRecord = (Button) findViewById(R.id.stop_button);
        mPlay = (Button) findViewById(R.id.play_button);
        mPause = (Button) findViewById(R.id.pause_button);
        mContinue = (Button) findViewById(R.id.play_button_continue);
        mSubmit = (Button) findViewById(R.id.submit_button);
        mRedo = (TextView) findViewById(R.id.redo_button);

        //================
        //     RECORD
        //================
        mRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()){
                    mAudioSavePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/mysong.3gp";

                    MediaRecorderReady();

                    mRecordPlayer = new MediaPlayer();
                    try {
                        mMediaRecorder.prepare();
                        mMediaRecorder.start();
                        mRecord.setVisibility(View.GONE);
                        mStopRecord.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    requestPermission();
                }
            }
        });


        //================
        //     STOP
        //================
        mStopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                mMediaRecorder.stop();
                mMediaRecorder.release();
                mStopRecord.setVisibility(View.GONE);
                mPlay.setVisibility(View.VISIBLE);
                mSubmit.setEnabled(true);
                mRedo.setVisibility(View.VISIBLE);
            }
        });

        //================
        //     REDO
        //================
        mRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlay.setVisibility(View.GONE);
                mRedo.setVisibility(View.GONE);
                mRecord.setVisibility(View.VISIBLE);
                mSubmit.setEnabled(false);
            }
        });

        //================
        //  BEGIN PLAYING
        //================
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException{
                try{
                    mRecordPlayer.setDataSource(mAudioSavePath);
                    mRecordPlayer.prepare();
                } catch(IOException e){
                    e.printStackTrace();
                }
                mRecordPlayer.start();
                mPlay.setVisibility(View.GONE);
                mPause.setVisibility(View.VISIBLE);
            }
        });

        //================
        //     PAUSE
        //================
        mPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRecordPlayer.isPlaying()) {
                    mRecordPlayer.pause();
                    mPause.setVisibility(View.GONE);
                    mContinue.setVisibility(View.VISIBLE);
                }

            }
        });

        //=====================
        //   CONTINUE PLAYING
        //=====================
        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecordPlayer.start();
                mPause.setVisibility(View.VISIBLE);
                mContinue.setVisibility(View.GONE);
            }
        });

        //================
        //     SUBMIT
        //================
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Create the song to upload and set references (Ids) to
                //the playlist it's on, the user (you) who uploaded it,
                //and the Id of the song itself.
                mSongToUpload = new Song();
                mSongToUpload.setTitle(mSongTitle.getText().toString());
                mSongToUpload.setPlaylistId(PLAYLIST_ID);
                mSongToUpload.setUserId(UserSingleton.getInstance().getUser().getId());
                mSongToUpload.setSongId(SONG_ID);

                //Show the progress of the uploading file.
                final ProgressDialog progressDialog = new ProgressDialog(RecordingActivity.this);
                progressDialog.setTitle("Uploading!");
                progressDialog.show();

                //Get the file ready to upload.
                Uri audioFileUri = Uri.fromFile(new File(mAudioSavePath));
                StorageReference songRef = mStorageRef.child(PLAYLIST_ID).child(SONG_ID).child(audioFileUri.getLastPathSegment());

                //Upload the file to Firebase Storage.
                songRef.putFile(audioFileUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d(TAG, "onSuccess: ");
                                progressDialog.dismiss();

                                //The final piece of the Song object is to add the
                                //link to the song file itself on Firebase Storage.
                                mSongToUpload.setStreamUrl(taskSnapshot.getDownloadUrl().toString());

                                //Then upload the Song Object to the database.
                                mSongListRef.child(SONG_ID).setValue(mSongToUpload);
                                Toast.makeText(getApplicationContext(), "Finished!", Toast.LENGTH_SHORT).show();
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("result",SONG_ID);
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred())
                                        /taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage((int)progress+"% uploaded...");
                            }
                        });


            }
        });

    }

    public void MediaRecorderReady(){
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mMediaRecorder.setOutputFile(mAudioSavePath);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(RecordingActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(RecordingActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RecordingActivity.this,"Permission Denied",
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

}
