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
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott Lindley on 12/12/2016.
 */

public class PuzzleRefreshService extends JobService {
    private static final String TAG = "PuzzleRefreshService";
    public static final String PUZZLE_REFRESH_SERVICE = "puzzle refresh service";
    public static final String KEYS_INTENT_KEY = "keys";
    public static final String DIFFICULTIES_INTENT_KEY = "difficulties";

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

    private void grabPuzzles(DatabaseReference ref, final String difficulty) {
        //TODO: grab number or puzzles in each difficulty. Use that to send all puzzles in one broadcast
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                adoptLittlePuzzleChild(dataSnapshot, difficulty);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getDetails());
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                Log.d(TAG, "onCancelled: " + databaseError.getCode());
            }
        });
    }

    private void adoptLittlePuzzleChild(DataSnapshot dataSnapshot, String difficulty) {
        Puzzle puzzle = new Puzzle();
        List<Long> longKey = ((List<Long>) dataSnapshot.getValue());
        List<Integer> intKey = new ArrayList<>();
        for (long longNum: longKey){
            intKey.add((int)(long)longNum);
        }
        puzzle.setKey(intKey);
        puzzle.setDifficulty(difficulty);
        JSONArray jsonKey = puzzle.getKeyJSONArray();
        broadcastIntent(jsonKey, difficulty);
    }

    private void broadcastIntent(JSONArray jsonKey, String difficulty) {
        Intent intent = new Intent(PUZZLE_REFRESH_SERVICE);

        intent.putExtra(KEYS_INTENT_KEY, jsonKey.toString());
        intent.putExtra(DIFFICULTIES_INTENT_KEY, difficulty);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
