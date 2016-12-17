package com.example.jon.eventmeets.main_menu;

import com.example.jon.eventmeets.Model.EventParent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jon on 12/16/2016.
 */

class MainMenuPresenter implements MainMenuContract.Presenter {
    private MainMenuView mView;
    private List<EventParent> mEventList;
    private FirebaseDatabase mFirebase;
    private DatabaseReference mReference;

    MainMenuPresenter(MainMenuView view) {
        mView = view;
        mFirebase = FirebaseDatabase.getInstance();
        mReference = mFirebase.getReference();
    }

    @Override
    public void onNewEventsNeeded() {
        mEventList = new ArrayList<>();
    }

    @Override
    public void onNewEventsReady(List<EventParent> newEvents) {
        mView.setupRecyclerView(newEvents);
    }

    @Override
    public void onEventPressed(EventParent eventParent) {

    }

    @Override
    public void onBrowsePressed() {

    }
}
