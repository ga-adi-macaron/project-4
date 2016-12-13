package com.scottlindley.sudokuapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PuzzleActivity extends AppCompatActivity implements PuzzleSolver.OnSolveFinishedListener{
    private static final String TAG = "PuzzleActivity";
    private DBHelper mDBHelper;
    private SudokuGridLayout mBoardView;
    private TextView mScoreView;
    private int[] mKey, mSolution, mUserAnswers;
    private int mSelectedNum, mScore, mStrikes;
    private ArrayList<TextView> mChoiceTiles;
    private PuzzleSolver mPuzzleSolver;
    private CountDownTimer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        mDBHelper = DBHelper.getInstance(this);

        setUpScoreCard();

        getPuzzleKey();

        mPuzzleSolver = new PuzzleSolver(mKey, PuzzleActivity.this);

        mUserAnswers = mKey;

        createCells();

        setUpChoiceTiles();

    }

    /**
     * Grabs one random puzzle's key of the selected difficulty.
     * The difficulty is found in the received intent from the MainMenuActivity.
     * Next the DBHelper is asked to grab on puzzle of said difficulty using one of its
     * 'get{difficulty}Puzzle()' methods.
     */
    private void getPuzzleKey(){
        Intent receivedIntent = getIntent();
        String difficulty = receivedIntent.getStringExtra(MainMenuActivity.DIFFICULTY_INTENT_KEY);
        Puzzle puzzle;
        switch (difficulty){
            case "easy":
                puzzle = mDBHelper.getEasyPuzzle();
                break;
            case "medium":
                puzzle = mDBHelper.getMediumPuzzle();
                break;
            case "hard":
                puzzle = mDBHelper.getHardPuzzle();
                break;
            case "expert":
                puzzle = mDBHelper.getExpertPuzzle();
                break;
            default: puzzle = null;
        }

        if (puzzle != null){
            mKey = puzzle.getKeyIntArray();
        } else {
            finish();
            Toast.makeText(this, "ERROR, PUZZLE NOT FOUND", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sets the starting score and begins a count down that subtracts 1 point from the score
     * with each 1 second tick.
     */
    private void setUpScoreCard(){
        mStrikes = 0;
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
                endGame(false);
            }
        }.start();
    }

    /**
     * The 81 TextView cells of the puzzle are created and inserted programatically. This
     * allows this activity's xml file to be much smaller.
     * Each cell is given an ID corresponding to it's position in the puzzle.
     * (first cell ID: 0 - last cell ID: 80)
     */
    private void createCells(){
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

    /**
     * Defines the actions taken upon clicking any one cell in the puzzle.
     * If the cell is empty, the click is taken as user attempting to assign a value to this cell.
     * If the cell isn't empty AND the number in that cell is still an input option (input options
     * that are no longer available are not visible to the user),
     * then set the selected number{@link #mSelectedNum} to the value found in that cell.
     * This is done by performing the Choice Tile's onClick.
     * @param cell
     */
    private void setCellClickListener(final TextView cell){
        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mKey[cell.getId()] == 0){
                    checkCellInput(cell);
                } else {
                    int number = Integer.parseInt(cell.getText().toString());
                    if(mChoiceTiles.get(number-1).getVisibility() == View.VISIBLE) {
                        mChoiceTiles.get(number - 1).performClick();
                    }
                }
            }
        });
    }

    /**
     * This method is called once a user inputs a value into the cell. The input is checked against
     * the puzzle's solution and actions are carried out accordingly.
     * @param cell
     */
    private void checkCellInput(TextView cell){
        if (mSelectedNum != 0) {
            if (mSolution[cell.getId()] == mSelectedNum) {
                //Choice was right
                cell.setText(String.valueOf(mSelectedNum));
                cell.setTextColor(getResources().getColor(R.color.colorAccent));
                mUserAnswers[cell.getId()] = Integer.parseInt(cell.getText().toString());

                /*
                If the user has found all instances of this number in the puzzle,
                remove that number down in the choice tiles.
                 */
                int numberCounter = 0;
                for (int i=0; i<mUserAnswers.length; i++){
                    if (mSelectedNum == mUserAnswers[i]){
                        numberCounter++;
                    }
                }
                if (numberCounter == 9){
                    ((CardView)mChoiceTiles.get(mSelectedNum-1).getParent())
                            .setVisibility(View.INVISIBLE);
                }
                checkForWin();
            } else {
                //Choice was wrong
                cell.setText("");
                mScore = mScore - 1000;
                if (mScore > 0) {
                    mScoreView.setText("Score: " + String.valueOf(mScore));
                }
                //Vibrate the phone to give a negative feedback
                ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(200);

                mStrikes++;
                //Tint the puzzle's color to indicate visually how many strikes the user has.
                switch (mStrikes){
                    case 1:
                        findViewById(R.id.strike_layer).setAlpha(0.25f);
                        break;
                    case 2:
                        findViewById(R.id.strike_layer).setAlpha(0.50f);
                        break;
                    case 3:
                        findViewById(R.id.strike_layer).setAlpha(0.75f);
                        //When the user has three strikes, the game is over
                        endGame(false);
                        break;
                    default:
                }
            }
        }
    }

    /**
     * Sets up the input selection tiles at the bottom of the screen
     */
    private void setUpChoiceTiles() {
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

    private void checkForWin(){
        boolean isCorrect = true;
        for (int i=0; i<81; i++){
            if (mUserAnswers[i] != mSolution[i]){
                isCorrect = false;
            }
        }
        if (isCorrect){
            mTimer.cancel();
            endGame(true);
        }
    }

    private void endGame(boolean win){
        for (TextView tile : mChoiceTiles){
            ((CardView)tile.getParent()).setVisibility(View.INVISIBLE);
            mTimer.cancel();
        }
        if(win){
            Toast.makeText(this, "You Win!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "GameOver", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Interface method called once the PuzzleSolver has finished solving the puzzle
     */
    @Override
    public void grabSolution() {
        mSolution = mPuzzleSolver.getSolution();
        (findViewById(R.id.loading_wheel)).setVisibility(View.INVISIBLE);
    }
}
