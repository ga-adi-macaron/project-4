package com.colinbradley.xboxoneutilitiesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoPlayerActivity extends AppCompatActivity {
    JCVideoPlayerStandard mVideoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Intent intent = getIntent();

        String videoURL = intent.getStringExtra("url");
        String imageURL = intent.getStringExtra("thumb");
        String title = intent.getStringExtra("title");


        mVideoPlayer = (JCVideoPlayerStandard)findViewById(R.id.video_player);
        mVideoPlayer.setUp(videoURL, JCVideoPlayer.SCREEN_LAYOUT_LIST, title);
        ImageView player = mVideoPlayer.thumbImageView;
        Picasso.with(this).load(imageURL).into(player);

    }

    @Override
    public void onBackPressed() {
        if (mVideoPlayer.backPress()){
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mVideoPlayer.releaseAllVideos();

    }
}
