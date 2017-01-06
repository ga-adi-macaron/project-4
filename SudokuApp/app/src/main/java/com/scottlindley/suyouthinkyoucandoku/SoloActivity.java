package com.scottlindley.suyouthinkyoucandoku;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class SoloActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "SoloActivity";
    public static final String DIFFICULTY_INTENT_KEY = "difficulty";
    public static final String PUZZLE_KEY_INTENT_KEY = "puzzle key";
    public static final int SOLO_PUZZLE_REQUEST_CODE = 5667;

    private TextView mEasyText, mMediumText, mHardText, mExpertText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_solo);

        getSupportActionBar().hide();

        mEasyText = (TextView)findViewById(R.id.easyText);
        mMediumText = (TextView)findViewById(R.id.mediumText);
        mHardText = (TextView)findViewById(R.id.hardText);
        mExpertText = (TextView)findViewById(R.id.expertText);

        mEasyText.setOnClickListener(this);
        mMediumText.setOnClickListener(this);
        mHardText.setOnClickListener(this);
        mExpertText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        DBHelper helper = DBHelper.getInstance(this);
        String difficulty;
        String stringKey;
        String selectionText = ((TextView)view).getText().toString();
        switch(selectionText){
            case "Easy":
                difficulty = "easy";
                stringKey = helper.getEasyPuzzle().getKeyJSONArray().toString();
                break;
            case "Medium":
                difficulty = "medium";
                stringKey = helper.getMediumPuzzle().getKeyJSONArray().toString();
                break;
            case "Hard":
                difficulty = "hard";
                stringKey = helper.getHardPuzzle().getKeyJSONArray().toString();
                break;
            case "Expert":
                difficulty = "expert";
                stringKey = helper.getExpertPuzzle().getKeyJSONArray().toString();
                break;
            default:
                difficulty = "error";
                stringKey = "error";
        }
        Intent intent = new Intent(SoloActivity.this, SoloPuzzleActivity.class);
        intent.putExtra(DIFFICULTY_INTENT_KEY, difficulty);
        intent.putExtra(PUZZLE_KEY_INTENT_KEY, stringKey);

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
                Pair.create(findViewById(R.id.activity_solo), getString(R.string.background_transition));
        ActivityOptions options =
                ActivityOptions.makeSceneTransitionAnimation(
                        SoloActivity.this, pair1, pair2, pair3, pair4, pair5, backgroundPair);
        startActivityForResult(intent, SOLO_PUZZLE_REQUEST_CODE, options.toBundle());
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
        super.onBackPressed();
    }
}
