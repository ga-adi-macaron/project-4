package com.example.jon.eventmeets.EventCategoryBrowser;

/**
 * Created by Jon on 12/16/2016.
 */

class EventBrowserPresenter implements EventBrowserContract.Presenter {
    private EventBrowserContract.View mView;

    EventBrowserPresenter(EventBrowserContract.View view) {
        mView = view;
    }

    @Override
    public void onActivityStarted() {

    }
}
