package com.example.jon.eventmeets.event_detail_components;


/**
 * Created by Jon on 12/22/2016.
 */

class Presenter implements VideoGameSearchContract.Presenter {
    private VideoGameSearchContract.View mView;

    private static final String BASE_URL = "http://www.giantbomb.com/api/search?";
    private static final String KEY = "&api_key=76850e49a2af9255d3cbb9caec66d702dfac1521 ";
    private static final String PARAMS = "field_list=name,id,platforms,image&format=json&query=";

    private static final String URL = BASE_URL+KEY+PARAMS;

    Presenter(VideoGameSearchContract.View view) {
        mView = view;
    }

    @Override
    public void onSearchRequested(String query) {

    }

}
