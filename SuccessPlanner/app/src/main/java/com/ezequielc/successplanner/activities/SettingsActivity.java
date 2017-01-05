package com.ezequielc.successplanner.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ezequielc.successplanner.R;

public class SettingsActivity extends AppCompatActivity {
    TextView mChangeVisionBoardColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Settings");

        mChangeVisionBoardColor = (TextView) findViewById(R.id.change_vision_board_background_color);
        mChangeVisionBoardColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "Change Vision Board Background Color", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
