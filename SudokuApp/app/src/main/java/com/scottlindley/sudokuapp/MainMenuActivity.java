package com.scottlindley.sudokuapp;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {
    private static final String TAG = "MainMenuActivity";
    public static final String DIFFICULTY_INTENT_KEY = "difficulty";
    private static final int PUZZLE_REFRESH_JOB_ID = 88;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBAssetHelper dbAssetSetUp = new DBAssetHelper(MainMenuActivity.this);
        dbAssetSetUp.getReadableDatabase();

        checkForNewPuzzles();

        Intent intent = new Intent(MainMenuActivity.this, PuzzleActivity.class);
        intent.putExtra(DIFFICULTY_INTENT_KEY, "medium");
        startActivity(intent);
    }

    private void checkForNewPuzzles(){
        DBHelper.getInstance(this).setUpBroadcastReceiver();
        JobInfo puzzleRefreshJob = new JobInfo.Builder(PUZZLE_REFRESH_JOB_ID,
                new ComponentName(this, PuzzleRefreshService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.schedule(puzzleRefreshJob);
    }
}
