package com.ezequielc.successplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DailyActivity extends AppCompatActivity {
    TextView mCurrentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        mCurrentDate = (TextView) findViewById(R.id.current_date);
        String currentDate = getIntent().getStringExtra(MainActivity.DATE_FORMATTED);
        mCurrentDate.setText(currentDate);
    }
}
