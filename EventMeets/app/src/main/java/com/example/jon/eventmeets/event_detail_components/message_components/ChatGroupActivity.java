package com.example.jon.eventmeets.event_detail_components.message_components;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatGroupActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private EditText mMessageText;
    private Button mSendMessage;
    private MessageGroup mGroup;
    private MessageRecyclerAdapter mAdapter;
    private DatabaseReference mReference;
    private ChildEventListener mListener;
    private Map<String,SelfMessageObject> mMessages;
    private List<SelfMessageObject> mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group);

        Intent intent = getIntent();
        String chatKey = intent.getStringExtra("chatKey");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mReference = database.getReference("chats").child(chatKey);

        mMessages = new HashMap<>();

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGroup = dataSnapshot.getValue(MessageGroup.class);
                mMessages = mGroup.getMessages();
                mContent = new ArrayList<>(mMessages.values());
                mAdapter = new MessageRecyclerAdapter(mContent);
                mMessageRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.VERTICAL, false));
                mMessageRecycler.setAdapter(mAdapter);
                setListener();
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
                    mSendMessage.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255,75,175,80)));
                    mSendMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String message = mMessageText.getText().toString();
                            SelfMessageObject sending = new SelfMessageObject(message);
                            mReference.child("messages").push().setValue(sending);
                            mMessageText.setText("");
                        }
                    });
                } else {
                    mSendMessage.setBackgroundTintList(ColorStateList.valueOf(Color.argb(100,75,175,80)));
                    mSendMessage.setOnClickListener(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mReference.child("messages").removeEventListener(mListener);
    }

    private void setListener() {
        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mGroup.addMessage(dataSnapshot.getKey(),dataSnapshot.getValue(MessageObject.class));
                mContent.add(dataSnapshot.getValue(MessageObject.class));
                mAdapter.notifyItemInserted(mContent.size());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChatGroupActivity.this, "Chat Error", Toast.LENGTH_SHORT).show();
            }
        };
        mReference.child("messages").addChildEventListener(mListener);
    }
}
