package com.scottlindley.suyouthinkyoucandoku;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.TimeUnit;

public class SoloPuzzleActivity extends BasePuzzleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        getSupportActionBar().hide();

        mDBHelper = DBHelper.getInstance(this);

        getPuzzleKey();
        initializeGame();
        for (TextView tile : mChoiceTiles){
            tile.setClickable(false);
        }
        for (TextView cell : mCellViews){
            cell.setVisibility(View.INVISIBLE);
        }
        ((TextView)findViewById(R.id.score_text)).setText("Loading puzzle...");
    }

    /**
     * Grabs one random puzzle's key of the selected difficulty.
     * The difficulty is found in the received intent from the MainMenuActivity.
     * Next the DBHelper is asked to grab on puzzle of said difficulty using one of its
     * 'get{difficulty}Puzzle()' methods.
     */
    private void getPuzzleKey(){
        Intent receivedIntent = getIntent();
        try {
            JSONArray jsonKey = new JSONArray(receivedIntent.getStringExtra(SoloActivity.PUZZLE_KEY_INTENT_KEY));
            mKey = new int[81];
            for (int i=0; i<jsonKey.length(); i++){
                mKey[i] = Integer.parseInt(jsonKey.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert dialog containing the result of the game (won or lost) as well as the
     * current game's stats compared against the user's high score/best time
     * @param win
     */
    @Override
    public void endGame(boolean win){
        for (TextView tile : mChoiceTiles){
            ((CardView)tile.getParent()).setVisibility(View.INVISIBLE);
            mTimer.cancel();
        }

        View dialogView = getLayoutInflater().inflate(R.layout.end_solo_game_dialog, null);
        TextView gameResultText = (TextView)dialogView.findViewById(R.id.game_result_text);
        TextView currentScore = (TextView)dialogView.findViewById(R.id.current_score);
        TextView highScore = (TextView)dialogView.findViewById(R.id.high_score);
        TextView currentTime = (TextView)dialogView.findViewById(R.id.current_time);
        TextView bestTime = (TextView)dialogView.findViewById(R.id.best_time);
        TextView lostMessage = (TextView)dialogView.findViewById(R.id.lost_game_message);

        if(win) {
            gameResultText.setText("You Win!");
            Stats stats = mDBHelper.getStats();
            if (mScore > stats.getHighscore()) {
                mDBHelper.updateHighScore(mScore);
                stats = mDBHelper.getStats();
            }
            if (mTime < stats.getBestTime() || stats.getBestTime() == 0) {
                mDBHelper.updateBestTime((int)(long) mTime);
                stats = mDBHelper.getStats();
            }
            currentScore.setText("Score: " + mScore);
            currentTime.setText("Time: " + mTimerView.getText());
            highScore.setText("High Score: " + stats.getHighscore());
            bestTime.setText("Best Time: " + DateUtils.formatElapsedTime((long)(int)stats.getBestTime()));
        } else {
            gameResultText.setText("You Lose");
            if (mStrikes > 2){
                lostMessage.setText("Three incorrect answers");
            } else {
                lostMessage.setText("Ran out of time! Maximum 20 minutes.");
            }
            lostMessage.setVisibility(View.VISIBLE);
        }
        mTimer.cancel();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton("okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setView(dialogView)
                .create();
        dialog.show();
    }

    @Override
    public void grabSolution() {
        super.grabSolution();
        for (TextView tile : mChoiceTiles){
            tile.setClickable(true);
        }
        for (TextView cell : mCellViews){
            cell.setVisibility(View.VISIBLE);
        }
        setUpScoreCard();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //If the score card has been set up, save the time and current score.
        if (mTimer!=null) {
            SharedPreferences.Editor prefsEditor =
                    getSharedPreferences(TIMER_PREFS_KEY, MODE_PRIVATE).edit();
            prefsEditor.putLong(TIME_PREFS_KEY, mTime);
            prefsEditor.putInt(SCORE_PREFS_KEY, mScore);
            prefsEditor.commit();
            mTimer.cancel();
        }
    }

    @Override
    protected void onResume() {
        SharedPreferences prefs = getSharedPreferences(TIMER_PREFS_KEY, MODE_PRIVATE);
        int score = prefs.getInt(SCORE_PREFS_KEY, -1);
        long time = prefs.getLong(TIME_PREFS_KEY, -1);
        final int scoreDropInterval = prefs.getInt(INTERVAL_PREFS_KEY, -1);

        //Check to verify these have been saved in onPause once before
        if (score != -1 && time != -1) {
            mScore = score;
            mTime = time;
            mTimer = new CountDownTimer(TimeUnit.MINUTES.toMillis(1000), 1000) {
                @Override
                public void onTick(long l) {
                    mScore = mScore - scoreDropInterval;
                    mTime++;
                    if (mScore <= 0) {
                        onFinish();
                    }
                     mTimerView.setText(DateUtils.formatElapsedTime(mTime));
                }

                @Override
                public void onFinish() {
                    mScore = 0;
                    endGame(false);
                }
            }.start();
        }
        super.onResume();
    }

    //Reset timer and score in preferences so they are not stored from one solo game to another
    @Override
    protected void onDestroy() {
        SharedPreferences.Editor prefsEdit =
                getSharedPreferences(TIMER_PREFS_KEY, MODE_PRIVATE).edit();
        prefsEdit.putLong(TIME_PREFS_KEY, -1);
        prefsEdit.putInt(SCORE_PREFS_KEY, -1);
        prefsEdit.commit();
        super.onDestroy();
    }

}
