package com.example.jon.eventmeets.main_menu;

import com.example.jon.eventmeets.model.GameResultObject;

import java.util.List;

interface MainMenuContract {
    interface Presenter {
        void onNewEventsNeeded();
        void onNewEventsReady(List<GameResultObject> newEvents);
        void onEventPressed(GameResultObject gameEvent);
        void onBrowsePressed();
    }

    interface View {
        void setupRecyclerView(List<GameResultObject> list);
        void openEventDetail(GameResultObject event);
        void openBrowseActivity();
        void openSettingsActivity();
    }
}
