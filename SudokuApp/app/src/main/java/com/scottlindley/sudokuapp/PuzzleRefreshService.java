package com.scottlindley.sudokuapp;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Scott Lindley on 12/12/2016.
 */

public class PuzzleRefreshService extends JobService{
    private static final String TAG = "PuzzleRefreshService";
    public static final String PUZZLE_REFRESH_SERVICE = "puzzle refresh service";
    public static final String KEYS_INTENT_KEY = "keys";
    public static final String DIFFICULTIES_INTENT_KEY = "difficulties";
    public static final String NUMBER_OF_PUZZLES_INTENT_KEY = "num puzzles";
    private JSONArray[] mKeys;
    private String[] mDifficulties;
    private boolean easyDone, mediumDone, hardDone, expertDone;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Puzzles");
        grabPuzzles(ref.child("easy"), "easy");
        grabPuzzles(ref.child("medium"), "medium");
        grabPuzzles(ref.child("hard"), "hard");
        grabPuzzles(ref.child("expert"), "expert");

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private void grabPuzzles(DatabaseReference ref, final String difficulty){
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                adoptLittlePuzzleChildren(dataSnapshot, difficulty);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adoptLittlePuzzleChildren(dataSnapshot, difficulty);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adoptLittlePuzzleChildren(dataSnapshot, difficulty);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                adoptLittlePuzzleChildren(dataSnapshot, difficulty);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+databaseError.getDetails());
                Log.d(TAG, "onCancelled: "+databaseError.getMessage());
                Log.d(TAG, "onCancelled: "+databaseError.getCode());
            }
        });
    }

    private void adoptLittlePuzzleChildren(DataSnapshot dataSnapshot, String difficulty){
        List<Puzzle> puzzles = new ArrayList<>();
        for (DataSnapshot snap : dataSnapshot.getChildren()){
            Puzzle puzzle = snap.getValue(Puzzle.class);
            puzzle.setDifficulty(difficulty);
            puzzles.add(puzzle);
        }
        if (puzzles.size() <= 10) {
            for (int i=0; i<puzzles.size(); i++){
                mKeys[i] = puzzles.get(i).getKey();
                mDifficulties[i] = puzzles.get(i).getDifficulty();
            }
        } else {
            //If there are more than 5 in the remote DB, add 5 random puzzles
            Collections.shuffle(puzzles);
            for (int i=0; i<10; i++){
                mKeys[i] = puzzles.get(i).getKey();
                mDifficulties[i] = puzzles.get(i).getDifficulty();
            }
        }
        //Indicates that this difficulty has finished processing the new puzzles
        switch (difficulty){
            case "easy":
                easyDone = true;
                break;
            case "medium":
                mediumDone = true;
                break;
            case "hard":
                hardDone = true;
                break;
            case "expert":
                expertDone = true;
                break;
        }
        //If all are finished, send the puzzle data over to the DBHelper
        //Will always be triggered by the last difficulty to finish.
        if (easyDone && mediumDone && hardDone && expertDone){
            broadcastIntent();
        }
    }

    private void broadcastIntent(){
        Intent intent = new Intent(PUZZLE_REFRESH_SERVICE);

        for (int i=0; i<mKeys.length; i++) {
            intent.putExtra(KEYS_INTENT_KEY+i, mKeys[i].toString());
            intent.putExtra(DIFFICULTIES_INTENT_KEY+i, mDifficulties[i]);
        }

        intent.putExtra(NUMBER_OF_PUZZLES_INTENT_KEY, mKeys.length);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        jobFinished(null, false);
    }
}
