package com.example.jon.eventmeets.main_menu;

import com.example.jon.eventmeets.model.GameResultObject;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jon on 12/16/2016.
 */

class MainMenuPresenter implements MainMenuContract.Presenter {
    private MainMenuView mView;
    private List<GameResultObject> mEventList;
    private DatabaseReference mReference;

    MainMenuPresenter(MainMenuView view) {
        mView = view;
        FirebaseDatabase firebase = FirebaseDatabase.getInstance();
        mReference = firebase.getReference();
    }

    @Override
    public void onNewEventsNeeded() {
        mEventList = new ArrayList<>();
    }

    @Override
    public void onNewEventsReady(List<GameResultObject> newEvents) {
        mView.setupRecyclerView(newEvents);
    }

    @Override
    public void onEventPressed(GameResultObject gameEvent) {

    }

    @Override
    public void onBrowsePressed() {
        mView.openBrowseActivity();
    }
}
