package com.scottlindley.suyouthinkyoucandoku;

import android.app.ActivityOptions;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.util.Pair;
import android.view.View;
import android.view.Window;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {
    private static final String TAG = "MainMenuActivity";
    private static final int PUZZLE_REFRESH_JOB_ID = 88;
    private CardView mSoloCard, mRaceCard, mStatsCard, mSettingsCard;
    public static final String RACE_INTENT_EXTRA = "race extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        TransitionSet transition = new TransitionSet();
        transition.addTransition(new ChangeTransform());
        getWindow().setSharedElementEnterTransition(transition);
        getWindow().setSharedElementReturnTransition(transition);

//        getWindow().setExitTransition(new Explode());
//        getWindow().setEnterTransition(new Explode());

        setContentView(R.layout.activity_main);

        DBAssetHelper dbAssetSetUp = new DBAssetHelper(MainMenuActivity.this);
        dbAssetSetUp.getReadableDatabase();

        setUpCardViews();

        
//        addPuzzle();

        checkForNewPuzzles();

        DBHelper.getInstance(this).getStats();
    }


    private void setUpCardViews() {
        mSoloCard = (CardView) findViewById(R.id.solo_card);
        mRaceCard = (CardView) findViewById(R.id.race_card);
        mStatsCard = (CardView) findViewById(R.id.stats_card);
        mSettingsCard = (CardView) findViewById(R.id.settings_card);

        mSoloCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair<View, String> pair1 =
                        Pair.create(findViewById(R.id.solo_card), getString(R.string.transition1));
                Pair<View, String> pair2 =
                        Pair.create(findViewById(R.id.race_card), getString(R.string.transition2));
                Pair<View, String> pair3 =
                        Pair.create(findViewById(R.id.settings_card), getString(R.string.transition3));
                Pair<View, String> pair4 =
                        Pair.create(findViewById(R.id.stats_card), getString(R.string.transition4));
                ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation(
                                MainMenuActivity.this, pair1, pair2, pair3, pair4);
                startActivity(new Intent(MainMenuActivity.this, SoloActivity.class), options.toBundle());
            }
        });

        mRaceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, RaceActivity.class);
                intent.putExtra(RACE_INTENT_EXTRA, true);
                startActivity(intent);
            }
        });

        mStatsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair<View, String> pair =
                        Pair.create(findViewById(R.id.stats_card), getString(R.string.transition4));
                ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation(MainMenuActivity.this, pair);
                startActivity(new Intent(MainMenuActivity.this, StatsActivity.class), options.toBundle());
            }
        });
    }

    //TODO: DELETES EVERYTHING
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

}
