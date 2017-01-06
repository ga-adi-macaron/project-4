package com.korbkenny.peoplesplaylist.playlist;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.korbkenny.peoplesplaylist.BitmapManipulation;
import com.korbkenny.peoplesplaylist.LoginActivity;
import com.korbkenny.peoplesplaylist.R;
import com.korbkenny.peoplesplaylist.RecordingActivity;
import com.korbkenny.peoplesplaylist.UserSingleton;
import com.korbkenny.peoplesplaylist.objects.Playlist;
import com.korbkenny.peoplesplaylist.objects.Song;
import com.korbkenny.peoplesplaylist.objects.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistActivity extends AppCompatActivity implements
        PlaylistRecyclerAdapter.RecyclerItemClickListener
{
    public static final Integer PLAYING = 1;
    public static final Integer PAUSED = 0;
    public static final int PICK_IMAGE_REQUEST = 111;
    public static final int RECORDING_RESULT_CODE = 666;
    private boolean firstTimePlaying = true;
    private static final String TAG = "Yes. ";
    //db
    private FirebaseDatabase db;
    private DatabaseReference mSongListRef, mPlaylistRef;
    private StorageReference mStoragePictureRef;
    //playlist-user
    private String PLAYLIST_ID, SONG_ID;
    private User ME;
    private Playlist mThisPlaylist;
    //views
    private CardView fabAddSong, mSaveThisPlaylist;
    private TextView mTitle,mDescription;
    private ImageView mCover, vUserIcon, mBigCover, mPlayPause;
    private RecyclerView mRecyclerView;
    private PlaylistRecyclerAdapter mAdapter;
    private RelativeLayout mFrame;
    //utility
    public MediaPlayer mMediaPlayer;
    private List<Song> mSongList;
    private SharedPreferences settings;
    //extra
    private HashMap<DatabaseReference,ValueEventListener> mDbListeners;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        //  Setup views and everything
        simpleSetup();
        mediaPlayerButtons();
        mDbListeners = new HashMap<>();
        syncToDatabase();

        //==============
        //  Add a Track
        //==============
        fabAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaylistActivity.this, RecordingActivity.class);
                intent.putExtra("PlaylistId",PLAYLIST_ID);
                startActivityForResult(intent,RECORDING_RESULT_CODE);
            }
        });

        //========================================
        //  Either upload an image to be the cover,
        //  or if it's already there, view it large
        //========================================
        mCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  If the cover's empty and it's your playlist, pick photo
                if (mThisPlaylist.getCover().equals("null") && ME.getId().equals(mThisPlaylist.getUserId())){
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                //  If it's not empty, regardless of who's playlist it is, view it large
                else {
                    mFrame.setVisibility(View.VISIBLE);
                    mBigCover.setVisibility(View.VISIBLE);
                    Target target = new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            mBigCover.setImageBitmap(bitmap);
                            ValueAnimator ani = ValueAnimator.ofFloat(0.3f, 1);
                            ani.setDuration(300);
                            ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    mBigCover.setAlpha((float) animation.getAnimatedValue());
                                    mFrame.setAlpha((float) animation.getAnimatedValue());
                                }
                            });
                            ani.start();
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    };

                    mBigCover.setTag(target);
                    Picasso.with(PlaylistActivity.this).load(mThisPlaylist.getCover()).into(target);

                    //  If anywhere outside of the image is clicked, exit.
                    mFrame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mFrame.setVisibility(View.GONE);
                            mBigCover.setVisibility(View.GONE);
                        }
                    });

                    //  If you click the image and it's your playlist, re-choose image
                    //  If it's not your playlist, then just exit.
                    mBigCover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(mThisPlaylist.getUserId().equals(ME.getId())){
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , 1);
                            }
                            else{
                                mFrame.setVisibility(View.GONE);
                                mBigCover.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });


        //=================
        //  Pick Image
        //=================

            mCover.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (ME.getId().equals(mThisPlaylist.getUserId())) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST);

                    }
                    return false;
                }
            });

        //=================
        //  Save Playlist
        //=================
        mSaveThisPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.edit().putString("SavedPlaylistId",PLAYLIST_ID).commit();
                Toast.makeText(PlaylistActivity.this, "Saved This Playlist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //    On Activity Result from:
    //              1. Recording a Track
    //              2. Loading an image
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        //COMING FROM A SUCCESSFUL RECORDING ACTIVITY
        if(resultCode == RESULT_OK && requestCode == RECORDING_RESULT_CODE)  {
            SONG_ID = imageReturnedIntent.getStringExtra("result");

            mSongListRef.child(SONG_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue()!=null) {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                mSongList.add(dataSnapshot.getValue(Song.class));
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                mAdapter.notifyDataSetChanged();

                            }
                        }.execute();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        //COMING FROM A SUCCESSFUL IMAGE-PICK SESSION
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST){
            //  If image came from the big image part, reset views
            if(mBigCover.getVisibility()==View.VISIBLE){
                    mBigCover.setVisibility(View.GONE);
                    mFrame.setVisibility(View.GONE);
                }

                //  Get the image and crop it to a 400x400 circle
                Uri selectedImage = imageReturnedIntent.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                    final Bitmap squareBitmap = BitmapManipulation.cropToSquare(bitmap);
                    Bitmap resizedBitmap = BitmapManipulation.resizeForCover(squareBitmap,400);
                    final Bitmap circleBitmap = BitmapManipulation.cropToCircle(resizedBitmap);

                    //  Save the image to your phone.
                    new AsyncTask<Void,Void,String>(){
                        @Override
                        protected String doInBackground(Void... voids) {
                            return saveImageToDisk(circleBitmap);
                        }

                        //  Then upload to firebase storage
                        @Override
                        protected void onPostExecute(String coverPath) {
                            Log.d(TAG, "onPostExecute: " + coverPath);
                            Uri file = Uri.fromFile(new File(coverPath+"/playlistcover.png"));
                            mCover.setImageURI(file);
                            mStoragePictureRef.child("playlistcover.png").putFile(file)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            if(taskSnapshot.getDownloadUrl()!=null) {
                                                mThisPlaylist.setCover(taskSnapshot.getDownloadUrl().toString());
                                                mPlaylistRef.setValue(mThisPlaylist);
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                    }.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //        Remove listeners when activity stops
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    protected void onStop() {
        super.onStop();
        for (Map.Entry<DatabaseReference, ValueEventListener> entry : mDbListeners.entrySet()) {
            DatabaseReference ref = entry.getKey();
            ValueEventListener listener = entry.getValue();
            ref.removeEventListener(listener);
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //    Setup Views, Recycler Adapter, etc.
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void simpleSetup(){

        //  Get intent
        PLAYLIST_ID = getIntent().getStringExtra("Playlist Id");

        //  Setup data references
        db = FirebaseDatabase.getInstance();
        mSongListRef = db.getReference("SongLists").child(PLAYLIST_ID);
        mStoragePictureRef = FirebaseStorage.getInstance().getReference("albumcovers").child(PLAYLIST_ID);
        ME = UserSingleton.getInstance().getUser();
        settings = getSharedPreferences(LoginActivity.SHARED_PREF,MODE_PRIVATE);

        //  Instantiate Song List
        mSongList = new ArrayList<>();

        //  Recycler
        mRecyclerView = (RecyclerView) findViewById(R.id.playlist_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new PlaylistRecyclerAdapter(mSongList, this);
        mRecyclerView.setAdapter(mAdapter);

        //  Views
        mTitle = (TextView)findViewById(R.id.playlist_title) ;
        mDescription = (TextView)findViewById(R.id.playlist_description);
        mCover = (ImageView)findViewById(R.id.cover_image);
        mPlayPause = (ImageView)findViewById(R.id.player_play_pause);
        mPlayPause.setTag(PAUSED);
        fabAddSong = (CardView) findViewById(R.id.add_song);
        vUserIcon = (ImageView) findViewById(R.id.playlist_user_icon);
        mSaveThisPlaylist = (CardView) findViewById(R.id.save_this_playlist);

        //  Big Cover Views
        mFrame = (RelativeLayout)findViewById(R.id.big_cover_frame);
        mBigCover = (ImageView)findViewById(R.id.big_big_cover);

        //  If you're not logged in, you can't add a song.
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
        mPlaylistRef = db.getReference("Playlists/"+PLAYLIST_ID);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... voids) {
                        mThisPlaylist = dataSnapshot.getValue(Playlist.class);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        mTitle.setText(mThisPlaylist.getTitle());
                        mDescription.setText(mThisPlaylist.getDescription());
                        if (mThisPlaylist.getCover().equals("null")){
                            mCover.setBackgroundResource(R.drawable.coverplaceholder);
                        } else {
                            Picasso.with(PlaylistActivity.this).load(mThisPlaylist.getCover()).into(mCover);
                        }
                        Picasso.with(PlaylistActivity.this).load(mThisPlaylist.getUserIcon()).into(vUserIcon);
                    }
                }.execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mPlaylistRef.addListenerForSingleValueEvent(listener);
        mDbListeners.put(mPlaylistRef,listener);

        ValueEventListener songsListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (dataSnapshot.getValue()!=null) {
                            for (DataSnapshot songSnapshot:dataSnapshot.getChildren()) {
                                mSongList.add(songSnapshot.getValue(Song.class));
                            }
                            Collections.shuffle(mSongList);

                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        mAdapter.notifyDataSetChanged();
                    }
                }.execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mSongListRef.addListenerForSingleValueEvent(songsListener);
        mDbListeners.put(mSongListRef,songsListener);
    }



    private String saveImageToDisk(Bitmap bitmap){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir",MODE_PRIVATE);
        File imagePath = new File(directory,"playlistcover.png");

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG,0,fos);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        Log.d(TAG, "saveImageToDisk: " + directory.getAbsolutePath());
        return directory.getAbsolutePath();
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //  When an item is clicked in the
    //  recyclerview, play that track
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onClickListener(Song song, final int position) {
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(mediaPlayer != null){
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mPlayPause.setTag(PAUSED);
                    mPlayPause.setImageResource(R.drawable.playbutton);
                }

            }
        });
        if (mMediaPlayer!=null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;
            } else {
                mMediaPlayer.reset();
                mMediaPlayer.release();
            }
        }
            mMediaPlayer = MediaPlayer.create(this, Uri.parse(song.getStreamUrl()));
            mMediaPlayer.start();
            firstTimePlaying = false;

        if(mPlayPause.getTag() == PAUSED){
            mPlayPause.setTag(PLAYING);
            mPlayPause.setImageResource(R.drawable.pausebutton);
        }
    }

    public void mediaPlayerButtons(){
        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPlayPause.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(1000);
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPlayPause.setEnabled(true);
                            }
                        });
                    }
                }).start();

                if(mMediaPlayer!=null) {
                    if(firstTimePlaying){
                        if(mSongList.size() > 0) {
                            mMediaPlayer.reset();
                            mMediaPlayer.release();
                            mMediaPlayer = MediaPlayer.create(PlaylistActivity.this, Uri.parse(mSongList.get(0).getStreamUrl()));
                            mMediaPlayer.start();
                            if (mPlayPause.getTag() == PAUSED) {
                                mPlayPause.setTag(PLAYING);
                                mPlayPause.setImageResource(R.drawable.pausebutton);
                            }
                            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    if(mediaPlayer != null){
                                        mediaPlayer.release();
                                    }
                                    mPlayPause.setTag(PAUSED);
                                    mPlayPause.setImageResource(R.drawable.playbutton);
                                }
                            });
                        }
                    } else {
                        if (mMediaPlayer.isPlaying()) {
                            mMediaPlayer.pause();
                            if (mPlayPause.getTag() == PLAYING) {
                                mPlayPause.setTag(PAUSED);
                                mPlayPause.setImageResource(R.drawable.pausebutton);
                            }
                        } else if (!mMediaPlayer.isPlaying()) {
                            mMediaPlayer.start();
                            if (mPlayPause.getTag() == PAUSED) {
                                mPlayPause.setTag(PLAYING);
                                mPlayPause.setImageResource(R.drawable.pausebutton);
                            }
                            firstTimePlaying = false;
                        }
                    }
                }
            }
        });
    }
    private void loadUserIcon() {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                vUserIcon.setImageBitmap(bitmap);
                ValueAnimator ani = ValueAnimator.ofFloat(0.3f, 1);
                ani.setDuration(300);
                ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        vUserIcon.setAlpha((float) animation.getAnimatedValue());
                    }
                });
                ani.start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        vUserIcon.setTag(target);
        Picasso.with(this).load(mThisPlaylist.getUserIcon()).into(target);
    }
}
