package com.scottlindley.sudokuapp;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Scott Lindley on 12/12/2016.
 */

public class PuzzleRefreshService extends JobService{
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Puzzles");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
