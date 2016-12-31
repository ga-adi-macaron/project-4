package com.example.jon.eventmeets.main_menu;

import com.example.jon.eventmeets.model.VideoGamingEvent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jon on 12/16/2016.
 */

class MainMenuPresenter implements MainMenuContract.Presenter {
    private MainMenuView mView;
    private List<VideoGamingEvent> mEventList;
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
    public void onNewEventsReady(List<VideoGamingEvent> newEvents) {
        mView.setupRecyclerView(newEvents);
    }

    @Override
    public void onEventPressed(VideoGamingEvent gameEvent) {

    }

    @Override
    public void onBrowsePressed() {
        mView.openBrowseActivity();
    }
}
