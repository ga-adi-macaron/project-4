package com.scottlindley.suyouthinkyoucandoku;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott Lindley on 12/12/2016.
 */

public class PuzzleRefreshService extends JobService {
    public static final String PUZZLE_REFRESH_SERVICE = "puzzle refresh service";
    public static final String KEYS_INTENT_KEY = "keys";
    public static final String DIFFICULTIES_INTENT_KEY = "difficulties";
    public static final String PUZZLE_COUNT_INTENT_KEY = "puzzle count";
    private List<Puzzle> puzzles;
    private int totalPuzzleCount, easyCount, mediumCount, hardCount, expertCount;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                puzzles = new ArrayList<>();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Puzzles");
                grabPuzzles(ref.child("easy"), "easy");
                grabPuzzles(ref.child("medium"), "medium");
                grabPuzzles(ref.child("hard"), "hard");
                grabPuzzles(ref.child("expert"), "expert");
                return null;
            }
        };
        task.execute();



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
                int count = (int)(long)dataSnapshot.getChildrenCount();
                for (DataSnapshot childSnap : dataSnapshot.getChildren()){
                    adoptLittlePuzzleChild(childSnap, difficulty);
                }
                switch (difficulty){
                    case "easy":
                        easyCount = count;
                        break;
                    case "medium":
                        mediumCount = count;
                        break;
                    case "hard":
                        hardCount = count;
                        break;
                    case "expert":
                        expertCount = count;
                        break;
                }

                //Only check if it's time to broadcast when all four counts have been initialized
                if(easyCount>0 && mediumCount>0 && hardCount>0 && expertCount>0) {
                    totalPuzzleCount = easyCount + mediumCount + hardCount + expertCount;

                    if (puzzles.size() == totalPuzzleCount) {
                        broadcastIntent(puzzles);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
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
        puzzles.add(puzzle);
    }

    private void broadcastIntent(List<Puzzle> puzzles) {
        Intent intent = new Intent(PUZZLE_REFRESH_SERVICE);

        for (int i=0; i<puzzles.size(); i++) {
            intent.putExtra(KEYS_INTENT_KEY+i, puzzles.get(i).getKeyJSONArray().toString());
            intent.putExtra(DIFFICULTIES_INTENT_KEY+i, puzzles.get(i).getDifficulty());
        }

        intent.putExtra(PUZZLE_COUNT_INTENT_KEY, puzzles.size());
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
