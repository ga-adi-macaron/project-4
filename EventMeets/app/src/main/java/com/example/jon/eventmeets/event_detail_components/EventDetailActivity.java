package com.example.jon.eventmeets.event_detail_components;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jon.eventmeets.R;
import com.example.jon.eventmeets.event_detail_components.find_players_components.EventMessageActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private String mName, mImage, mCover;
    private int mId;
    private TextView mPlatform1, mPlatform2, mPlatform3, mPlatform4, mGameSummary;
    private FloatingActionButton mFab;
    private List<String> mPlatforms;

    private static final String IMAGE_URL = "https://images.igdb.com/igdb/image/upload/t_screenshot_med/";
    private static final String COVER_URL = "https://images.igdb.com/igdb/image/upload/t_cover_small/";

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

        if(intent.getStringExtra("cover").length() > 0) {
            mCover = COVER_URL + intent.getStringExtra("cover") + ".jpg";
        }

        String summary = intent.getStringExtra("summary");
        if(summary != null) {
            mGameSummary.setText(summary);
        } else {
            mGameSummary.setText("No Details Found");
        }

        mName = intent.getStringExtra("name");
        setTitle(mName);

        mId = intent.getIntExtra("id", -1);

        if(intent.getStringExtra("image").length() > 0) {
            mImage = IMAGE_URL+intent.getStringExtra("image")+".jpg";
            Picasso.with(this).load(mImage).fit().into((ImageView)findViewById(R.id.screen_image));
        }

        mPlatforms = intent.getStringArrayListExtra("platforms");
        if(mPlatforms.contains("XBox")) {
            mPlatform1.setVisibility(View.VISIBLE);
            mPlatform1.setText("XBox");
        }
        if(mPlatforms.contains("Nintendo")) {
            mPlatform4.setVisibility(View.VISIBLE);
            mPlatform4.setText("Nintendo");
        }
        if(mPlatforms.contains("PlayStation")) {
            mPlatform3.setVisibility(View.VISIBLE);
            mPlatform3.setText("PlayStation");
        }
        if(mPlatforms.contains("PC")) {
            mPlatform2.setVisibility(View.VISIBLE);
            mPlatform2.setText("PC");
        }

        mFab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.fab:
                consoleSelectionDialog();
                break;
        }
    }

    private void consoleSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Console");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        if(mPlatforms.contains("XBox")) {
            adapter.add("XBox");
        }
        if(mPlatforms.contains("Nintendo")) {
            adapter.add("Nintendo");
        }
        if(mPlatforms.contains("PlayStation")) {
            adapter.add("PlayStation");
        }
        if(mPlatforms.contains("PC")) {
            adapter.add("PC");
        }

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selected = adapter.getItem(which);
                onConsoleSelected(selected);
            }
        });

        builder.show();
    }

    private void onConsoleSelected(String console) {
        Intent intent = new Intent(this, EventMessageActivity.class);
        intent.putExtra("console", console);
        intent.putExtra("id", mId);
        intent.putExtra("name", mName);
        intent.putExtra("screencap", mImage);
        intent.putExtra("cover", mCover);
        startActivity(intent);
    }
}
