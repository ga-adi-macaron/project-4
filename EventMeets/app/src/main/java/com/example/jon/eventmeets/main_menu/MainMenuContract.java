package com.example.jon.eventmeets.main_menu;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.jon.eventmeets.Model.Category;
import com.example.jon.eventmeets.Model.EventParent;

import java.util.List;

public interface MainMenuContract {
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
        void openBrowseActivity(Category category);
        void displayLoginButton();
        void openSettingsActivity();
    }
}
