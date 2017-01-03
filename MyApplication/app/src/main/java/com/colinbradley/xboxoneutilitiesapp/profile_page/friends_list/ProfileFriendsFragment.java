package com.colinbradley.xboxoneutilitiesapp.profile_page.friends_list;

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

import com.colinbradley.xboxoneutilitiesapp.MainActivity;
import com.colinbradley.xboxoneutilitiesapp.R;
import com.colinbradley.xboxoneutilitiesapp.profile_page.ProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by colinbradley on 12/19/16.
 */

public class ProfileFriendsFragment extends Fragment implements FriendsListAdapter.OnItemSelectedListener{
    public static final String TAG = "FriendsFragment";

    RecyclerView mRV;
    FriendsListAdapter mAdapter;
    List<Friend> mFriendsList;
    AsyncTask<Void,Void,Void> mTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFriendsList = new ArrayList<>();

        mTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .headers(MainActivity.mHeaders)
                        .url("https://xboxapi.com/v2/" + ProfileActivity.mXUID + "/friends")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    Log.d(TAG, "doInBackground: Gathering Friends");
                    JSONArray friendsArray = new JSONArray(response.body().string());
                    for (int i = 0; i < friendsArray.length(); i++){
                        String gamertag = friendsArray.getJSONObject(i).getString("Gamertag");
                        String picURL = friendsArray.getJSONObject(i).getString("GameDisplayPicRaw");
                        long xuid = friendsArray.getJSONObject(i).getLong("id");
                        mFriendsList.add(new Friend(gamertag,picURL,xuid));
                        Log.d(TAG, "doInBackground: friend added GT -- " + gamertag + " XUID -- " + xuid);
                        Collections.sort(mFriendsList, Friend.gamertagComparator);
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

        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        mRV = (RecyclerView)rootView.findViewById(R.id.friends_rv);
        mRV.setLayoutManager(new LinearLayoutManager(rootView.getContext(),
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new FriendsListAdapter(mFriendsList,rootView.getContext(), ProfileFriendsFragment.this);
        mRV.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onItemSelected(long id) {
        Log.d(TAG, "onItemSelected: --------");

        String idAsString = String.valueOf(id);

        Log.d(TAG, "onItemSelected: id before parsing == " + id);
        Log.d(TAG, "onItemSelected: id being passed -- " + idAsString);

        Intent intent = new Intent(this.getContext(), ProfileActivity.class);
        intent.putExtra("xuid", idAsString);
        startActivity(intent);
    }
}
