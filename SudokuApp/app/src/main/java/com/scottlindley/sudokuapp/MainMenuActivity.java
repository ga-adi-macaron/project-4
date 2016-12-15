package com.scottlindley.sudokuapp;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainMenuActivity";
    public static final String DIFFICULTY_INTENT_KEY = "difficulty";
    public static final String PUZZLE_KEY_INTENT_KEY = "puzzle key";
    private static final int PUZZLE_REFRESH_JOB_ID = 88;
    private CardView mSoloCard, mRaceCard, mStatsCard, mSettingsCard;

    //TODO: THIS ACTIVITY IS A MESS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());
        getWindow().getAllowEnterTransitionOverlap();

        setContentView(R.layout.activity_main);

        DBAssetHelper dbAssetSetUp = new DBAssetHelper(MainMenuActivity.this);
        dbAssetSetUp.getReadableDatabase();

        setUpCardViews();

//        addPuzzle();

//        checkForNewPuzzles();

    }

    private void setUpCardViews(){
        mSoloCard = (CardView)findViewById(R.id.solo_card);
        mRaceCard = (CardView)findViewById(R.id.race_card);
        mStatsCard = (CardView)findViewById(R.id.stats_card);
        mSettingsCard = (CardView)findViewById(R.id.settings_card);

        mSoloCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView soloText = ((TextView)mSoloCard.getChildAt(0));
                TextView raceText = ((TextView)mRaceCard.getChildAt(0));
                TextView statsText = ((TextView)mStatsCard.getChildAt(0));
                ImageView settingsImage = ((ImageView)mSettingsCard.getChildAt(0));

                TextView easyText = (TextView)findViewById(R.id.easyText);
                TextView mediumText = (TextView)findViewById(R.id.mediumText);
                TextView hardText = (TextView)findViewById(R.id.hardText);
                TextView expertText = (TextView)findViewById(R.id.expertText);

                easyText.setVisibility(View.VISIBLE);
                mediumText.setVisibility(View.VISIBLE);
                hardText.setVisibility(View.VISIBLE);
                expertText.setVisibility(View.VISIBLE);

                soloText.setVisibility(View.INVISIBLE);
                raceText.setVisibility(View.INVISIBLE);
                statsText.setVisibility(View.INVISIBLE);
                settingsImage.setVisibility(View.INVISIBLE);

                easyText.setOnClickListener(MainMenuActivity.this);
                mediumText.setOnClickListener(MainMenuActivity.this);
                hardText.setOnClickListener(MainMenuActivity.this);
                expertText.setOnClickListener(MainMenuActivity.this);
            }
        });
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

    private void addPuzzle() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Puzzles").child("expert");

        List<Integer> key = new ArrayList<>();

        String nums =
                "";

        if (nums.length() == 81) {
            for (int i = 0; i < 81; i++) {
                key.add(Integer.parseInt(String.valueOf(nums.charAt(i))));
            }
            ref.push().setValue(key);
            ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(200);
        }
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        DBHelper helper = DBHelper.getInstance(this);
        String difficulty;
        String stringKey;
        String cardText = ((TextView)view).getText().toString();
        switch(cardText){
            case "Easy":
                difficulty = "easy";
                stringKey = helper.getEasyPuzzle().getKeyJSONArray().toString();
                break;
            case "Medium":
                difficulty = "medium";
                stringKey = helper.getMediumPuzzle().getKeyJSONArray().toString();
                break;
            case "Hard":
                difficulty = "hard";
                stringKey = helper.getHardPuzzle().getKeyJSONArray().toString();
                break;
            case "Expert":
                difficulty = "expert";
                stringKey = helper.getExpertPuzzle().getKeyJSONArray().toString();
                break;
            default:
                difficulty = "error";
                stringKey = "error";
        }
        Intent intent = new Intent(MainMenuActivity.this, PuzzleActivity.class);
        intent.putExtra(DIFFICULTY_INTENT_KEY, difficulty);
        intent.putExtra(PUZZLE_KEY_INTENT_KEY, stringKey);
        startActivity(intent);
    }
}
