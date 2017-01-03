package com.ezequielc.successplanner.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ezequielc.successplanner.R;

public class VisionBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_board);

        getSupportActionBar().setTitle("Vision Board");
    }
}
