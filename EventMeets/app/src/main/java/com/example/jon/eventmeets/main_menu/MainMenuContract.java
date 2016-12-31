package com.example.jon.eventmeets.main_menu;

import com.example.jon.eventmeets.model.VideoGamingEvent;

import java.util.List;

interface MainMenuContract {
    interface Presenter {
        void onNewEventsNeeded();
        void onNewEventsReady(List<VideoGamingEvent> newEvents);
        void onEventPressed(VideoGamingEvent gameEvent);
        void onBrowsePressed();
    }

    interface View {
        void setupRecyclerView(List<VideoGamingEvent> list);
        void hideLoginButton();
        void openEventDetail(VideoGamingEvent event);
        void openBrowseActivity();
        void displayLoginButton();
        void openSettingsActivity();
    }
}
