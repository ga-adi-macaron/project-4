package com.example.jon.eventmeets.event_detail_components;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jon.eventmeets.R;
import com.example.jon.eventmeets.model.BaseUser;
import com.example.jon.eventmeets.model.DatabaseGameObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventMessageActivity extends AppCompatActivity {
    private DatabaseGameObject mGameObject;
    private String mName, mPlatform, mId, mScreenshot, mCover;
    private DatabaseReference mReference;

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

        mGameObject = new DatabaseGameObject(mId, mName, mScreenshot, mCover, mPlatform);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        mReference = db.getReference("games");

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(mId))
                    mReference.child(mId).child(mPlatform).setValue(mGameObject);
                String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                mReference.child(mId).child(mPlatform).child(user).setValue("looking");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("EventMessageActivity", "onCancelled: "+databaseError);
            }
        });
    }
}
