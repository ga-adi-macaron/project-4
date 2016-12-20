package com.example.jon.eventmeets.main_menu;

import com.example.jon.eventmeets.Model.BaseEvent;
import com.example.jon.eventmeets.Model.ConversationEventModels.ConversationEventParent;
import com.example.jon.eventmeets.Model.DrinkEventModels.DrinkEventParent;
import com.example.jon.eventmeets.Model.EventParent;
import com.example.jon.eventmeets.Model.GamingEventModels.VideoGamingEvent;
import com.example.jon.eventmeets.Model.NatureEventModels.HikingEvent;
import com.example.jon.eventmeets.Model.TasteEventModels.TasteEventParent;
import com.google.firebase.auth.FirebaseAuth;
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
    private DatabaseReference mReference;

    MainMenuPresenter(MainMenuView view) {
        mView = view;
        FirebaseDatabase firebase = FirebaseDatabase.getInstance();
        mReference = firebase.getReference();
    }

    @Override
    public void onNewEventsNeeded() {
        mEventList = new ArrayList<>();
        for(int i=0;i<10;i++) {
            mEventList.add(getRandomEvent());
        }
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
        mView.openBrowseActivity();
    }

    private EventParent getRandomEvent() {
        EventParent randomEvent;
        int categoryNumber = (int)Math.round(Math.random()*6)+1;
        switch(categoryNumber) {
            case 1:
                randomEvent = new ConversationEventParent();
                break;
            case 2:
                randomEvent = new DrinkEventParent();
                break;
            case 3:
                randomEvent = new VideoGamingEvent("title", "category", false);
                break;
            case 4:
                randomEvent = new HikingEvent("hiking", "nature", "location");
                break;
            case 5:
                randomEvent = new ConversationEventParent();
                break;
            case 6:
                randomEvent = new DrinkEventParent();
                break;
            default:
                return null;
        }

        return randomEvent;
    }
}
