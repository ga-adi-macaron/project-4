package com.colinbradley.xboxoneutilitiesapp.profile_page.gameclips;

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

import com.colinbradley.xboxoneutilitiesapp.MainActivity;
import com.colinbradley.xboxoneutilitiesapp.R;
import com.colinbradley.xboxoneutilitiesapp.profile_page.ProfileActivity;

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
 * Created by colinbradley on 12/19/16.
 */

public class ProfileGameClipsFragment extends Fragment implements GameClipsAdapter.OnItemSelectedListener{
    public static final String TAG = "GameClipsFragment";
    RecyclerView mRV;
    GameClipsAdapter mAdapter;
    List<GameClip> mClipsList;
    AsyncTask<Void,Void,Void> mTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mClipsList = new ArrayList<>();

        mTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .headers(MainActivity.mHeaders)
                        .url("https://xboxapi.com/v2/" + ProfileActivity.mXUID + "/game-clips")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    JSONArray clipsJSONarray = new JSONArray(response.body().string());
                    for (int i = 0; i < clipsJSONarray.length(); i++){
                        JSONObject clipObj = clipsJSONarray.getJSONObject(i);
                        JSONArray urlInfo = clipObj.getJSONArray("gameClipUris");
                        JSONArray imgURLs = clipObj.getJSONArray("thumbnails");

                        String title = clipObj.getString("clipName");
                        String game = clipObj.getString("titleName");
                        String description = clipObj.getString("userCaption");
                        String clipURL = urlInfo.getJSONObject(0).getString("uri");
                        String imgURL = imgURLs.getJSONObject(0).getString("uri");

                        int j = i + 1;
                        if (title.equals("")){
                            title = "Clip #" + j;
                        }
                        if (description.equals("")){
                            description = "no description";
                        }

                        Log.d(TAG, "doInBackground: created new Clip: " + clipURL);
                        Log.d(TAG, "doInBackground: ^^clip name: " + title);
                        Log.d(TAG, "doInBackground: ^^clip description: " + description);
                        Log.d(TAG, "doInBackground: ^^game clip came from: " + game);

                        mClipsList.add(new GameClip(title,game,description,clipURL,imgURL));
                        Log.d(TAG, "doInBackground: added clip...size = " + mClipsList.size());
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
            }
        }.execute();

        View rootView = inflater.inflate(R.layout.fragment_gameclips, container, false);

        mRV = (RecyclerView)rootView.findViewById(R.id.gameclips_rv);
        mRV.setLayoutManager(new LinearLayoutManager(rootView.getContext(),LinearLayoutManager.VERTICAL, false));
        mAdapter = new GameClipsAdapter(mClipsList, ProfileGameClipsFragment.this, rootView.getContext());
        mRV.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onItemSelected(String url) {

    }
}
