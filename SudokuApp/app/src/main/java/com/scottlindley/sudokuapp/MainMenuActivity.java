package com.scottlindley.sudokuapp;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

//        addPuzzle();



//        checkForNewPuzzles();
        //TODO: THIS DELETES DATA BEFORE RECEIVING NEW DATA.... FIX!

        
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

    private void transformAndPost(List<Integer> key, String difficulty) {
        ArrayList<Integer> key2 = new ArrayList<>();
        ArrayList<Integer> key3 = new ArrayList<>();
        ArrayList<Integer> key4 = new ArrayList<>();

        for (int i = 0; i < key.size(); i++) {
            if (key.get(i) != 0) {
                int shiftA = key.get(i) + 2;
                int shiftB = key.get(i) + 5;
                int shiftC = key.get(i) + 7;
                if (shiftA > 9) {
                    shiftA = shiftA - 9;
                }
                if (shiftB > 9) {
                    shiftB = shiftB - 9;
                }
                if (shiftC > 9) {
                    shiftC = shiftC - 9;
                }
                key2.add(shiftA);
                key3.add(shiftB);
                key4.add(shiftC);
            } else {
                key2.add(0);
                key3.add(0);
                key4.add(0);
            }
        }

        ArrayList<ArrayList<Integer>> transformedKeys = new ArrayList<>();
        transformedKeys.add((ArrayList<Integer>) key);
        transformedKeys.add(mirrorX(key));
        transformedKeys.add(mirrorY(key));
        transformedKeys.add(mirrorY(mirrorX(key)));
        transformedKeys.add(key2);
        transformedKeys.add(mirrorX(key2));
        transformedKeys.add(mirrorY(key2));
        transformedKeys.add(mirrorY(mirrorX(key2)));
        transformedKeys.add(key3);
        transformedKeys.add(mirrorX(key3));
        transformedKeys.add(mirrorY(key3));
        transformedKeys.add(mirrorY(mirrorX(key3)));
        transformedKeys.add(key4);
        transformedKeys.add(mirrorX(key4));
        transformedKeys.add(mirrorY(key4));
        transformedKeys.add(mirrorY(mirrorX(key4)));

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Puzzles");

        for (int i = 0; i < transformedKeys.size(); i++) {
            ref.child(difficulty).push().setValue(transformedKeys.get(i));
        }
    }

    private ArrayList<Integer> mirrorX(List<Integer> key){
        ArrayList<Integer> mirrored = new ArrayList<>();
        ArrayList<Integer> row = new ArrayList<>();
        for(int i=0; i<key.size(); i++){
            row.add(key.get(i));
            if (i%9==8){
                Collections.reverse(row);
                mirrored.addAll(row);
                row.clear();
            }
        }
        return mirrored;
    }

    private ArrayList<Integer> mirrorY(List<Integer> key){
        ArrayList<Integer> mirrored = new ArrayList<>();
        ArrayList<ArrayList<Integer>> rows = new ArrayList<>();
        ArrayList<Integer> row = new ArrayList<>();
        for(int i=0; i<key.size(); i++){
            row.add(key.get(i));
            if(i%9==8){
                ArrayList<Integer> addedRow = new ArrayList<>();
                addedRow.addAll(row);
                rows.add(addedRow);
                row.clear();
            }
        }
        Collections.reverse(rows);
        for(ArrayList<Integer> r : rows){
            mirrored.addAll(r);
        }
        return mirrored;
    }
}
