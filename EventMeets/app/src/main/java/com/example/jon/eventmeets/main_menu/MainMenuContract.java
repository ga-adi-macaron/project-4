package com.example.jon.eventmeets.main_menu;

import com.example.jon.eventmeets.model.GameResultObject;

import java.util.List;
import java.util.Map;

interface MainMenuContract {
    interface Presenter {
        void onNewEventsNeeded();
        void onEventPressed(String chatId);
        void onBrowsePressed();
    }

    interface View {
        void setupRecyclerView(Map<String, String> list);
        void openEventDetail(String chatId);
        void openBrowseActivity();
        void openSettingsActivity();
    }
}
