package com.scottlindley.suyouthinkyoucandoku;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {
    private TextView mHighScore, mBestTime, mRacesWon, mRacesLost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        setUpViews();
    }

    public void setUpViews(){
        mHighScore = (TextView)findViewById(R.id.high_score);
        mBestTime = (TextView)findViewById(R.id.best_time);
        mRacesWon = (TextView)findViewById(R.id.races_won);
        mRacesLost = (TextView)findViewById(R.id.races_lost);

        Stats stats = DBHelper.getInstance(this).getStats();

        String time = DateUtils.formatElapsedTime(stats.getBestTime());
        mHighScore.setText("High Score: "+ stats.getHighscore());
        mBestTime.setText("Best Time: "+ time);
        mRacesWon.setText("Races Won:  "+ stats.getRacesWon());
        mRacesLost.setText("Races Lost: "+ stats.getRacesLost());
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
        super.onBackPressed();
    }
}
