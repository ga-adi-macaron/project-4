package com.scottlindley.suyouthinkyoucandoku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.BaseGameUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RaceActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener, RoomUpdateListener{
    private static final String TAG = "RaceActivity";
    public static final int RC_SIGN_IN = 5667;
    public static final int RC_AUTOMATCH = 6980;
    private boolean mResolvingConnectionFailure = false;
    private List<String> mParticipantIDS;

    private GoogleApiClient mGoogleApiClient;
    private TextView mQuickRaceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();


        mQuickRaceButton = (TextView)findViewById(R.id.quickRace);
        mQuickRaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//                Games.RealTimeMultiplayer.join(mGoogleApiClient, roomConfig);
                Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, 1, 1, true);
                startActivityForResult(intent, RC_AUTOMATCH);
//                Games.RealTimeMultiplayer.create(mGoogleApiClient, roomConfig);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    //TODO: FIX THIS UGLY HACK. CURRENTLY FORCING A SECOND SIGN IN
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: "+mGoogleApiClient.isConnected());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure){return;}

        mResolvingConnectionFailure = true;

        Log.d(TAG, "onConnectionFailed: "+connectionResult);
        if(!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult, RC_SIGN_IN, "ERROR BASE GAME UTILS")){
            mResolvingConnectionFailure = false;
        }

    }

    @Override
    public void onRoomConnecting(Room room) {
        Log.d(TAG, "onRoomConnecting: ");
    }

    @Override
    public void onRoomAutoMatching(Room room) {
        Log.d(TAG, "onRoomAutoMatching: ");
    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> list) {
        Log.d(TAG, "onPeerInvitedToRoom: ");
    }

    @Override
    public void onPeerDeclined(Room room, List<String> list) {
        Log.d(TAG, "onPeerDeclined: ");
    }

    @Override
    public void onPeerJoined(Room room, List<String> list) {
        Log.d(TAG, "onPeerJoined: ");
    }

    @Override
    public void onPeerLeft(Room room, List<String> list) {
        Log.d(TAG, "onPeerLeft: ");
    }

    @Override
    public void onConnectedToRoom(Room room) {
        Log.d(TAG, "onConnectedToRoom: ");
    }

    @Override
    public void onDisconnectedFromRoom(Room room) {
        Log.d(TAG, "onDisconnectedFromRoom: ");
    }

    @Override
    public void onPeersConnected(Room room, List<String> list) {
        Log.d(TAG, "onPeersConnected: ");
        mParticipantIDS = list;
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> list) {
        Log.d(TAG, "onPeersDisconnected: ");
    }

    @Override
    public void onP2PConnected(String s) {
        Log.d(TAG, "onP2PConnected: ");
    }

    @Override
    public void onP2PDisconnected(String s) {
        Log.d(TAG, "onP2PDisconnected: ");
    }

    @Override
    public void onRoomCreated(int i, Room room) {
        Log.d(TAG, "onRoomCreated: "+room.getRoomId());
        Toast.makeText(this, "Room ID: "+room.getRoomId(), Toast.LENGTH_SHORT).show();
        if (i != GamesStatusCodes.STATUS_OK) {
            // let screen go to sleep
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            // show error message, return to main screen.
        }
    }

    @Override
    public void onJoinedRoom(int i, Room room) {
        Log.d(TAG, "onRoomCreated: "+room.getRoomId());
        if (i != GamesStatusCodes.STATUS_OK) {
            // let screen go to sleep
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            // show error message, return to main screen.
        }
    }

    @Override
    public void onLeftRoom(int i, String s) {
        Log.d(TAG, "onLeftRoom: ");
    }

    @Override
    public void onRoomConnected(int i, Room room) {
        Log.d(TAG, "onRoomConnected: ");
        byte[] data = "MESSAGE".getBytes();
        Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, data, room.getRoomId());
    }

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {
        String data = new String(realTimeMessage.getMessageData(), StandardCharsets.UTF_8);
        Log.d(TAG, "onRealTimeMessageReceived: "+data);
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
}
