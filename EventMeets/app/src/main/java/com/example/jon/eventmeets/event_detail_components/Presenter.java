package com.example.jon.eventmeets.event_detail_components;


import android.os.AsyncTask;

import com.example.jon.eventmeets.model.VideoGamingEvent;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jon on 12/22/2016.
 */

class Presenter implements VideoGameSearchContract.Presenter {
    private VideoGameSearchContract.View mView;
    private List<VideoGamingEvent> mGameList;

    private static final String BASE_URL = "http://www.giantbomb.com/api/search?";
    private static final String KEY = "&api_key=76850e49a2af9255d3cbb9caec66d702dfac1521";
    private static final String PARAMS = "&resources=game&field_list=name,id,platforms,image&format=json&query=";

    Presenter(VideoGameSearchContract.View view) {
        mView = view;
        mGameList = new ArrayList<>();
    }

    @Override
    public void onSearchRequested(String query) {
        String fullRequest = BASE_URL+KEY+PARAMS+query;
        new GameSearchTask().execute(fullRequest);
    }

    private class GameSearchTask extends AsyncTask<String, Void, GiantBombRootObject> {

        @Override
        protected GiantBombRootObject doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(params[0])
                    .build();

            GiantBombRootObject result;

            try {
                Response response = client.newCall(request).execute();

                JSONObject json = new JSONObject(response.body().string());

                Gson gson = new Gson();
                result = gson.fromJson(json.toString(), GiantBombRootObject.class);
                return result;
            } catch(IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(GiantBombRootObject s) {
            mGameList = s.getResults();
            mView.displaySearchResults(mGameList);
        }
    }
}
