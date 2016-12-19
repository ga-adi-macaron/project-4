package com.korbkenny.peoplesplaylist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlaylistActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    PlaylistRecyclerAdapter mAdapter;
    String PLAYLIST_ID;
    FirebaseDatabase db;
    DatabaseReference mPlaylistRef;
    Playlist mThisPlaylist;
    CardView mPlayPause, mBack, mSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        //  Get the playlist from the database
        PLAYLIST_ID = getIntent().getStringExtra("Playlist Id");
        db = FirebaseDatabase.getInstance();
        mPlaylistRef = db.getReference("Playlists/"+PLAYLIST_ID);

        mPlaylistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mThisPlaylist = dataSnapshot.getValue(Playlist.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //  Views Setup
        TextView title = (TextView)findViewById(R.id.playlist_title) ;
        TextView description = (TextView)findViewById(R.id.playlist_description);
        ImageView cover = (ImageView)findViewById(R.id.cover_image);
        mPlayPause = (CardView)findViewById(R.id.player_play_pause);
        mBack = (CardView)findViewById(R.id.player_back);
        mSkip = (CardView)findViewById(R.id.player_skip);

        //  Recyclerview Setup
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_cardview);
        mAdapter = new PlaylistRecyclerAdapter(mThisPlaylist.getSongList());
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mAdapter);

        

    }
}
