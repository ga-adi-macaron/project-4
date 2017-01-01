package com.example.jon.eventmeets.event_detail_components;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jon.eventmeets.R;
import com.squareup.picasso.Picasso;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private String mName, mImage;
    private int mId;
    private boolean mXbox, mPc, mNintendo, mPlaystation;
    private TextView mPlatform1, mPlatform2, mPlatform3, mPlatform4;
    private FloatingActionButton mFab;

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

        Intent intent = getIntent();

        mName = intent.getStringExtra("name");
        mImage = intent.getStringExtra("image");

        mId = intent.getIntExtra("id", -1);

        mXbox = intent.getBooleanExtra("xbox", false);
        mPc = intent.getBooleanExtra("pc", false);
        mNintendo = intent.getBooleanExtra("nintendo", false);
        mPlaystation = intent.getBooleanExtra("playstation", false);

        Picasso.with(this).load(mImage).into((ImageView)findViewById(R.id.screen_image));

        if(hasMultiplePlatforms()) {
            if(mXbox) {
                mPlatform1.setVisibility(View.VISIBLE);
                mPlatform1.setText("xbox");
            }
            if(mPc) {
                mPlatform2.setVisibility(View.VISIBLE);
                mPlatform2.setText("pc");
            }
            if(mNintendo) {
                mPlatform3.setVisibility(View.VISIBLE);
                mPlatform3.setText("nintendo");
            }
            if(mPlaystation) {
                mPlatform4.setVisibility(View.VISIBLE);
                mPlatform4.setText("playstation");
            }
        }

        mFab.setOnClickListener(this);
    }

    private boolean hasMultiplePlatforms() {
        return mXbox&&mPc || mXbox&&mNintendo || mXbox&&mPlaystation
                || mPc&&mNintendo || mPc&&mPlaystation || mPlaystation&&mNintendo;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.fab:

        }
    }

    private void consoleSelection() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select Console")
                .setView(R.layout.console_selection_dialog)
                .create();
    }
}
