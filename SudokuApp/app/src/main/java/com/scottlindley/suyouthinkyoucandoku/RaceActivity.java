package com.scottlindley.suyouthinkyoucandoku;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
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

public class RaceActivity extends BasePuzzleActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener, RoomUpdateListener {
    private static final String TAG = "RaceActivity";
    public static final int RC_SIGN_IN = 5667;
    public static final int RC_AUTOMATCH = 6980;
    private boolean mResolvingConnectionFailure = false;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
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

        View dialogView = getLayoutInflater().inflate(R.layout.end_game_dialog, null);
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

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient!=null) {
            mGoogleApiClient.connect();
        }
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
        Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, 1, 1, true);
        startActivityForResult(intent, RC_AUTOMATCH);
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
        Log.d(TAG, "onRoomConnected: ");
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Puzzles").child("medium");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Puzzle> puzzles = new ArrayList<>();
                for (DataSnapshot childSnap : dataSnapshot.getChildren()){
                    puzzles.add(childSnap.getValue(Puzzle.class));
                }

                int randomIndex = getRandomNumberFromString(room.getRoomId(), puzzles.size());

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
        Log.d(TAG, "onRealTimeMessageReceived: " + data);
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
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
    public void onPeersDisconnected(Room room, List<String> list) {}
    @Override
    public void onP2PConnected(String s) {}
    @Override
    public void onP2PDisconnected(String s) {}
    @Override
    public void onRoomCreated(int i, Room room) {}
    @Override
    public void onJoinedRoom(int i, Room room) {}
    @Override
    public void onLeftRoom(int i, String s) {}
}
