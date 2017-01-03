package com.example.jon.eventmeets.event_detail_components;

import com.example.jon.eventmeets.model.GameResultObject;

import java.util.List;

/**
 * Created by Jon on 12/22/2016.
 */

public interface VideoGameSearchContract {

    interface View {
        void displaySearchResults(List<GameResultObject> games);
    }

    interface Presenter {
        void onSearchRequested(String query);
    }
}
