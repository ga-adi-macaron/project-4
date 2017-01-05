package com.example.jon.eventmeets.event_detail_components.find_players_components;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.jon.eventmeets.R;
import com.example.jon.eventmeets.model.AvailablePlayer;
import com.example.jon.eventmeets.model.DatabaseGameObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventMessageActivity extends AppCompatActivity {
    private DatabaseGameObject mGameObject;
    private String mName, mPlatform, mId, mScreenshot, mCover;
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;
    private List<String> mPlayerIDs;
    private AvailablePlayerRecycler mAdapter;
    private RecyclerView mPlayerRecycler;
    private TextView mTotalPlayers;
    private List<AvailablePlayer> mPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_message);

        Intent intent = getIntent();

        mId = Integer.toString(intent.getIntExtra("id",-1));
        mName = intent.getStringExtra("name");
        mPlatform = intent.getStringExtra("console");
        mCover = intent.getStringExtra("cover");
        mScreenshot = intent.getStringExtra("screencap");

        setTitle(mName);
        mPlayerRecycler = (RecyclerView)findViewById(R.id.player_recycler);
        mTotalPlayers = (TextView)findViewById(R.id.total_players);

        mGameObject = new DatabaseGameObject(mId, mName, mScreenshot, mCover, mPlatform);
        mPlayerIDs = new ArrayList<>();
        mPlayers = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("games");

        mPlayerRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(!dataSnapshot.hasChild(mId)&&!dataSnapshot.hasChild(mPlatform)) {
                    mReference.child(mId).child(mPlatform).setValue(mGameObject);
                } else {
                    for(DataSnapshot data :dataSnapshot.child(mId).child(mPlatform).getChildren()) {
                        if(!(data.getKey().equals("id")||data.getKey().equals("cover")||data.getKey().equals("title")
                        ||data.getKey().equals("screencap")||data.getKey().equals("platform"))) {
                            mPlayerIDs.add(data.getKey());
                        }
                    }
                }
                if(!dataSnapshot.child(mId).child(mPlatform).hasChild(user)) {
                    mReference.child(mId).child(mPlatform).child(user).setValue("looking");
                    mPlayerIDs.add(user);
                }
                mTotalPlayers.setText(mPlayerIDs.size()+" players searching");
                findPlayersById();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("EventMessageActivity", "onCancelled: "+databaseError);
            }
        });

    }

    private void findPlayersById() {
        for(int i=0;i<mPlayerIDs.size();i++) {
            mDatabase.getReference("users").child(mPlayerIDs.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mPlayers.add(dataSnapshot.getValue(AvailablePlayer.class));
                    if(mPlayers.size() == mPlayerIDs.size()) {
                        mAdapter = new AvailablePlayerRecycler(mPlayers);
                        mPlayerRecycler.setAdapter(mAdapter);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
