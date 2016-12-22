package com.example.jon.eventmeets.event_detail_components;

/**
 * Created by Jon on 12/22/2016.
 */

class Presenter implements VideoGameSearchContract {
    private VideoGameSearchContract.View mView;

    Presenter(View view) {
        mView = view;
    }
}
