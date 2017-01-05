package com.example.jon.eventmeets.event_detail_components.find_players_components;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.jon.eventmeets.R;
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
    private List<String> mPlayerIDs;
    private AvailablePlayerRecycler mAdapter;
    private RecyclerView mPlayerRecycler;
    private TextView mTotalPlayers;

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

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        mReference = db.getReference("games");

        mAdapter = new AvailablePlayerRecycler(mPlayerIDs);
        mPlayerRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mPlayerRecycler.setAdapter(mAdapter);

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(!dataSnapshot.hasChild(mId)) {
                    mReference.child(mId).child(mPlatform).setValue(mGameObject);
                } else {
                    for(DataSnapshot data :dataSnapshot.child(mId).child(mPlatform).getChildren()) {
                        if(!(data.getKey().equals("id")||data.getKey().equals("cover")||data.getKey().equals("title")
                        ||data.getKey().equals("screencap")||data.getKey().equals("platform"))) {
                            mPlayerIDs.add(data.getKey());
                        }
                    }
                }
                mReference.child(mId).child(mPlatform).child(user).setValue("looking", new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        mPlayerIDs.add(user);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                mTotalPlayers.setText(mPlayerIDs.size()+" players searching");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("EventMessageActivity", "onCancelled: "+databaseError);
            }
        });

    }
}
