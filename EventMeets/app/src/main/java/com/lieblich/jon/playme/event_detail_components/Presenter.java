package com.lieblich.jon.playme.event_detail_components;


import android.os.AsyncTask;

import com.lieblich.jon.playme.model.GameResultObject;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

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
    private List<GameResultObject> mGameList;

    private static final String BASE_URL = "https://igdbcom-internet-game-database-v1.p.mashape.com/games/?";
    private static final String KEY = "E9pRHWwFlJmshdOJGsvVPo718hyXp1qzb0djsntnV1Kaal2dcL";
    private static final String PARAMS = "limit=20&fields=id,name,release_dates,screenshots,cover,summary&search=";
    private static final String KEY_HEADER = "X-Mashape-Key";
    private static final String TYPE_HEADER = "Accept";
    private static final String TYPE = "application/json";

//    private static final String BASE_URL = "http://www.giantbomb.com/api/search?";
//    private static final String KEY = "&api_key=76850e49a2af9255d3cbb9caec66d702dfac1521";
//    private static final String PARAMS = "&resources=game&field_list=name,id,platforms,image&format=json&query=";

    Presenter(VideoGameSearchContract.View view) {
        mView = view;
        mGameList = new ArrayList<>();
    }

    @Override
    public void onSearchRequested(String query) {
        String fullRequest = BASE_URL+PARAMS+query;
        new GameSearchTask().execute(fullRequest);
    }

    private class GameSearchTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(params[0])
                    .addHeader(KEY_HEADER, KEY)
                    .addHeader(TYPE_HEADER, TYPE)
                    .build();


            try {
                Response response = client.newCall(request).execute();

                JSONArray root = new JSONArray(response.body().string());

                for(int i=0;i<root.length();i++) {
                    Gson gson = new Gson();
                    GameResultObject game = gson.fromJson(root.getJSONObject(i).toString(), GameResultObject.class);
//                    if(game.hasSufficientData())
                        mGameList.add(game);
                }
            } catch(IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            mView.displaySearchResults(mGameList);
        }
    }
}
