package com.example.jon.eventmeets.main_menu;

import com.example.jon.eventmeets.model.GameResultObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jon on 12/16/2016.
 */

class MainMenuPresenter implements MainMenuContract.Presenter {
    private MainMenuView mView;
    private Map<String, String> mChats;
    private DatabaseReference mReference;

    MainMenuPresenter(MainMenuView view) {
        mView = view;
        mChats = new HashMap<>();
        FirebaseDatabase firebase = FirebaseDatabase.getInstance();
        mReference = firebase.getReference("users");
    }

    @Override
    public void onNewEventsNeeded() {
        mReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data : dataSnapshot.getChildren()) {
                            mChats.put(data.getKey(), data.getValue(String.class));
                        }
                        mView.setupRecyclerView(mChats);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onEventPressed(String chatId) {

    }

    @Override
    public void onBrowsePressed() {
        mView.openBrowseActivity();
    }
}
