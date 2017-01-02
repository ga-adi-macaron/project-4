package com.example.jon.eventmeets.event_detail_components;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jon.eventmeets.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private String mName, mImage;
    private int mId;
    private TextView mPlatform1, mPlatform2, mPlatform3, mPlatform4, mGameSummary;
    private FloatingActionButton mFab;

    private static final String IMAGE_URL = "https://images.igdb.com/igdb/image/upload/t_screenshot_med/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton)findViewById(R.id.fab);

        mPlatform1 = (TextView)findViewById(R.id.platform1);
        mPlatform2 = (TextView)findViewById(R.id.platform2);
        mPlatform3 = (TextView)findViewById(R.id.platform3);
        mPlatform4 = (TextView)findViewById(R.id.platform4);
        mGameSummary = (TextView)findViewById(R.id.game_summary);

        Intent intent = getIntent();

        String summary = intent.getStringExtra("summary");
        if(summary.length() > 0) {
            mGameSummary.setText(summary);
        } else {
            mGameSummary.setText("No Details Found");
        }

        mName = intent.getStringExtra("name");
        setTitle(mName);

        mId = intent.getIntExtra("id", -1);

        mImage = intent.getStringExtra("image");
        if(mImage.length() > 0) {
            mImage = IMAGE_URL+mImage+".jpg";
            Picasso.with(this).load(mImage).fit().into((ImageView)findViewById(R.id.screen_image));
        }

        ArrayList<String> platforms = intent.getStringArrayListExtra("platforms");
        if(platforms.contains("XBox")) {
            mPlatform1.setVisibility(View.VISIBLE);
            mPlatform1.setText("XBox");
        }
        if(platforms.contains("PC")) {
            mPlatform2.setVisibility(View.VISIBLE);
            mPlatform2.setText("PC");
        }
        if(platforms.contains("PlayStation")) {
            mPlatform3.setVisibility(View.VISIBLE);
            mPlatform3.setText("PlayStation");
        }
        if(platforms.contains("Nintendo")) {
            mPlatform4.setVisibility(View.VISIBLE);
            mPlatform4.setText("Nintendo");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.fab:

        }
    }
}
