package com.scottlindley.suyouthinkyoucandoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SoloActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String DIFFICULTY_INTENT_KEY = "difficulty";
    public static final String PUZZLE_KEY_INTENT_KEY = "puzzle key";

    private TextView mEasyText, mMediumText, mHardText, mExpertText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo);


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
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
        super.onBackPressed();
    }
}
