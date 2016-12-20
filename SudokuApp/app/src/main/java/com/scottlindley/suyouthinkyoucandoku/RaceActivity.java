package com.scottlindley.suyouthinkyoucandoku;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

public class RaceActivity extends BasePuzzleActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener, RoomUpdateListener {
    private static final String TAG = "RaceActivity";
    public static final int RC_SIGN_IN = 5667;
    public static final int RC_AUTOMATCH = 6980;
    public static final String OPPONENT_DISCONNECTED_MESSAGE = "opponent disconnected";
    public static final String OPPONENT_QUIT_MESSAGE = "opponent quit";
    public static final String TOO_MANY_GUESSES_MESSAGE = "too many guesses";
    public static final String OPPONENT_FINISHED_MESSAGE = "opponent finished";
    private boolean mOpponentQuit, mOpponentDisconnected, mOpponentTooManyGuesses, mOpponentFinished;
    private String mRoomID;
    private boolean mResolvingConnectionFailure = false;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        mDBHelper = DBHelper.getInstance(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
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
            }
            resultDetailText.setVisibility(View.VISIBLE);
        }

        racesWon.setText("Races Won: "+ stats.getRacesWon());
        racesLost.setText("Races Lost: "+ stats.getRacesLost());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton("okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
     * @param stringSeed
     * @param range
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
    public void setUpScoreCard() {
        mStrikes = 0;
        mTimerView = (TextView)findViewById(R.id.score_text);
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
     * Once connected, launches player invite dialog.
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: "+mGoogleApiClient.isConnected());
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
                Log.d(TAG, "onDataChange: "+room.getParticipantIds().size());
                Log.d(TAG, "onDataChange: "+room.getParticipantIds().get(0));
                Log.d(TAG, "onDataChange: "+room.getParticipantIds().get(1));
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
        } else {
            int cellLocation = Integer.parseInt(data);
            tintCell(cellLocation);
        }
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
    public void onPeerLeft(Room room, List<String> list) {
        mOpponentQuit = true;
        endGame(true);
    }
    @Override
    public void onConnectedToRoom(Room room) {}
    @Override
    public void onDisconnectedFromRoom(Room room) { endGame(false);
        byte[] data = OPPONENT_DISCONNECTED_MESSAGE.getBytes();
        Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, data, mRoomID);
        RaceActivity.this.finish();
    }
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
    public void onP2PDisconnected(String s) {}
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
