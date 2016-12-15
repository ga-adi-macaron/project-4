package com.scottlindley.suyouthinkyoucandoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {
    private static final String TAG = "MainMenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBAssetHelper dbAssetSetUp = new DBAssetHelper(MainMenuActivity.this);
        dbAssetSetUp.getReadableDatabase();

        startActivity(new Intent(MainMenuActivity.this, PuzzleActivity.class));
    }
}
