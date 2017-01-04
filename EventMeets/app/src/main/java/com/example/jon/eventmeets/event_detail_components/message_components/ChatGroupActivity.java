package com.example.jon.eventmeets.event_detail_components.message_components;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jon.eventmeets.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ChatGroupActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private EditText mMessageText;
    private Button mSendMessage;
    private MessageGroup mGroup;
    private MessageRecyclerAdapter mAdapter;
    private DatabaseReference mReference;
    private ChildEventListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group);

        Intent intent = getIntent();
        String chatKey = intent.getStringExtra("chatKey");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mReference = database.getReference("chats").child(chatKey);

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGroup = dataSnapshot.getValue(MessageGroup.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mMessageRecycler = (RecyclerView)findViewById(R.id.chat_message_recycler);
        mMessageText = (EditText)findViewById(R.id.message_edit);
        mSendMessage = (Button)findViewById(R.id.send_message);

        mMessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    mSendMessage.setBackgroundColor(Color.alpha(255));
                    mSendMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String message = mMessageText.getText().toString();
                            SelfMessageObject sending = new SelfMessageObject(message);
                        }
                    });
                } else {
                    mSendMessage.setBackgroundColor(Color.alpha(100));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAdapter = new MessageRecyclerAdapter(mGroup.getMessages());
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mMessageRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mGroup.addMessage(dataSnapshot.getValue(SelfMessageObject.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChatGroupActivity.this, "Message Error", Toast.LENGTH_SHORT).show();
            }
        };
        mReference.child("messages").addChildEventListener(mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mReference.child("messages").removeEventListener(mListener);
    }
}
