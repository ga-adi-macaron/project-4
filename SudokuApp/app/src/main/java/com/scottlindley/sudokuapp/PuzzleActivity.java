package com.scottlindley.sudokuapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PuzzleActivity extends AppCompatActivity implements PuzzleSolver.OnSolveFinishedListener{
    private SudokuGridLayout mBoardView;
    private TextView mScoreView;
    private int[] mKey, mSolution, mUserAnswers;
    private int mSelectedNum, mScore;
    private ArrayList<TextView> mChoiceTiles;
    private PuzzleSolver mPuzzleSolver;
    private CountDownTimer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        mScoreView = (TextView)findViewById(R.id.score_text);
        mScore = 15001;
        mScoreView.setText(String.valueOf(mScore));

        mTimer = new CountDownTimer(TimeUnit.MINUTES.toMillis(20), 1000){
            @Override
            public void onTick(long l) {
                mScore--;
                if (mScore <= 0){
                    onFinish();
                }
                mScoreView.setText("Score: "+String.valueOf(mScore));
            }

            @Override
            public void onFinish() {
                mScore = 0;
                mScoreView.setText("Score: 0");
                Toast.makeText(PuzzleActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                cancel();
            }
        }.start();


        mKey = new int[81];
        mKey[0] = 1;
        mKey[5] = 9;
        mKey[9] = 5;
        mKey[10] = 3;
        mKey[15] = 4;
        mKey[17] = 1;
        mKey[19] = 9;
        mKey[20] = 7;
        mKey[23] = 8;
        mKey[27] = 2;
        mKey[29] = 4;
        mKey[30] = 7;
        mKey[34] = 5;
        mKey[40] = 4;
        mKey[46] = 5;
        mKey[50] = 1;
        mKey[51] = 3;
        mKey[53] = 4;
        mKey[57] = 5;
        mKey[60] = 6;
        mKey[61] = 8;
        mKey[63] = 9;
        mKey[65] = 3;
        mKey[70] = 2;
        mKey[71] = 7;
        mKey[75] = 2;
        mKey[80] = 3;

        mPuzzleSolver = new PuzzleSolver(mKey, this);

        mUserAnswers = mKey;

        createCells();

        setUpChoiceTiles();
    }


    public void createCells(){
        mBoardView = (SudokuGridLayout) findViewById(R.id.board_view);

        for (int i=0; i<81; i++){
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.setMargins(0,0,0,0);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);

            TextView cell = new TextView(this);
            cell.setId(i);
            cell.setLayoutParams(params);
            cell.setTextSize(22);
            cell.setGravity(Gravity.CENTER);
            cell.setTextColor(Color.BLACK);
            cell.setTypeface(null, Typeface.BOLD);

            if (mKey[i] != 0){
                cell.setText(String.valueOf(mKey[i]));
            }

            setCellClickListener(cell);

            mBoardView.addView(cell);
        }
    }

    public void setCellClickListener(final TextView cell){
        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mKey[cell.getId()] == 0){
                    checkCellInput(cell);
                } else {
                    int number = Integer.parseInt(cell.getText().toString());
                    mChoiceTiles.get(number - 1).performClick();
                }
            }
        });
    }

    public void checkCellInput(TextView cell){
        if (mSelectedNum != 0) {
            if (mSolution[cell.getId()] == mSelectedNum) {
                //Choice was right
                cell.setText(String.valueOf(mSelectedNum));
                cell.setTextColor(getResources().getColor(R.color.colorAccent));
                mUserAnswers[cell.getId()] = Integer.parseInt(cell.getText().toString());

                int numberCounter = 0;
                for (int i=0; i<mUserAnswers.length; i++){
                    if (mSelectedNum == mUserAnswers[i]){
                        numberCounter++;
                    }
                }
                if (numberCounter == 9){
                    mChoiceTiles.get(mSelectedNum-1).setVisibility(View.INVISIBLE);
                }
                checkForWin();
            } else {
                //Choice was wrong
                cell.setText("");
                mScore = mScore - 1000;
                if (mScore > 0) {
                    mScoreView.setText("Score: " + String.valueOf(mScore));
                }
                ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(200);
            }
        }
    }

    public void setUpChoiceTiles() {
        mChoiceTiles = new ArrayList<>();
        mChoiceTiles.add((TextView) findViewById(R.id.tile1));
        mChoiceTiles.add((TextView) findViewById(R.id.tile2));
        mChoiceTiles.add((TextView) findViewById(R.id.tile3));
        mChoiceTiles.add((TextView) findViewById(R.id.tile4));
        mChoiceTiles.add((TextView) findViewById(R.id.tile5));
        mChoiceTiles.add((TextView) findViewById(R.id.tile6));
        mChoiceTiles.add((TextView) findViewById(R.id.tile7));
        mChoiceTiles.add((TextView) findViewById(R.id.tile8));
        mChoiceTiles.add((TextView) findViewById(R.id.tile9));

        for (TextView tile : mChoiceTiles){
            tile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSelectedNum = Integer.parseInt(((TextView)view).getText().toString());
                    for (TextView tile: mChoiceTiles){
                        tile.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        tile.setTextColor(Color.WHITE);
                    }
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    ((TextView)view).setTextColor(Color.BLACK);
                    for (int i=0; i<81; i++){
                        if (mUserAnswers[i] == mSelectedNum){
                            ((TextView)mBoardView.findViewById(i))
                                    .setTextColor(getResources().getColor(R.color.colorAccent));
                        } else {
                            ((TextView)mBoardView.findViewById(i))
                                    .setTextColor(Color.BLACK);
                        }
                    }
                }
            });
        }
    }

    public void checkForWin(){
        boolean isCorrect = true;
        for (int i=0; i<81; i++){
            if (mUserAnswers[i] != mSolution[i]){
                isCorrect = false;
            }
        }
        if (isCorrect){
            //TODO: END GAME
            Toast.makeText(this, "You Win!", Toast.LENGTH_SHORT).show();
            mTimer.cancel();
        }
    }

    @Override
    public void grabSolution() {
        mSolution = mPuzzleSolver.getSolution();
        (findViewById(R.id.loading_wheel)).setVisibility(View.INVISIBLE);

    }
}
