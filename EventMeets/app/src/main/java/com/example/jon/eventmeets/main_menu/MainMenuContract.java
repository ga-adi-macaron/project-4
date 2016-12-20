package com.example.jon.eventmeets.main_menu;

import com.example.jon.eventmeets.model.EventParent;

import java.util.List;

interface MainMenuContract {
    interface Presenter {
        void onNewEventsNeeded();
        void onNewEventsReady(List<EventParent> newEvents);
        void onEventPressed(EventParent eventParent);
        void onBrowsePressed();
    }

    interface View {
        void setupRecyclerView(List<EventParent> list);
        void hideLoginButton();
        void openEventDetail(EventParent event);
        void openBrowseActivity();
        void displayLoginButton();
        void openSettingsActivity();
    }
}
