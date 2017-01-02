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
    public static final String ARMED_WEAPONS_PREFS = "armed weapons";
    public static final String WEAPON_SLOT1_KEY = "slot 1";
    public static final String WEAPON_SLOT2_KEY = "slot 2";
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

        //TODO: REPLACE CURRENT PUZZLES WITH NEW ONES
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

    /*
    When Wifi connection is established, download new puzzles from the remote
    database and store them locally.
    */
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

    /*
    Allows to user to arm up to two weapons in their inventory and bring them into the race arena.
     */
    private void launchWeaponArmingDialog(){
        final View dialogView = getLayoutInflater().inflate(R.layout.arm_weapons_dialog, null);

        final WeaponDialog dialog = (WeaponDialog) new WeaponDialog(this)
                .setView(dialogView)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        setUpDialogViews(dialogView, dialog);
        dialog.setPositiveButton("okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor prefsEditor =
                        getSharedPreferences(ARMED_WEAPONS_PREFS, MODE_PRIVATE).edit();

                prefsEditor.putString(WEAPON_SLOT1_KEY, dialog.mWeaponSlot1);
                prefsEditor.putString(WEAPON_SLOT2_KEY, dialog.mWeaponSlot2);
                prefsEditor.commit();

                Intent intent = new Intent(MainMenuActivity.this, RaceActivity.class);
                intent.putExtra(RACE_INTENT_EXTRA, true);
                startActivity(intent);
            }
        });
        dialog.create();
        dialog.show();
    }

    private void setUpDialogViews(final View dialogView, final WeaponDialog dialog){
        SharedPreferences prefs = getSharedPreferences(ArmoryActivity.ARMORY_SHARED_PREFS, MODE_PRIVATE);

        final ImageView bombIcon = (ImageView)dialogView.findViewById(R.id.bomb_inventory_icon);
        final ImageView spyIcon = (ImageView)dialogView.findViewById(R.id.spy_inventory_icon);
        final ImageView interfIcon = (ImageView)dialogView.findViewById(R.id.interference_inventory_icon);

        final CardView weaponSlot1 = (CardView)dialogView.findViewById(R.id.weapon1);
        CardView weaponSlot2 = (CardView)dialogView.findViewById(R.id.weapon2);

        final int bombCount = prefs.getInt(ArmoryActivity.BOMB_COUNT_KEY, 0);
        final int spyCount = prefs.getInt(ArmoryActivity.SPY_COUNT_KEY, 0);
        final int interfCount = prefs.getInt(ArmoryActivity.INTERFERENCE_COUNT_KEY, 0);

        final TextView bombInventoryText = (TextView)dialogView.findViewById(R.id.bomb_inventory_count);
        final TextView spyInventoryText = (TextView)dialogView.findViewById(R.id.spy_inventory_count);
        final TextView interfInventoryText = (TextView)dialogView.findViewById(R.id.interference_inventory_count);

        bombInventoryText.setText(String.valueOf(bombCount));
        spyInventoryText.setText(String.valueOf(spyCount));
        interfInventoryText.setText(String.valueOf(interfCount));

        bombIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(bombInventoryText.getText().toString()) > 0){
                    interfIcon.setColorFilter(Color.WHITE);
                    spyIcon.setColorFilter(Color.WHITE);
                    ((ImageView)view).setColorFilter(Color.rgb(75,75,75));
                    dialog.mSelectedWeapon = "bomb";
                }
            }
        });

        spyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(spyInventoryText.getText().toString()) > 0){
                    bombIcon.setColorFilter(Color.WHITE);
                    interfIcon.setColorFilter(Color.WHITE);
                    ((ImageView)view).setColorFilter(Color.rgb(75,75,75));
                    dialog.mSelectedWeapon = "spy";
                }
            }
        });

        interfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(interfInventoryText.getText().toString()) > 0){
                    bombIcon.setColorFilter(Color.WHITE);
                    spyIcon.setColorFilter(Color.WHITE);
                    ((ImageView)view).setColorFilter(Color.rgb(75,75,75));
                    dialog.mSelectedWeapon = "interference";
                }
            }
        });

        View.OnClickListener weaponSlotClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView weaponImage;
                String weaponSlot;
                switch (view.getId()){
                    case R.id.weapon1:
                        weaponImage = (ImageView)dialogView.findViewById(R.id.weapon1_image);
                        weaponSlot = "slot 1";
                        break;
                    case R.id.weapon2:
                        weaponImage = (ImageView)dialogView.findViewById(R.id.weapon2_image);
                        weaponSlot = "slot 2";
                        break;
                    default:
                        return;
                }

                String selectedWeapon = dialog.mSelectedWeapon;
                if(!selectedWeapon.equals("")) {
                    if (selectedWeapon.equals("bomb")){
                        weaponImage.setImageResource(R.drawable.explosion);
                        int bombCount = Integer.parseInt(bombInventoryText.getText().toString());
                        bombInventoryText.setText(String.valueOf(bombCount-1));
                    } else if (selectedWeapon.equals("spy")){
                        weaponImage.setImageResource(R.drawable.eye);
                        int spyCount = Integer.parseInt(spyInventoryText.getText().toString());
                        spyInventoryText.setText(String.valueOf(spyCount-1));
                    } else if (selectedWeapon.equals("interference")){
                        weaponImage.setImageResource(R.drawable.lightning);
                        int interfCount = Integer.parseInt(interfInventoryText.getText().toString());
                        interfInventoryText.setText(String.valueOf(interfCount-1));
                    }
                    if (weaponSlot.equals("slot 1")){
                        dialog.mWeaponSlot1 = selectedWeapon;
                    } else if (weaponSlot.equals("slot 2")){
                        dialog.mWeaponSlot2 = selectedWeapon;
                    }
                    dialog.mSelectedWeapon = "";
                    bombIcon.setColorFilter(Color.WHITE);
                    spyIcon.setColorFilter(Color.WHITE);
                    interfIcon.setColorFilter(Color.WHITE);
                }


            }
        };

        View.OnLongClickListener weaponSlotLongListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                SharedPreferences.Editor prefsEditor =
                        getSharedPreferences(ARMED_WEAPONS_PREFS, MODE_PRIVATE).edit();

                ImageView weaponImage;
                String weaponSlot;
                switch (view.getId()){
                    case R.id.weapon1:
                        weaponImage = (ImageView)dialogView.findViewById(R.id.weapon1_image);
                        weaponSlot = "slot 1";
                        break;
                    case R.id.weapon2:
                        weaponImage = (ImageView)dialogView.findViewById(R.id.weapon2_image);
                        weaponSlot = "slot 2";
                        break;
                    default:
                        return false;
                }
                if (weaponSlot.equals("slot 1")){
                    prefsEditor.putString(WEAPON_SLOT1_KEY, "none");
                } else if (weaponSlot.equals("slot 2")){
                    prefsEditor.putString(WEAPON_SLOT2_KEY, "none");
                }
                dialog.mSelectedWeapon = "";
                weaponImage.setImageResource(R.drawable.none);
                bombIcon.setColorFilter(Color.WHITE);
                spyIcon.setColorFilter(Color.WHITE);
                interfIcon.setColorFilter(Color.WHITE);
                int currentBombText = Integer.parseInt(bombInventoryText.getText().toString());
                if (currentBombText<bombCount) {
                    bombInventoryText.setText(String.valueOf(currentBombText + 1));
                }
                int currentSpyText = Integer.parseInt(spyInventoryText.getText().toString());
                if (currentSpyText<spyCount) {
                    spyInventoryText.setText(String.valueOf(currentSpyText + 1));
                }
                int currentInterfText = Integer.parseInt(interfInventoryText.getText().toString());
                if (currentInterfText<interfCount){
                    interfInventoryText.setText(String.valueOf(currentInterfText+1));
                }
                return true;
            }
        };

        weaponSlot1.setOnClickListener(weaponSlotClickListener);
        weaponSlot1.setOnLongClickListener(weaponSlotLongListener);
        weaponSlot2.setOnClickListener(weaponSlotClickListener);
        weaponSlot2.setOnLongClickListener(weaponSlotLongListener);
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