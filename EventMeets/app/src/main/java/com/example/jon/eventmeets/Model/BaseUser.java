package com.example.jon.eventmeets.model;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Jon on 12/16/2016.
 */

public class BaseUser {
    private String username;
    private String firstName;
    private String lastName;
    private List<String> activeChatKeys;

    private BaseUser(){}

    private static BaseUser sInstance = null;

    public static BaseUser getInstance() {
        if(sInstance == null) {
            sInstance = new BaseUser();
        }
        return sInstance;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getActiveChatKeys() {
        return activeChatKeys;
    }

    public void addNewChat(String key) {
        if(!activeChatKeys.contains(key)) {
            activeChatKeys.add(key);
        }
    }

    public void fetchChatKeys() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("users").child(username).child("chats");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    activeChatKeys.add(data.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("BaseUser tag", "onCancelled: "+databaseError);
            }
        });
    }
}
