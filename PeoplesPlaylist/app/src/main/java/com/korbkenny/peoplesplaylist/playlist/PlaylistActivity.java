package com.korbkenny.peoplesplaylist.playlist;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
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
import com.korbkenny.peoplesplaylist.objects.Playlist;
import com.korbkenny.peoplesplaylist.objects.Song;

import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {
    public static final int REQUEST_SONG_CODE = 108;

    private RecyclerView mRecyclerView;
    private PlaylistRecyclerAdapter mAdapter;
    private String PLAYLIST_ID;
    private FirebaseDatabase db;
    private DatabaseReference mPlaylistRef;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        setupEverything();

        //  Choose a song to add to the playlist
        fabAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSongToUpload == null) {
                    mSongToUpload = new Song();
                }
                onCreateDialog().show();
            }
        });
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //     Add Song Dialog, complete with Select Track helper
    //     method, onActivityResult for when the track starts
    //     uploading to storage, and uploading song to database.
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Dialog onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.add_song_dialog, null))
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog view = (Dialog) dialog;
                        EditText title = (EditText) view.findViewById(R.id.add_song_title);
                        mSongToUpload.setTitle(title.getText().toString());
                        selectTrack();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    private void selectTrack() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/mpeg");
        startActivityForResult(Intent.createChooser(intent, "Select Track"), REQUEST_SONG_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SONG_CODE && resultCode == Activity.RESULT_OK) {
            if ((data != null) && (data.getData() != null)) {

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading! WOO!");
                progressDialog.show();

                Uri audioFileUri = data.getData();
                StorageReference songRef = mStorageRef.child(audioFileUri.getLastPathSegment());

                songRef.putFile(audioFileUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                mSongToUpload.setStreamUrl(taskSnapshot.getDownloadUrl());
                                mSongToUpload.setSongId(mSongToUpload.getTitle()+(int)(Math.random()*1000));
                                mSongListRef.push().setValue(mSongToUpload);
                                Toast.makeText(getApplicationContext(), "Finished!", Toast.LENGTH_SHORT).show();
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
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //     Helper methods for setting up all the views,
    //     including notifying the adapter when anything
    //     has been changed in the firebase database.
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void setupEverything(){
        PLAYLIST_ID = getIntent().getStringExtra("Playlist Id");
        db = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mPlaylistRef = db.getReference("Playlists/"+PLAYLIST_ID);
        mSongListRef = db.getReference("Playlists/"+PLAYLIST_ID+"/SongList");

        mRecyclerView = (RecyclerView) findViewById(R.id.playlist_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(llm);

        mPlaylistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mThisPlaylist = dataSnapshot.getValue(Playlist.class);
                mSongList = mThisPlaylist.getSongList();
                if(mSongList == null){
                    mSongList = new ArrayList<>();
                }
                mAdapter = new PlaylistRecyclerAdapter(mSongList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //  Views
        mTitle = (TextView)findViewById(R.id.playlist_title) ;
        mDescription = (TextView)findViewById(R.id.playlist_description);
        mCover = (ImageView)findViewById(R.id.cover_image);
        mPlayPause = (CardView)findViewById(R.id.player_play_pause);
        mBack = (CardView)findViewById(R.id.player_back);
        mSkip = (CardView)findViewById(R.id.player_skip);
        fabAddSong = (FloatingActionButton)findViewById(R.id.fab_song);

        //  Media Player
        if(mMediaPlayer==null) {
            mMediaPlayer = new MediaPlayer();
        }
    }
}
