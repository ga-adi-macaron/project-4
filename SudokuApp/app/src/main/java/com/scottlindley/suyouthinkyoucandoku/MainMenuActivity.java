package com.scottlindley.suyouthinkyoucandoku;

import android.app.ActivityOptions;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {
    private static final String TAG = "MainMenuActivity";
    private static final int PUZZLE_REFRESH_JOB_ID = 88;
    private CardView mSoloCard, mRaceCard, mStatsCard, mArmoryCard;
    public static final String RACE_INTENT_EXTRA = "race extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        TransitionSet transition = new TransitionSet();
        transition.addTransition(new ChangeTransform());
        getWindow().setSharedElementEnterTransition(transition);
        getWindow().setSharedElementReturnTransition(transition);

        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        DBAssetHelper dbAssetSetUp = new DBAssetHelper(MainMenuActivity.this);
        dbAssetSetUp.getReadableDatabase();

        setUpCardViews();

//        addPuzzle();

        checkForNewPuzzles();
    }


    private void setUpCardViews() {
        mSoloCard = (CardView) findViewById(R.id.solo_card);
        mRaceCard = (CardView) findViewById(R.id.race_card);
        mStatsCard = (CardView) findViewById(R.id.stats_card);
        mArmoryCard = (CardView) findViewById(R.id.armory_card);

        mSoloCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair<View, String> pair1 =
                        Pair.create(findViewById(R.id.solo_card), getString(R.string.transition1));
                Pair<View, String> pair2 =
                        Pair.create(findViewById(R.id.race_card), getString(R.string.transition2));
                Pair<View, String> pair3 =
                        Pair.create(findViewById(R.id.armory_card), getString(R.string.transition3));
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
                NetworkConnectivityChecker networkChecker = new NetworkConnectivityChecker(MainMenuActivity.this);
                if (networkChecker.isConnected()) {
                    launchWeaponArmingDialog();
                } else {
                    Toast.makeText(MainMenuActivity.this, "No network detected", Toast.LENGTH_SHORT).show();
                }
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

        mArmoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, ArmoryActivity.class));
            }
        });
    }

    private void checkForNewPuzzles(){
        NetworkConnectivityChecker networkChecker = new NetworkConnectivityChecker(MainMenuActivity.this);
        if (networkChecker.isConnected()) {
            DBHelper.getInstance(this).setUpBroadcastReceiver();
            JobInfo puzzleRefreshJob = new JobInfo.Builder(PUZZLE_REFRESH_JOB_ID,
                    new ComponentName(this, PuzzleRefreshService.class))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .build();
            JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            scheduler.schedule(puzzleRefreshJob);
        }
    }

    private void launchWeaponArmingDialog(){
        View dialogView = getLayoutInflater().inflate(R.layout.arm_weapons_dialog, null);

        WeaponDialog dialog = (WeaponDialog) new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton("okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainMenuActivity.this, RaceActivity.class);
                        intent.putExtra(RACE_INTENT_EXTRA, true);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();

        setUpDialogViews(dialogView, dialog);
        dialog.show();
    }

    private void setUpDialogViews(View dialogView, final WeaponDialog dialog){
        SharedPreferences prefs = getSharedPreferences(ArmoryActivity.ARMORY_SHARED_PREFS, MODE_PRIVATE);

        final ImageView bombIcon = (ImageView)dialogView.findViewById(R.id.bomb_inventory_icon);
        final ImageView spyIcon = (ImageView)dialogView.findViewById(R.id.spy_inventory_icon);
        final ImageView interfIcon = (ImageView)dialogView.findViewById(R.id.interference_inventory_icon);

        CardView weaponSlot1 = (CardView)dialogView.findViewById(R.id.weapon1);
        CardView weaponSlot2 = (CardView)dialogView.findViewById(R.id.weapon2);

        final int bombCount = prefs.getInt(ArmoryActivity.BOMB_COUNT_KEY, 0);
        final int spyCount = prefs.getInt(ArmoryActivity.SPY_COUNT_KEY, 0);
        final int interfCount = prefs.getInt(ArmoryActivity.INTERFERENCE_COUNT_KEY, 0);

        TextView bombInventoryText = (TextView)dialog.findViewById(R.id.bomb_inventory_count);
        TextView spyInventoryText = (TextView)dialog.findViewById(R.id.spy_inventory_count);
        TextView interfInventoryText = (TextView)dialog.findViewById(R.id.interference_inventory_count);

        bombIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bombCount > 0){
                    spyIcon.setBackgroundColor(Color.WHITE);
                    interfIcon.setBackgroundColor(Color.WHITE);
                    view.setBackgroundColor(Color.rgb(75,75,75));
                    dialog.mSelectedWeapon = "bomb";
                }
            }
        });

        spyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spyCount > 0){
                    bombIcon.setBackgroundColor(Color.WHITE);
                    interfIcon.setBackgroundColor(Color.WHITE);
                    view.setBackgroundColor(Color.rgb(75,75,75));
                    dialog.mSelectedWeapon = "spy";
                }
            }
        });

        interfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(interfCount > 0){
                    bombIcon.setBackgroundColor(Color.WHITE);
                    spyIcon.setBackgroundColor(Color.WHITE);
                    view.setBackgroundColor(Color.rgb(75,75,75));
                    dialog.mSelectedWeapon = "interference";
                }
            }
        });
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