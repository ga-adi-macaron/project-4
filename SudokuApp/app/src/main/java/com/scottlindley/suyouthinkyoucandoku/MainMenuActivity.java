package com.scottlindley.suyouthinkyoucandoku;

import android.app.ActivityOptions;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.ChangeTransform;
import android.transition.Explode;
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
        getWindow().setExitTransition(new Explode());

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

        DBHelper mDbHelper = DBHelper.getInstance(this);
        boolean hasPuzzlesLocally = false;
        if (mDbHelper.getAllPuzzles() != null){
            hasPuzzlesLocally = true;
        }
        checkForNewPuzzles(hasPuzzlesLocally);
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
                Pair<View, String> backgroundPair =
                        Pair.create(findViewById(R.id.main_menu_acivity), getString(R.string.background_transition));
                ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation(
                                MainMenuActivity.this, pair1, pair2, pair3, pair4, backgroundPair);
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
                Pair<View, String> pair1 =
                        Pair.create(findViewById(R.id.stats_card), getString(R.string.transition4));
                Pair<View, String> pair2 =
                        Pair.create(findViewById(R.id.solo_card), getString(R.string.transition1));
                Pair<View, String> pair3 =
                        Pair.create(findViewById(R.id.race_card), getString(R.string.transition2));
                Pair<View, String> pair4 =
                        Pair.create(findViewById(R.id.armory_card), getString(R.string.transition3));
                Pair<View, String> backgroundPair =
                        Pair.create(findViewById(R.id.main_menu_acivity), getString(R.string.background_transition));

                ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation(
                                MainMenuActivity.this, pair1, pair2, pair3, pair4, backgroundPair);
                startActivity(new Intent(MainMenuActivity.this, StatsActivity.class), options.toBundle());
            }
        });

        mArmoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair<View, String> pair1 =
                        Pair.create(findViewById(R.id.stats_card), getString(R.string.transition4));
                Pair<View, String> pair2 =
                        Pair.create(findViewById(R.id.solo_card), getString(R.string.transition1));
                Pair<View, String> pair3 =
                        Pair.create(findViewById(R.id.race_card), getString(R.string.transition2));
                Pair<View, String> pair4 =
                        Pair.create(findViewById(R.id.armory_card), getString(R.string.transition3));
                Pair<View, String> pair5 =
                        Pair.create(findViewById(R.id.tinyActivity), getString(R.string.activityExplode));
                Pair<View, String> backgroundPair =
                        Pair.create(findViewById(R.id.main_menu_acivity), getString(R.string.background_transition));
                ActivityOptions options =
                ActivityOptions.makeSceneTransitionAnimation(
                        MainMenuActivity.this, pair1, pair2, pair3, pair4, pair5, backgroundPair);
                ActivityCompat.startActivity(MainMenuActivity.this,
                        new Intent(MainMenuActivity.this, ArmoryActivity.class), options.toBundle());
            }
        });
    }

    /**
     * Creates job services that download puzzles from the remote database. The required network
     * type of the service depends upon the boolean parameter.
     * @param hasPuzzlesLocally denotes whether or not the local database contains any puzzles already.
     */
    private void checkForNewPuzzles(boolean hasPuzzlesLocally){
        NetworkConnectivityChecker networkChecker = new NetworkConnectivityChecker(MainMenuActivity.this);
        if (networkChecker.isConnected()) {
            DBHelper.getInstance(this).setUpBroadcastReceiver();
            JobInfo.Builder puzzleRefreshJob = new JobInfo.Builder(PUZZLE_REFRESH_JOB_ID,
                    new ComponentName(this, PuzzleRefreshService.class));
            if (hasPuzzlesLocally) {
                puzzleRefreshJob.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
            } else {
                puzzleRefreshJob.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
                launchLoadingPuzzleDialog();
            }
            JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            scheduler.schedule(puzzleRefreshJob.build());
        } else {
            Toast.makeText(this, "Please connect to network to download puzzles.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void launchLoadingPuzzleDialog(){
        View dialogView = getLayoutInflater().inflate(R.layout.loading_puzzle_dialog, null);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        //User cannot cancel until service has finished
                    }
                })
                .setView(dialogView)
                .create();
        dialog.show();

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                dialog.dismiss();
            }
        };

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(receiver, new IntentFilter(DBHelper.PUZZLE_LOADING_DONE_INTENT));
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
                    public void onClick(DialogInterface dialogInterface, int i) {}});
        setUpDialogViews(dialogView, dialog);
        dialog.setPositiveButton("okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String[] weapons = new String[]{dialog.mWeaponSlot1, dialog.mWeaponSlot2};

                Intent intent = new Intent(MainMenuActivity.this, RaceActivity.class);
                intent.putExtra(RACE_INTENT_EXTRA, weapons);
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
        final CardView weaponSlot2 = (CardView)dialogView.findViewById(R.id.weapon2);

        final int bombCount = prefs.getInt(ArmoryActivity.BOMB_COUNT_KEY, 0);
        final int spyCount = prefs.getInt(ArmoryActivity.SPY_COUNT_KEY, 0);
        final int interfCount = prefs.getInt(ArmoryActivity.INTERFERENCE_COUNT_KEY, 0);

        final TextView bombInventoryText = (TextView)dialogView.findViewById(R.id.bomb_inventory_count);
        final TextView spyInventoryText = (TextView)dialogView.findViewById(R.id.spy_inventory_count);
        final TextView interfInventoryText = (TextView)dialogView.findViewById(R.id.interference_inventory_count);

        bombInventoryText.setText(String.valueOf(bombCount));
        spyInventoryText.setText(String.valueOf(spyCount));
        interfInventoryText.setText(String.valueOf(interfCount));

        dialogView.findViewById(R.id.bomb_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(bombInventoryText.getText().toString()) > 0){
                    interfIcon.setColorFilter(Color.WHITE);
                    spyIcon.setColorFilter(Color.WHITE);
                    bombIcon.setColorFilter(getResources().getColor(R.color.colorAccent));
                    dialog.mSelectedWeapon = "bomb";
                }
            }
        });

        dialogView.findViewById(R.id.spy_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(spyInventoryText.getText().toString()) > 0){
                    bombIcon.setColorFilter(Color.WHITE);
                    interfIcon.setColorFilter(Color.WHITE);
                    spyIcon.setColorFilter(getResources().getColor(R.color.colorAccent));
                    dialog.mSelectedWeapon = "spy";
                }
            }
        });

        dialogView.findViewById(R.id.interference_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(interfInventoryText.getText().toString()) > 0){
                    bombIcon.setColorFilter(Color.WHITE);
                    spyIcon.setColorFilter(Color.WHITE);
                    interfIcon.setColorFilter(getResources().getColor(R.color.colorAccent));
                    dialog.mSelectedWeapon = "interference";
                }
            }
        });

        View.OnClickListener weaponSlotClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView weaponImage;
                String weaponSlot, weaponCurrentlyInSlot;
                //Determine which weapon slot was clicked
                switch (view.getId()){
                    case R.id.weapon1:
                        weaponImage = (ImageView)dialogView.findViewById(R.id.weapon1_image);
                        weaponSlot = "slot 1";
                        weaponCurrentlyInSlot = dialog.mWeaponSlot1;
                        break;
                    case R.id.weapon2:
                        weaponImage = (ImageView)dialogView.findViewById(R.id.weapon2_image);
                        weaponSlot = "slot 2";
                        weaponCurrentlyInSlot = dialog.mWeaponSlot2;
                        break;
                    default:
                        return;
                }

                String selectedWeapon = dialog.mSelectedWeapon;

                if(!selectedWeapon.equals("") && !weaponCurrentlyInSlot.equals(selectedWeapon)) {
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

                    //Set the weapon into the slot and save what weapon if any was there before
                    String weaponToAddBack = "";
                    if (weaponSlot.equals("slot 1")){
                        weaponToAddBack = dialog.mWeaponSlot1;
                        if (!selectedWeapon.equals(dialog.mWeaponSlot1)) {
                            dialog.mWeaponSlot1 = selectedWeapon;
                        }
                    } else if (weaponSlot.equals("slot 2")){
                        weaponToAddBack = dialog.mWeaponSlot2;
                        if (!selectedWeapon.equals(dialog.mWeaponSlot2)) {
                            dialog.mWeaponSlot2 = selectedWeapon;
                        }
                        dialog.mWeaponSlot2 = selectedWeapon;
                    }

                    //Add the old weapon back into the inventory count
                    if (!selectedWeapon.equals(weaponToAddBack)) {
                        switch (weaponToAddBack) {
                            case "bomb":
                                bombInventoryText.setText(String.valueOf(
                                        Integer.parseInt(bombInventoryText.getText().toString()) + 1));
                                break;
                            case "spy":
                                spyInventoryText.setText(String.valueOf(
                                        Integer.parseInt(spyInventoryText.getText().toString())+ 1));
                                break;
                            case "interference":
                                interfInventoryText.setText(String.valueOf(
                                        Integer.parseInt(interfInventoryText.getText().toString())+ 1));
                                break;
                        }
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
                    dialog.mWeaponSlot1 = "none";
                } else if (weaponSlot.equals("slot 2")){
                    dialog.mWeaponSlot2 = "none";
                }
                dialog.mSelectedWeapon = "";
                weaponImage.setImageResource(R.drawable.empty);
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