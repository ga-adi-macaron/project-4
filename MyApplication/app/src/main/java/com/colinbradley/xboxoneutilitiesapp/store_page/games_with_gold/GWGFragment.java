package com.colinbradley.xboxoneutilitiesapp.store_page.games_with_gold;

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

public class GWGFragment extends Fragment implements GWGAdapter.OnItemSelectedListener{
    public static final String TAG = "GWGFragment";

    GWGAdapter mAdapter;
    RecyclerView mRV;
    List<GameWithGold> mGwGList;
    AsyncTask<Void,Void,Void> mTask;
    ProgressBar mProgressBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mGwGList = new ArrayList<>();
        Log.d(TAG, "onCreateView: made list");
        View rootView = inflater.inflate(R.layout.fragment_games_with_gold, container, false);

        mProgressBar = (ProgressBar)rootView.findViewById(R.id.gwg_progressbar);

        mTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();
                Log.d(TAG, "doInBackground: client made");

                Request request = new Request.Builder()
                        .headers(MainActivity.mHeaders)
                        .url("https://xboxapi.com/v2/xboxone-gold-lounge")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    Log.d(TAG, "doInBackground: Gathering Games with Gold");
                    JSONObject responseObj = new JSONObject(response.body().string());
                    Log.d(TAG, "doInBackground: Response --- " + responseObj.toString());
                    JSONArray gwgList = responseObj.getJSONArray("GamesWithGold");

                    for (int i = 0; i < gwgList.length(); i++){
                        String title = gwgList.getJSONObject(i).getString("TitleName");
                        String originalPrice = gwgList.getJSONObject(i).getString("ListPriceText");
                        String gameID = gwgList.getJSONObject(i).getString("ID");
                        String boxArtURL = gwgList.getJSONObject(i).getString("TitleImage");

                        String price = "$" + originalPrice.substring(1);

                        GameWithGold game = new GameWithGold(title, price, boxArtURL, gameID);
                        mGwGList.add(game);
                        Log.d(TAG, "doInBackground: Added game to list " + title + " - game ID " + gameID);
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

        Log.d(TAG, "onCreateView: identifiy rootview");

        mRV = (RecyclerView)rootView.findViewById(R.id.gwg_recyclerview);
        Log.d(TAG, "onCreateView: rv instanciated");
        mRV.setLayoutManager(new LinearLayoutManager(rootView.getContext(),LinearLayoutManager.VERTICAL,false));
        Log.d(TAG, "onCreateView: set layout manager");
        mAdapter = new GWGAdapter(mGwGList, GWGFragment.this, rootView.getContext());
        Log.d(TAG, "onCreateView: created adapter");
        mRV.setAdapter(mAdapter);
        Log.d(TAG, "onCreateView: set adapter");
        return rootView;


    }

    @Override
    public void onItemSelected(String id) {
        Intent intent = new Intent(getContext(),GWGDetails.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
