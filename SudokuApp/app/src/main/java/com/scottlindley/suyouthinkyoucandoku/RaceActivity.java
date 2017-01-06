package com.scottlindley.suyouthinkyoucandoku;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RaceActivity extends BasePuzzleActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener, RoomUpdateListener {
    private static final String TAG = "RaceActivity";
    public static final int RC_SIGN_IN = 5667;
    public static final int RC_AUTOMATCH = 6980;
    public static final String STILL_CONNECTED_MESSAGE = "opponent still connected";
    public static final String OPPONENT_DISCONNECTED_MESSAGE = "opponent disconnected";
    public static final String OPPONENT_QUIT_MESSAGE = "opponent quit";
    public static final String TOO_MANY_GUESSES_MESSAGE = "too many guesses";
    public static final String OPPONENT_FINISHED_MESSAGE = "opponent finished";
    public static final String INTERFERENCE_MESSAGE = "interference";
    public static final String BOMB_MESSAGE = "Bomb";
    private boolean mOpponentQuit, mOpponentDisconnected, mOpponentTooManyGuesses, mOpponentFinished;
    private boolean mUserDisconnected;
    private String mRoomID;
    private int mNumberOfParticipants;
    private boolean mResolvingConnectionFailure, mStillConnected, mGameAlreadyEnded;
    private GoogleApiClient mGoogleApiClient;
    private String mWeapon1, mWeapon2, mWeaponSelected = "none";
    private boolean mWeapon1Clicked, mWeapon2Clicked;
    private int[] mOpponentCellsFilled = new int[81];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);
        getSupportActionBar().hide();

        mDBHelper = DBHelper.getInstance(this);

        (findViewById(R.id.loading_wheel)).setVisibility(View.INVISIBLE);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }

    /**
     * Takes the weapons that were armed before the race from shared preferences and assigns
     * them to the weapon
     * slots above the game board. Click listeners are then assigned to the weapons.
     */
    private void setUpWeapons(){
        SharedPreferences prefs =
                getSharedPreferences(MainMenuActivity.ARMED_WEAPONS_PREFS, MODE_PRIVATE);
        mWeapon1 = prefs.getString(MainMenuActivity.WEAPON_SLOT1_KEY, "none");
        mWeapon2 = prefs.getString(MainMenuActivity.WEAPON_SLOT2_KEY, "none");

        final ImageView weapon1Image = (ImageView)findViewById(R.id.weapon1_image);
        final ImageView weapon2Image = (ImageView)findViewById(R.id.weapon2_image);

        setWeaponImage(mWeapon1, weapon1Image);
        setWeaponImage(mWeapon2, weapon2Image);

        CardView weapon1Card = (CardView)findViewById(R.id.weapon1);
        CardView weapon2Card = (CardView)findViewById(R.id.weapon2);

        weapon1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWeapon1Clicked = !mWeapon1Clicked;
                if (!mWeapon1.equals("none") && mWeapon1Clicked == true){
                    weapon1Image.setColorFilter(getResources().getColor(R.color.colorAccent));
                    mWeapon2Clicked = false;
                    weapon2Image.setColorFilter(Color.WHITE);
                    adjustCardText(1);
                } else if (!mWeapon1.equals("none") && mWeapon1Clicked == false){
                    weapon1Image.setColorFilter(Color.WHITE);
                    mWeaponSelected = "none";
                    mTimerView.setText("Race!");
                }
            }
        });

        weapon2Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWeapon2Clicked = !mWeapon2Clicked;
                if (!mWeapon2.equals("none") && mWeapon2Clicked == true){
                    weapon2Image.setColorFilter(getResources().getColor(R.color.colorAccent));
                    mWeapon1Clicked = false;
                    weapon1Image.setColorFilter(Color.WHITE);
                    adjustCardText(2);
                } else if (!mWeapon2.equals("none") && mWeapon2Clicked == false){
                    weapon2Image.setColorFilter(Color.WHITE);
                    mWeaponSelected = "none";
                    mTimerView.setText("Race!");
                }
            }
        });
    }

    /**
     * Changes the text found above the weapons when a weapon is either selected or deselected.
     * @param weaponSlot is the location of the selected weapon (position 1 or position 2).
     */
    private void adjustCardText(int weaponSlot){
        String weapon = "none";
        if (weaponSlot == 1) {
            weapon = mWeapon1;
        } else if (weaponSlot == 2){
            weapon = mWeapon2;
        }
        switch (weapon) {
            case "bomb":
                mTimerView.setText("Long press a box!");
                break;
            case "spy":
                mTimerView.setText("Long press the board!");
                break;
            case "interference":
                mTimerView.setText("Long press the board!");
                break;
            case "none":
                mTimerView.setText("Race!");
        }
        mWeaponSelected = weapon;
    }

    /**
     * sets the appropriate image depending on the name of the weapon given.
     * @param weaponName is the name of the weapon.
     * @param weaponImage is the ImageView that will be assigned an icon.
     */
    private void setWeaponImage(String weaponName, ImageView weaponImage){
        switch (weaponName){
            case "bomb":
                weaponImage.setImageResource(R.drawable.explosion);
                break;
            case "spy":
                weaponImage.setImageResource(R.drawable.eye);
                break;
            case "interference":
                weaponImage.setImageResource(R.drawable.lightning);
                break;
            case "none":
                weaponImage.setImageResource(R.drawable.empty);
                break;
        }
    }

    /**
     * Override of the method found in BasePuzzle Activity. This version also sends messages to the
     * opponent. If correct, the user sends off the cell's id, or position on the board. If
     * incorrect and the user already has three strikes, send a message to the opponent saying the user
     * has lost.
     * @param cell
     */
    @Override
    public void checkCellInput(TextView cell) {
        //If it's correct then send the opponent a message
        if (mSelectedNum != 0) {
            if (mSolution[cell.getId()] == mSelectedNum) {
                byte[] data = String.valueOf(cell.getId()).getBytes();
                Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, data, mRoomID);
            }
        } else {
            if (mStrikes > 2) {
                byte[] data = TOO_MANY_GUESSES_MESSAGE.getBytes();
                Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, data, mRoomID);
            }
        }
        super.checkCellInput(cell);
    }

    /**
     * Displays an alert dialog containing the result of the game (won or lost) as well as the
     * current game's stats compared against the user's high score/best time
     * @param win
     */
    @Override
    public void endGame(boolean win) {
        if (!mGameAlreadyEnded) {
            mGameAlreadyEnded = true;
            for (TextView tile : mChoiceTiles) {
                ((CardView) tile.getParent()).setVisibility(View.INVISIBLE);
            }

            View dialogView = getLayoutInflater().inflate(R.layout.end_race_game_dialog, null);
            TextView gameResultText = (TextView) dialogView.findViewById(R.id.game_result_text);
            TextView resultDetailText = (TextView) dialogView.findViewById(R.id.lost_game_message);
            TextView racesWon = (TextView) dialogView.findViewById(R.id.races_won);
            TextView racesLost = (TextView) dialogView.findViewById(R.id.races_lost);
            TextView coinsWon = (TextView) dialogView.findViewById(R.id.won_coins);

            Stats stats = mDBHelper.getStats();
            if (win) {
                gameResultText.setText("You Win!");
                SharedPreferences prefs = getSharedPreferences(ArmoryActivity.ARMORY_SHARED_PREFS, MODE_PRIVATE);
                int coins = prefs.getInt(ArmoryActivity.COIN_COUNT_KEY, 0);
                coinsWon.setText("+5 coins for your victory!\nCoins: " + (coins + 5));
                coinsWon.setVisibility(View.VISIBLE);
                prefs.edit()
                        .putInt(ArmoryActivity.COIN_COUNT_KEY, coins + 5)
                        .commit();

                if (isCorrect) {
                    resultDetailText.setText("You completed the puzzle before the enemy");
                    byte[] data = OPPONENT_FINISHED_MESSAGE.getBytes();
                    Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, data, mRoomID);
                } else if (mOpponentTooManyGuesses) {
                    resultDetailText.setText("Your opponent guessed made too many incorrect guesses");
                } else if (mOpponentQuit) {
                    resultDetailText.setText("Your opponent quit");
                } else if (mOpponentDisconnected) {
                    resultDetailText.setText("Your opponent was disconnected");
                }
                resultDetailText.setVisibility(View.VISIBLE);
                mDBHelper.updateRacesWon(stats.getRacesWon() + 1);
                stats = mDBHelper.getStats();
            } else {
                //The game was lost
                gameResultText.setText("You Lose");
                mDBHelper.updateRacesLost(stats.getRacesLost() + 1);
                stats = mDBHelper.getStats();
                if (mStrikes > 2) {
                    resultDetailText.setText("Three incorrect answers");
                    byte[] data = "too many guesses".getBytes();
                    Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, data, mRoomID);
                } else if (mOpponentFinished) {
                    resultDetailText.setText("Your opponent has finished the puzzle");
                } else if (mUserDisconnected) {
                    resultDetailText.setText("You have disconnected from the game");
                }
                resultDetailText.setVisibility(View.VISIBLE);
            }
            mTimer.cancel();

            racesWon.setText("Races Won: " + stats.getRacesWon());
            racesLost.setText("Races Lost: " + stats.getRacesLost());

            checkForAcheivementUnlock(stats);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setPositiveButton("okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            RaceActivity.this.finish();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            RaceActivity.this.finish();
                        }
                    })
                    .setView(dialogView)
                    .create();
            if (!RaceActivity.this.isFinishing() && !RaceActivity.this.isDestroyed()) {
                dialog.show();
            }

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    /**
     * Checks to see if the user has unlocked an achievment.
     * @param stats are the user's updates stats that contain the number of races won.
     */
    private void checkForAcheivementUnlock(Stats stats){
        if (stats.getRacesWon() == 1){
            Games.Achievements.unlock(
                    mGoogleApiClient, getResources().getString(R.string.achievement1));
        }
        if (stats.getRacesWon() == 10){
            Games.Achievements.unlock(
                    mGoogleApiClient, getResources().getString(R.string.achievement2));
        }
        if (stats.getRacesWon() == 50){
            Games.Achievements.unlock(
                    mGoogleApiClient, getResources().getString(R.string.achievement3));
        }
        if (stats.getRacesWon() == 100){
            Games.Achievements.unlock(
                    mGoogleApiClient, getResources().getString(R.string.achievement4));
        }
        if (stats.getRacesWon() == 200){
            Games.Achievements.unlock(
                    mGoogleApiClient, getResources().getString(R.string.achievement5));
        }
    }

    /**
     * Helper method that takes a string and produces long value seed. The method then returns a
     * random integer from that seed. The connected room ID is used to ensure that the two players
     * grab the same 'random' puzzle from the remote database.
     * @param stringSeed is a string that will be used to product a seed for the random number generator.
     * @param range constricts the returned number from 0 to range.
     * @return
     */
    private int getRandomNumberFromString(String stringSeed, int range){
        char[] roomIdChars = stringSeed.toCharArray();
        long charValues = 0;
        for(int j=0; j<roomIdChars.length; j++){
            charValues = (charValues*10) + Character.getNumericValue(roomIdChars[j]);
        }
        Random random = new Random(charValues);
        return random.nextInt(range);
    }

    /**
     * tints a cell to indicate the opponent has placed a correct input.
     * @param cellLocation is the location of the opponent's input.
     */
    private void tintCell(int cellLocation){
        mCellViews.get(cellLocation).setBackgroundColor(getResources().getColor(R.color.oppenentCellColor));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient!=null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        mUserDisconnected = true;
        super.onStop();
        if(mGoogleApiClient!=null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        if (mUserDisconnected){
            endGame(false);
        }
        super.onResume();
    }

    @Override
    public void setUpScoreCard() {
        mStrikes = 0;
        mTimerView = (TextView)findViewById(R.id.score_text);
        mTimerView.setText("Looking for match...");
        mTime = 0;
        mTimer = new CountDownTimer(TimeUnit.MINUTES.toMillis(1000), 1000) {
            @Override
            public void onTick(long l) {
                if(mTime>20 && mNumberOfParticipants < 2){
                    cancel();
                    launchNoMatchFoundDialog();
                    (findViewById(R.id.loading_wheel)).setVisibility(View.INVISIBLE);
                }
                //Send a message to the other player every second to say we're still connected
                if(mNumberOfParticipants == 2 && mGoogleApiClient.isConnected()){
                    byte[] data = STILL_CONNECTED_MESSAGE.getBytes();
                    Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient,data,mRoomID);
                }
                /*
                Every 5 seconds check to see if the opponent is still sending messages. If the opponent
                has been sending the STILL_CONNECTED_MESSAGE, then mStillConnected must be true. If it
                isn't then it is assumed the opponent has disconnected.
                 */
                if(mTime%5==0 & mNumberOfParticipants == 2){
                    if (mStillConnected){
                        mStillConnected = false;
                    } else {
                        NetworkConnectivityChecker networkChecker =
                                new NetworkConnectivityChecker(RaceActivity.this);
                        if(networkChecker.isConnected()) {
                            mOpponentDisconnected = true;
                            endGame(true);
                        } else {
                            mUserDisconnected = true;
                            endGame(false);
                        }
                    }
                }
                mTime++;
            }
            @Override
            public void onFinish() {
                RaceActivity.this.finish();
            }
        }.start();
    }

    /**
     * Adds long click listeners to all the cells to use a selected weapon.
     * @param cell is the cell that is clicked (short or long).
     */
    @Override
    public void setCellClickListener(TextView cell) {
        super.setCellClickListener(cell);
        cell.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                switch (mWeaponSelected){
                    case "bomb":
                        deployBomb(view.getId());
                        return true;
                    case "spy":
                        deploySpy();
                        return true;
                    case "interference":
                        deployInterference();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    /**
     * First gets the box number using the puzzle solver's logic. Then clears the color tint
     * of any cells in the selected box. Finally, sends a message to the opponent notifying him/her
     * that a bomb has been used and in which box.
     * number
     * @param cellIndex is the cell that was long pressed.
     */
    private void deployBomb(int cellIndex){
        //Clear the tinted boxes on the attacker's screen
        int boxNumber = mPuzzleSolver.getBoxNumber(cellIndex);
        int[] boxCells = mPuzzleSolver.getBoxCells()[boxNumber];
        for (int i=0; i<boxCells.length; i++){
            if (mOpponentCellsFilled[boxCells[i]] == 1){
                mCellViews.get(boxCells[i]).setBackgroundColor(Color.TRANSPARENT);
            }
        }
        //Send a message to the opponent containing the box number whose contents will be erased
        String message = BOMB_MESSAGE+boxNumber;
        byte[] data = message.getBytes();
        Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, data,mRoomID);
        //Adjust the weapon image that was just used as well as the top card text
        removeWeaponFromWeaponSlot();
        removeWeaponFromInventory("bomb");
    }

    /**
     * Sets all cells that the opponent has filled to show the value in the solution.
     * A timer is started for three seconds. When the Countdown timer is done, the board is reverted
     * back to it's state beofre the spy weapon was deployed.
     */
    private void deploySpy(){
        for (int i=0; i<mOpponentCellsFilled.length; i++){
            if (mOpponentCellsFilled[i] == 1){
                mCellViews.get(i).setBackgroundColor(Color.rgb(75,75,75));
                mCellViews.get(i).setText(mSolution[i]+"");
                mCellViews.get(i).setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }
        CountDownTimer spyTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                for (int i=0; i<mOpponentCellsFilled.length; i++){
                    if (mOpponentCellsFilled[i] == 1){
                        mCellViews.get(i).setBackgroundColor(getResources().getColor(R.color.oppenentCellColor));
                        mCellViews.get(i).setTextColor(Color.BLACK);
                        if (mUserAnswers[i] == 0){
                            mCellViews.get(i).setText("");
                        } else {
                            mCellViews.get(i).setText(mUserAnswers[i]+"");
                        }
                    }
                }
            }
        };
        spyTimer.start();
        removeWeaponFromWeaponSlot();
        removeWeaponFromInventory("spy");
    }

    /**
     * Sends a message to the user's opponent to receive interference weapon.
     */
    private void deployInterference(){
        byte[] data = INTERFERENCE_MESSAGE.getBytes();
        Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, data, mRoomID);
        removeWeaponFromWeaponSlot();
        removeWeaponFromInventory("interference");
    }

    /**
     * Removes on weapon of a specific type from the user's inventory. Called when the player uses
     * on of his or her weapons.
     * @param weapon is the weapon name.
     */
    private void removeWeaponFromInventory(String weapon){
        SharedPreferences prefs =
                getSharedPreferences(ArmoryActivity.ARMORY_SHARED_PREFS, MODE_PRIVATE);
        switch (weapon){
            case "bomb":
                int bombCount = prefs.getInt(ArmoryActivity.BOMB_COUNT_KEY, 0);
                if (bombCount!=0) {
                    prefs.edit().putInt(ArmoryActivity.BOMB_COUNT_KEY, bombCount - 1).commit();
                }
                break;
            case "spy":
                int spyCount = prefs.getInt(ArmoryActivity.SPY_COUNT_KEY, 0);
                if (spyCount!=0) {
                    prefs.edit().putInt(ArmoryActivity.SPY_COUNT_KEY, spyCount - 1).commit();
                }
                break;
            case "interference":
                int interfCount = prefs.getInt(ArmoryActivity.INTERFERENCE_COUNT_KEY, 0);
                if (interfCount!=0) {
                    prefs.edit().putInt(ArmoryActivity.INTERFERENCE_COUNT_KEY, interfCount - 1).commit();
                }
                break;
        }
    }

    /**
     * Clears the weapon icon from the weapon slot at the top so it cannot be used twice.
     */
    private void removeWeaponFromWeaponSlot(){
        if (mWeapon1Clicked){
            mWeapon1 = "none";
            adjustCardText(1);
            setWeaponImage(mWeapon1, (ImageView) findViewById(R.id.weapon1_image));
            ((ImageView) findViewById(R.id.weapon1_image)).setColorFilter(Color.WHITE);
        } else if (mWeapon2Clicked){
            mWeapon2 = "none";
            adjustCardText(2);
            setWeaponImage(mWeapon2, (ImageView)findViewById(R.id.weapon2_image));
            ((ImageView) findViewById(R.id.weapon2_image)).setColorFilter(Color.WHITE);
        }
    }

    /**
     * Removes the users input found within the specified box number. The box then flashes orange/white
     * for three seconds to draw the user's eye and show where the bombing has taken place.
     * @param boxNumber is the box number that was bombed.
     */
    private void receiveBomb(int boxNumber){

        final int[] boxCellIds = mPuzzleSolver.getBoxCells()[boxNumber];
        for (int i=0; i<boxCellIds.length; i++){
            if (mKey[boxCellIds[i]] == 0) {
                mUserAnswers[boxCellIds[i]] = 0;
                mCellViews.get(boxCellIds[i]).setText("");
            }
        }

        CountDownTimer timer = new CountDownTimer(2000, 250) {
            boolean swap = false;
            @Override
            public void onTick(long l) {
                for (int i=0; i<boxCellIds.length; i++){
                    if (swap) {
                        mCellViews.get(boxCellIds[i])
                                .setBackgroundColor(getResources().getColor(R.color.bombFlashColor));
                    } else {
                        mCellViews.get(boxCellIds[i]).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
                swap = !swap;
            }

            @Override
            public void onFinish() {
                for (int i=0; i<boxCellIds.length; i++) {
                    if (mOpponentCellsFilled[boxCellIds[i]] == 1){
                        mCellViews.get(boxCellIds[i])
                                .setBackgroundColor(getResources().getColor(R.color.oppenentCellColor));
                    } else {
                        mCellViews.get(boxCellIds[i]).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        };
        timer.start();

        ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(2000);
        Toast.makeText(this, "YOU'VE BEEN ERASE BOMBED!", Toast.LENGTH_SHORT).show();
        //Reset the choice tiles because one or more hidden tiles may need to be made visible again.
        int[] numberCounters = new int[9];
        for (int i=0; i<mUserAnswers.length; i++){
            if (mUserAnswers[i] != 0){
                int value = mUserAnswers[i];
                numberCounters[value-1]++;
            }
        }

        for (int i=0; i<numberCounters.length; i++) {
            if (!(numberCounters[i] < 9)) {
                ((CardView) mChoiceTiles.get(i).getParent())
                        .setVisibility(View.INVISIBLE);
            } else {
                ((CardView) mChoiceTiles.get(i).getParent())
                        .setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Causes half the user's key to become obscured. Then the other half.
     */
    private void receiveInterference(){
        Toast.makeText(this, "Opponent used Interference!", Toast.LENGTH_SHORT).show();
        CountDownTimer timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                boolean skip = false;
                if (l < 1000 || l < 3000) {
                    skip = false;
                }
                if (l <= 6000 && l >= 300) {
                    skip = true;
                }

                for (int i = 0; i < mKey.length; i++) {
                    if (mKey[i] != 0) {
                        if (!skip) {
                            mCellViews.get(i).setBackgroundColor(Color.rgb(75,75,75));
                            mCellViews.get(i).setText("");
                        } else {
                            mCellViews.get(i).setBackgroundColor(Color.TRANSPARENT);
                            mCellViews.get(i).setText(mKey[i]+"");
                        }
                        skip = !skip;
                    }
                }
            }

            //Reset the board to it's original state before the interference occurred.
            @Override
            public void onFinish() {
                for (int i=0; i<mCellViews.size(); i++){
                    if (mOpponentCellsFilled[i] == 1){
                        mCellViews.get(i).setBackgroundColor(getResources().getColor(R.color.oppenentCellColor));
                    } else {
                        mCellViews.get(i).setBackgroundColor(Color.TRANSPARENT);

                    }
                    if (mUserAnswers[i] == 0){
                        mCellViews.get(i).setText("");
                    } else {
                        mCellViews.get(i).setText(mUserAnswers[i]+"");
                    }
                }
            }
        };
        timer.start();
    }

    /**
     * Launches an alert dialog to notify the user that no match was found.
     */
    private void launchNoMatchFoundDialog(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton("okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RaceActivity.this.finish();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        RaceActivity.this.finish();
                    }
                })
                .setTitle("No match found. Please try again!")
                .create();
        mTimer.cancel();
        if(RaceActivity.this!=null) {
            dialog.show();
        }
    }

    @Override
    public void grabSolution() {
        super.grabSolution();
        mTimerView.setText("Race!");
    }

    /**
     * Handles the results of both the google play games sign in dialog, as well as the multi-player
     * user invitation dialog.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            mResolvingConnectionFailure = false;
            mGoogleApiClient.connect();
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "onActivityResult: CONNECTING");
            } else {
                Log.d(TAG, "onActivityResult: "+resultCode);
            }
        }
        if (requestCode == RC_AUTOMATCH) {
            Log.d(TAG, "onActivityResult: "+resultCode);
            ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
            Bundle autoMatch = RoomConfig.createAutoMatchCriteria(1, 1, 0);
            RoomConfig roomConfig = RoomConfig.builder(RaceActivity.this)
                    .setRoomStatusUpdateListener(RaceActivity.this)
                    .setMessageReceivedListener(RaceActivity.this)
                    .setAutoMatchCriteria(autoMatch)
                    .addPlayersToInvite(invitees)
                    .build();
            Games.RealTimeMultiplayer.create(mGoogleApiClient, roomConfig);
        }
    }

    /**
     * Once connected, creates a mulitplayer room and sets up the score card.
     * The score card setup also initializes a timer.
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        (findViewById(R.id.loading_wheel)).setVisibility(View.VISIBLE);

        setUpScoreCard();

        setUpWeapons();

        Bundle autoMatch = RoomConfig.createAutoMatchCriteria(1, 1, 0);
        RoomConfig roomConfig = RoomConfig.builder(RaceActivity.this)
                .setRoomStatusUpdateListener(RaceActivity.this)
                .setMessageReceivedListener(RaceActivity.this)
                .setAutoMatchCriteria(autoMatch)
                .build();
        Games.RealTimeMultiplayer.create(mGoogleApiClient, roomConfig);
    }

    /**
     * Attempt to reconnect if connection is suspended.
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");
        mGoogleApiClient.connect();
    }

    /**
     * If connection fails, attempt to resolve connection issue.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure){return;}

        mResolvingConnectionFailure = true;

        Log.d(TAG, "onConnectionFailed: "+connectionResult);
        if(!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult, RC_SIGN_IN, "ERROR BASE GAME UTILS")){
            mResolvingConnectionFailure = false;
        }
    }

    /**
     * Once connected to a room, a random puzzle is pulled from the remote database for the players
     * to solve. Next the puzzle is solved and the UI pieces are initialized.
     * @param i
     * @param room
     */
    @Override
    public void onRoomConnected(int i, final Room room) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mRoomID = room.getRoomId();
        mNumberOfParticipants = room.getParticipantIds().size();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Puzzles").child("medium");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Puzzle> puzzles = new ArrayList<>();
                for (DataSnapshot childSnap : dataSnapshot.getChildren()){
                    ArrayList<Long> longKey = (ArrayList<Long>) childSnap.getValue();
                    ArrayList<Integer> intKey = new ArrayList<>();
                    for (Long longNum : longKey){
                        intKey.add((int)(long)longNum);
                    }
                    Puzzle puzzle = new Puzzle();
                    puzzle.setKey(intKey);
                    puzzles.add(puzzle);
                }
                String participants = room.getParticipantIds().get(0) + room.getParticipantIds().get(1);

                int randomIndex = getRandomNumberFromString(participants, puzzles.size());

                mKey = puzzles.get(randomIndex).getKeyIntArray();
                initializeGame();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+databaseError.getCode());
            }
        });
    }

    /**
     * Handles all data received from the user's opponent.
     * @param realTimeMessage
     */
    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {
        String data = new String(realTimeMessage.getMessageData(), StandardCharsets.UTF_8);
        if(data.equals(OPPONENT_QUIT_MESSAGE)){
            mOpponentQuit = true;
            endGame(true);
        } else if(data.equals(OPPONENT_DISCONNECTED_MESSAGE)){
            mOpponentDisconnected = true;
            endGame(true);
        } else if(data.equals(TOO_MANY_GUESSES_MESSAGE)){
            mOpponentTooManyGuesses = true;
            endGame(true);
        } else if(data.equals(OPPONENT_FINISHED_MESSAGE)){
            mOpponentFinished = true;
            endGame(false);
        } else if(data.equals(STILL_CONNECTED_MESSAGE)){
            mStillConnected = true;
        } else if(data.equals(INTERFERENCE_MESSAGE)){
            receiveInterference();
        } else if(data.contains(BOMB_MESSAGE)){
            int boxNumber = Character.getNumericValue(data.charAt(data.length()-1));
            receiveBomb(boxNumber);
        } else {
            int cellLocation = Integer.parseInt(data);
            tintCell(cellLocation);
            mOpponentCellsFilled[cellLocation] = 1;
        }
    }

    //User has quit out of the game if he/she presses the back button
    @Override
    public void onBackPressed() {
        if(mRoomID!=null) {
            byte[] data = OPPONENT_QUIT_MESSAGE.getBytes();
            Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, data, mRoomID);
        }
        mTimer.cancel();
        super.onBackPressed();
    }

    //All methods below are required interface overrides

    @Override
    public void onPeersDisconnected(Room room, List<String> list) {
        mOpponentDisconnected = true;
        endGame(true);
    }
    @Override
    public void onP2PDisconnected(String s) {
        Log.d(TAG, "onP2PDisconnected: ");
    }
    @Override
    public void onLeftRoom(int i, String s) {
        RaceActivity.this.finish();
        byte[] data = OPPONENT_QUIT_MESSAGE.getBytes();
        Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, data, mRoomID);
    }
    @Override
    public void onRoomConnecting(Room room) {}
    @Override
    public void onRoomAutoMatching(Room room) {}
    @Override
    public void onPeerInvitedToRoom(Room room, List<String> list) {}
    @Override
    public void onPeerDeclined(Room room, List<String> list) {}
    @Override
    public void onPeerJoined(Room room, List<String> list) {}
    @Override
    public void onPeerLeft(Room room, List<String> list) {}
    @Override
    public void onConnectedToRoom(Room room) {}
    @Override
    public void onDisconnectedFromRoom(Room room) {}
    @Override
    public void onPeersConnected(Room room, List<String> list) {}
    @Override
    public void onRoomCreated(int i, Room room) {}
    @Override
    public void onJoinedRoom(int i, Room room) {}
    @Override
    public void onP2PConnected(String s) {}

}
