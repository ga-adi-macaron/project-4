package com.colinbradley.xboxoneutilitiesapp.store_page.xbox_marketplace;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.colinbradley.xboxoneutilitiesapp.MainActivity;
import com.colinbradley.xboxoneutilitiesapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by colinbradley on 12/29/16.
 */

public class XBMarketplaceFragment extends Fragment implements XBMarketplaceAdapter.OnItemSelectedListener {
    public static final String TAG = "XBMarketplaceFragment";

    XBMarketplaceAdapter mAdapter;
    RecyclerView mRV;
    List<Game> mGameList;
    AsyncTask<Void,Void,Void> mTask;
    ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mGameList = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_xbox_marketplace, container, false);

        mProgressBar = (ProgressBar)v.findViewById(R.id.xbm_progressbar);
        mTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .headers(MainActivity.mHeaders)
                        .url("https://xboxapi.com/v2/browse-marketplace/games/")
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    JSONObject responseObj = new JSONObject(response.body().string());

                    JSONArray gamesList = responseObj.getJSONArray("Items");

                    for (int i = 0; i < gamesList.length(); i++){
                        String title = gamesList.getJSONObject(i).getString("Name");
                        String devName = gamesList.getJSONObject(i).getString("DeveloperName");
                        String gameID = gamesList.getJSONObject(i).getString("ID");
                        String imgURL = gamesList.getJSONObject(i).getJSONArray("Images").getJSONObject(0).getString("Url");

                        Game game = new Game(title, gameID, imgURL, devName);
                        mGameList.add(game);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }.execute();


        mRV = (RecyclerView)v.findViewById(R.id.xbm_recyclerview);
        mRV.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new XBMarketplaceAdapter(mGameList, v.getContext(), XBMarketplaceFragment.this);
        mRV.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onItemSelected(String id) {
        Log.d(TAG, "onItemSelected: " + id);
        Intent intent = new Intent(getContext(),XBMarketplaceGameDetails.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
