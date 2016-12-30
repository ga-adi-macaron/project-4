package com.korbkenny.peoplesplaylist.playlist;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.korbkenny.peoplesplaylist.R;
import com.korbkenny.peoplesplaylist.UserSingleton;
import com.korbkenny.peoplesplaylist.objects.Playlist;
import com.korbkenny.peoplesplaylist.objects.Song;
import com.korbkenny.peoplesplaylist.objects.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PlaylistActivity extends AppCompatActivity {
    public static final int RequestPermissionCode = 11335;


    private RecyclerView mRecyclerView;
    private PlaylistRecyclerAdapter mAdapter;
    private String PLAYLIST_ID, SONG_ID;
    private FirebaseDatabase db;
    private DatabaseReference mSongListRef;
    private Playlist mThisPlaylist;
    private CardView mPlayPause, mBack, mSkip;
    private StorageReference mStorageRef;
    private Uri downloadUrl;
    private TextView mTitle,mDescription;
    private ImageView mCover;
    private FloatingActionButton fabAddSong;
    private MediaPlayer mMediaPlayer;
    private Song mSongToUpload;
    private List<Song> mSongList;
    private User ME;
    private String mAudioSavePath;
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mRecordPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        PLAYLIST_ID = getIntent().getStringExtra("Playlist Id");
        db = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("songs");
        ME = UserSingleton.getInstance().getUser();

        mSongList = new ArrayList<>();
        Song song = new Song();
        song.setTitle("HEllo there");
        song.setUserId(ME.getId());
        mSongList.add(song);

        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                syncToDatabase();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mAdapter.notifyDataSetChanged();
            }
        }.execute();

        //  Choose a song to add to the playlist
        fabAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSongToUpload == null) {
                    mSongToUpload = new Song();
                }
                onRecordTrackDialog().show();
            }
        });
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //     Record Song Dialog. Uploads to Firebase Storage
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Dialog onRecordTrackDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.record_song_dialog,null);
        builder.setView(view);

        SONG_ID = mSongListRef.push().getKey();

        final EditText songTitle = (EditText) view.findViewById(R.id.record_edittext);
        final Button recordButton = (Button) view.findViewById(R.id.record_button);
        final Button stopButton = (Button) view.findViewById(R.id.stop_button);
        final Button playButton = (Button) view.findViewById(R.id.play_button);
        final Button pauseButton = (Button) view.findViewById(R.id.pause_button);
        final Button submit = (Button) view.findViewById(R.id.submit_button);
        final Button continuePlaying = (Button) view.findViewById(R.id.play_button_continue);
        final TextView redo = (TextView) view.findViewById(R.id.redo_button);

        //================
        //     RECORD
        //================
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()){
                    mAudioSavePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/Song" + SONG_ID + ".3gp";
                    MediaRecorderReady();
                    mRecordPlayer = new MediaPlayer();
                    try {
                        mMediaRecorder.prepare();
                        mMediaRecorder.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    recordButton.setVisibility(View.GONE);
                    stopButton.setVisibility(View.VISIBLE);
                } else {
                    requestPermission();
                }
            }
        });

        //================
        //     STOP
        //================
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaRecorder.stop();
                stopButton.setVisibility(View.GONE);
                playButton.setVisibility(View.VISIBLE);
                submit.setEnabled(true);
                redo.setVisibility(View.VISIBLE);
            }
        });

        //================
        //     REDO
        //================
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playButton.setVisibility(View.GONE);
                redo.setVisibility(View.GONE);
                recordButton.setVisibility(View.VISIBLE);
            }
        });

        //================
        //  BEGIN PLAYING
        //================
        playButton.setOnClickListener(new View.OnClickListener() {
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
                playButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
            }
        });

        //================
        //     PAUSE
        //================
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRecordPlayer.isPlaying()) {
                    mRecordPlayer.pause();
                    pauseButton.setVisibility(View.GONE);
                    continuePlaying.setVisibility(View.VISIBLE);
                }

            }
        });

        //=====================
        //   CONTINUE PLAYING
        //=====================
        continuePlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecordPlayer.start();
            }
        });


        //================
        //     SUBMIT
        //================
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Create the song to upload and set references (Ids) to
                //the playlist it's on, the user (you) who uploaded it,
                //and the Id of the song itself.
                mSongToUpload = new Song();
                mSongToUpload.setTitle(songTitle.getText().toString());
                mSongToUpload.setPlaylistId(PLAYLIST_ID);
                mSongToUpload.setUserId(ME.getId());
                mSongToUpload.setSongId(SONG_ID);

                //Show the progress of the uploading file.
                final ProgressDialog progressDialog = new ProgressDialog(PlaylistActivity.this);
                progressDialog.setTitle("Uploading!");
                progressDialog.show();

                //Get the file ready to upload.
                Uri audioFileUri = Uri.fromFile(new File(mAudioSavePath));
                StorageReference songRef = mStorageRef.child(audioFileUri.getLastPathSegment());

                //Upload the file to Firebase Storage.
                songRef.putFile(audioFileUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                progressDialog.dismiss();

                                //The final piece of the Song object is to add the
                                //link to the song file itself on Firebase Storage.
                                mSongToUpload.setStreamUrl(taskSnapshot.getDownloadUrl());

                                //Then upload the Song Object to the database.
                                mSongListRef.child(SONG_ID).setValue(mSongToUpload);
                                Toast.makeText(getApplicationContext(), "Finished!", Toast.LENGTH_SHORT).show();

                                //Once it's uploaded to the database, add it to the list of songs
                                //and notify the adapter that the song was added.
                                mSongListRef.child(SONG_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue()==null) {
                                            mSongList.add(dataSnapshot.getValue(Song.class));
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

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

        return builder.create();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //    Setup MediaRecorder for recording dialog
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void MediaRecorderReady(){
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mMediaRecorder.setOutputFile(mAudioSavePath);
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //    Setup Views, Recycler Adapter, etc.
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void simpleSetup(){

        //  Recycler
        mRecyclerView = (RecyclerView) findViewById(R.id.playlist_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new PlaylistRecyclerAdapter(mSongList);
        mRecyclerView.setAdapter(mAdapter);

        //  Views
        mTitle = (TextView)findViewById(R.id.playlist_title) ;
        mDescription = (TextView)findViewById(R.id.playlist_description);
        mCover = (ImageView)findViewById(R.id.cover_image);
        mPlayPause = (CardView)findViewById(R.id.player_play_pause);
        mBack = (CardView)findViewById(R.id.player_back);
        mSkip = (CardView)findViewById(R.id.player_skip);
        fabAddSong = (FloatingActionButton)findViewById(R.id.fab_song);

        if(ME.getId()==null){
            fabAddSong.setVisibility(View.GONE);
        }

        //  Media Player
        if(mMediaPlayer==null) {
            mMediaPlayer = new MediaPlayer();
        }
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //    Gets the playlist info and songlist.
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void syncToDatabase(){
        DatabaseReference playlistRef = db.getReference("Playlists/"+PLAYLIST_ID);
        mSongListRef = db.getReference("Songs/"+PLAYLIST_ID);

        playlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mThisPlaylist = dataSnapshot.getValue(Playlist.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSongListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()==null) {
                    for (DataSnapshot songSnapshot:dataSnapshot.getChildren()) {
                        mSongList.add(songSnapshot.getValue(Song.class));
                    }
                    Collections.shuffle(mSongList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //          Check and Request Permissions
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void requestPermission() {
        ActivityCompat.requestPermissions(PlaylistActivity.this, new
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
                        Toast.makeText(PlaylistActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(PlaylistActivity.this,"Permission Denied",
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
