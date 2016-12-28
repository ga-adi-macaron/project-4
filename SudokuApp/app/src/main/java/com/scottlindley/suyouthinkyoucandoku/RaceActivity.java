package com.scottlindley.suyouthinkyoucandoku;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

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
    private boolean mOpponentQuit, mOpponentDisconnected, mOpponentTooManyGuesses, mOpponentFinished;
    private boolean mUserDisconnected;
    private String mRoomID;
    private int mNumberOfParticipants;
    private boolean mResolvingConnectionFailure, mStillConnected;
    private GoogleApiClient mGoogleApiClient;
    private String weapon1, weapon2;
    private boolean weapon1Clicked, weapon2Clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        mDBHelper = DBHelper.getInstance(this);

        (findViewById(R.id.loading_wheel)).setVisibility(View.INVISIBLE);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }

    private void setUpWeapons(){
        SharedPreferences prefs =
                getSharedPreferences(MainMenuActivity.ARMED_WEAPONS_PREFS, MODE_PRIVATE);
        weapon1 = prefs.getString(MainMenuActivity.WEAPON_SLOT1_KEY, "none");
        weapon2 = prefs.getString(MainMenuActivity.WEAPON_SLOT2_KEY, "none");

        final ImageView weapon1Image = (ImageView)findViewById(R.id.weapon1_image);
        final ImageView weapon2Image = (ImageView)findViewById(R.id.weapon2_image);

        setWeaponImage(weapon1, weapon1Image);
        setWeaponImage(weapon2, weapon2Image);

        CardView weapon1Card = (CardView)findViewById(R.id.weapon1);
        CardView weapon2Card = (CardView)findViewById(R.id.weapon2);

        weapon1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weapon1Clicked = !weapon1Clicked;
                if (!weapon1.equals("none") && !weapon1Clicked){
                    weapon1Image.setColorFilter(getResources().getColor(R.color.colorAccent));
                    weapon2Clicked = false;
                    weapon2Image.setColorFilter(Color.WHITE);
                    adjustCardText(1);
                } else if (!weapon1.equals("none") && weapon1Clicked){
                    weapon1Image.setColorFilter(Color.WHITE);
                    mTimerView.setText("Race!");
                }
            }
        });

        weapon2Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weapon2Clicked = !weapon2Clicked;
                if (!weapon2.equals("none") && !weapon2Clicked){
                    weapon2Image.setColorFilter(getResources().getColor(R.color.colorAccent));
                    weapon1Clicked = false;
                    weapon1Image.setColorFilter(Color.WHITE);
                    adjustCardText(2);
                } else if (!weapon2.equals("none") && weapon2Clicked){
                    weapon2Image.setColorFilter(Color.WHITE);
                    mTimerView.setText("Race!");
                }
            }
        });
    }

    private void adjustCardText(int weaponSlot){
        String weapon = "none";
        if (weaponSlot == 1) {
            weapon = weapon1;
        } else if (weaponSlot == 2){
            weapon = weapon2;
        }
        switch (weapon) {
            case "bomb":
                mTimerView.setText("Long press a box to drop Erase Bomb!");
                break;
            case "spy":
                mTimerView.setText("Long press the board to spy!");
                break;
            case "interference":
                mTimerView.setText("Long press the board to deploy interference!");
                break;
            case "none":
        }
    }

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
                weaponImage.setImageResource(R.drawable.none);
                break;
        }
    }

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
    public void endGame(boolean win){
        for (TextView tile : mChoiceTiles){
            ((CardView)tile.getParent()).setVisibility(View.INVISIBLE);
        }

        View dialogView = getLayoutInflater().inflate(R.layout.end_race_game_dialog, null);
        TextView gameResultText = (TextView)dialogView.findViewById(R.id.game_result_text);
        TextView resultDetailText = (TextView)dialogView.findViewById(R.id.lost_game_message);
        TextView racesWon = (TextView)dialogView.findViewById(R.id.races_won);
        TextView racesLost = (TextView)dialogView.findViewById(R.id.races_lost);

        Stats stats = mDBHelper.getStats();
        if(win) {
            gameResultText.setText("You Win!");
            if(isCorrect) {
                resultDetailText.setText("You completed the puzzle before the enemy");
                byte[] data = OPPONENT_FINISHED_MESSAGE.getBytes();
                Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, data, mRoomID);
            } else if (mOpponentTooManyGuesses){
                resultDetailText.setText("Your opponent guessed made too many incorrect guesses");
            } else if (mOpponentQuit){
                resultDetailText.setText("Your opponent quit");
            } else if (mOpponentDisconnected){
                resultDetailText.setText("Your opponent was disconnected");
            }
            resultDetailText.setVisibility(View.VISIBLE);
            mDBHelper.updateRacesWon(stats.getRacesWon() + 1);
            stats = mDBHelper.getStats();
        } else {
            //The game was lost
            gameResultText.setText("You Lose");
            mDBHelper.updateRacesLost(stats.getRacesLost()+1);
            stats = mDBHelper.getStats();
            if (mStrikes > 2){
                resultDetailText.setText("Three incorrect answers");
                byte[] data = "too many guesses".getBytes();
                Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient,data,mRoomID);
            } else if (mOpponentFinished){
                resultDetailText.setText("Your opponent has finished the puzzle");
            } else if (mUserDisconnected){
                resultDetailText.setText("You have disconnected from the game");
            }
            resultDetailText.setVisibility(View.VISIBLE);
        }
        mTimer.cancel();

        racesWon.setText("Races Won: "+ stats.getRacesWon());
        racesLost.setText("Races Lost: "+ stats.getRacesLost());

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
        dialog.show();

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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

    private void tintCell(int cellLocation){
        Log.d(TAG, "tintCell: "+cellLocation);
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
        } else {
            int cellLocation = Integer.parseInt(data);
            tintCell(cellLocation);
        }
    }

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
    public void onPeersDisconnected(Room room, List<String> list) {
        mOpponentDisconnected = true;
        endGame(true);
    }
    @Override
    public void onP2PConnected(String s) {}
    @Override
    public void onP2PDisconnected(String s) {
        Log.d(TAG, "onP2PDisconnected: ");
    }
    @Override
    public void onRoomCreated(int i, Room room) {}
    @Override
    public void onJoinedRoom(int i, Room room) {}
    @Override
    public void onLeftRoom(int i, String s) {
        RaceActivity.this.finish();
        byte[] data = OPPONENT_QUIT_MESSAGE.getBytes();
        Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, data, mRoomID);
    }

}
