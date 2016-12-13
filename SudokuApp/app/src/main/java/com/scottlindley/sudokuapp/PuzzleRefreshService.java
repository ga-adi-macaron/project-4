package com.scottlindley.sudokuapp;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    List<List<String>> mKeys;
    List<String> mDifficulties;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Puzzles");
        grabEasyPuzzles(ref.child("easy"));
        grabHardPuzzles(ref.child("medium"));
        grabHardPuzzles(ref.child("hard"));
        grabExpertPuzzles(ref.child("expert"));

        Intent intent = new Intent(PUZZLE_REFRESH_SERVICE);


        //TODO: THIS IS GETTING MESSY... FIX
        for (int i=0; )
        String[][] keysArr = new String[mKeys.size()][mKeys.get(0).size()];

        intent.putExtra(KEYS_INTENT_KEY, mKeys.toArray(keysArr));

        jobFinished(jobParameters, false);

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private void grabEasyPuzzles(DatabaseReference easyRef){
        easyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Puzzle> easyPuzzles = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    Puzzle puzzle = snap.getValue(Puzzle.class);
                    puzzle.setDifficulty("easy");
                    easyPuzzles.add(puzzle);
                }
                if (easyPuzzles.size() <= 5) {
                    for (int i=0; i<easyPuzzles.size(); i++){
                        mKeys.add(easyPuzzles.get(i).getKey());
                        mDifficulties.add(easyPuzzles.get(i).getDifficulty());
                    }
                } else {
                    //If there are more than 5 in the remote DB, add 5 random puzzles
                    Collections.shuffle(easyPuzzles);
                    for (int i=0; i<5; i++){
                        mKeys.add(easyPuzzles.get(i).getKey());
                        mDifficulties.add(easyPuzzles.get(i).getDifficulty());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+databaseError.getDetails());
                Log.d(TAG, "onCancelled: "+databaseError.getMessage());
                Log.d(TAG, "onCancelled: "+databaseError.getCode());
            }
        });
    }

    private void grabMediumPuzzles(DatabaseReference mediumRef){
        mediumRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Puzzle> mediumPuzzles = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    Puzzle puzzle = snap.getValue(Puzzle.class);
                    puzzle.setDifficulty("medium");
                    mediumPuzzles.add(puzzle);
                }
                if (mediumPuzzles.size() <= 5) {
                    for (int i=0; i<mediumPuzzles.size(); i++){
                        mKeys.add(mediumPuzzles.get(i).getKey());
                        mDifficulties.add(mediumPuzzles.get(i).getDifficulty());
                    }
                } else {
                    //If there are more than 5 in the remote DB, add 5 random puzzles
                    Collections.shuffle(mediumPuzzles);
                    for (int i=0; i<5; i++){
                        mKeys.add(mediumPuzzles.get(i).getKey());
                        mDifficulties.add(mediumPuzzles.get(i).getDifficulty());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+databaseError.getDetails());
                Log.d(TAG, "onCancelled: "+databaseError.getMessage());
                Log.d(TAG, "onCancelled: "+databaseError.getCode());
            }
        });
    }

    private void grabHardPuzzles(DatabaseReference hardRef){
        hardRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Puzzle> hardPuzzles = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    Puzzle puzzle = snap.getValue(Puzzle.class);
                    puzzle.setDifficulty("hard");
                    hardPuzzles.add(puzzle);
                }
                if (hardPuzzles.size() <= 5) {
                    for (int i=0; i<hardPuzzles.size(); i++){
                        mKeys.add(hardPuzzles.get(i).getKey());
                        mDifficulties.add(hardPuzzles.get(i).getDifficulty());
                    }
                } else {
                    //If there are more than 5 in the remote DB, add 5 random puzzles
                    Collections.shuffle(hardPuzzles);
                    for (int i=0; i<5; i++){
                        mKeys.add(hardPuzzles.get(i).getKey());
                        mDifficulties.add(hardPuzzles.get(i).getDifficulty());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+databaseError.getDetails());
                Log.d(TAG, "onCancelled: "+databaseError.getMessage());
                Log.d(TAG, "onCancelled: "+databaseError.getCode());
            }
        });
    }

    private void grabExpertPuzzles(DatabaseReference expertRef){
        expertRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Puzzle> expertPuzzles = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    Puzzle puzzle = snap.getValue(Puzzle.class);
                    puzzle.setDifficulty("expert");
                    expertPuzzles.add(puzzle);
                }
                if (expertPuzzles.size() <= 5) {
                    for (int i=0; i<expertPuzzles.size(); i++){
                        mKeys.add(expertPuzzles.get(i).getKey());
                        mDifficulties.add(expertPuzzles.get(i).getDifficulty());
                    }
                } else {
                    //If there are more than 5 in the remote DB, add 5 random puzzles
                    Collections.shuffle(expertPuzzles);
                    for (int i=0; i<5; i++){
                        mKeys.add(expertPuzzles.get(i).getKey());
                        mDifficulties.add(expertPuzzles.get(i).getDifficulty());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+databaseError.getDetails());
                Log.d(TAG, "onCancelled: "+databaseError.getMessage());
                Log.d(TAG, "onCancelled: "+databaseError.getCode());
            }
        });
    }
}
