package com.colinbradley.xboxoneutilitiesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        String videoURL = "https://gameclipscontent-d2022.xboxlive.com/0009000009589a75-941012fc-1902-4e0f-b407-401aa969a447/GameClip-Original.MP4?sv=2015-12-11&sr=b&si=DefaultAccess&sig=zTK4dBfaZ3zf1sBNcuS9idgvQY%2B5zfHEsurvSNC2IAI%3D&__gda__=1482334694_80d83cd1144d9c69e0523de207159419";
        String imageURL = "https://gameclipscontent-t2022.xboxlive.com/0009000009589a75-941012fc-1902-4e0f-b407-401aa969a447-public/Thumbnail_Small.PNG?sv=2015-12-11&sr=b&si=DefaultAccess&sig=xedQgJ4cCIvOrJjFDGFJnNYThlDm9yOmEgnCj8l8K%2BQ%3D";
        String title = "Testing Video Player";


        JCVideoPlayerStandard videoPlayer = (JCVideoPlayerStandard)findViewById(R.id.video_player);
        videoPlayer.setUp(videoURL, JCVideoPlayer.SCREEN_LAYOUT_LIST, title);
        ImageView mayb = videoPlayer.thumbImageView;
        Picasso.with(this).load(imageURL).into(mayb);



    }
}
