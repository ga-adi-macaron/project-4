package com.scottlindley.suyouthinkyoucandoku;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class SoloPuzzleActivity extends BasePuzzleActivity {
    private static final String TAG = "SoloPuzzleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        mDBHelper = DBHelper.getInstance(this);

        getPuzzleKey();
        initializeGame();
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

}
