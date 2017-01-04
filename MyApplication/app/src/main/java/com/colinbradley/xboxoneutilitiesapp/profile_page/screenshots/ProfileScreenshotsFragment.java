package com.colinbradley.xboxoneutilitiesapp.profile_page.screenshots;

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
import android.widget.Toast;

import com.colinbradley.xboxoneutilitiesapp.FullscreenImageActivity;
import com.colinbradley.xboxoneutilitiesapp.MainActivity;
import com.colinbradley.xboxoneutilitiesapp.R;
import com.colinbradley.xboxoneutilitiesapp.profile_page.ProfileActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;

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

public class ProfileScreenshotsFragment extends Fragment implements ScreenshotsAdapter.OnItemSelectedListener{
    public static final String TAG = "ScreenshotsFragment";

    List<Screenshot> mScreenshotList;
    AsyncTask<Void,Void,Void> mTask;
    ScreenshotsAdapter mAdapter;
    RecyclerView mRV;

    ShareDialog mShareDialog;
    CallbackManager mCallbackManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mScreenshotList = new ArrayList<>();

        mTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .headers(MainActivity.mHeaders)
                        .url("https://xboxapi.com/v2/" + ProfileActivity.mXUID + "/screenshots")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    JSONArray screenshotJSONarray = new JSONArray(response.body().string());
                    for (int i = 0; i < screenshotJSONarray.length(); i++){
                        JSONObject screenshotOBJ = screenshotJSONarray.getJSONObject(i);
                        JSONArray urlInfo = screenshotOBJ.getJSONArray("screenshotUris");

                        String title = screenshotOBJ.getString("screenshotName");
                        String game = screenshotOBJ.getString("titleName");
                        String description = screenshotOBJ.getString("userCaption");
                        String screenshotURL = urlInfo.getJSONObject(0).getString("uri");

                        int j = i + 1;
                        if (title.equals("")){
                            title = "Screenshot #" + j;
                        }
                        if (description.equals("")){
                            description = "no description";
                        }

                        Log.d(TAG, "doInBackground: created new SS: " + screenshotURL);
                        Log.d(TAG, "doInBackground: ^^SS name: " + title);
                        Log.d(TAG, "doInBackground: ^^SS description: " + description);
                        Log.d(TAG, "doInBackground: ^^game SS came from: " + game);

                        mScreenshotList.add(new Screenshot(title,description,screenshotURL,game));
                        Log.d(TAG, "doInBackground: added clip...size = " + mScreenshotList.size());
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

        mCallbackManager = CallbackManager.Factory.create();
        mShareDialog = new ShareDialog(this);
        mShareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(getActivity().getApplicationContext(), "Article Shared", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity().getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        View rootView = inflater.inflate(R.layout.fragment_screenshots,container,false);

        mRV = (RecyclerView)rootView.findViewById(R.id.screenshot_rv);
        mRV.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new ScreenshotsAdapter(mScreenshotList, ProfileScreenshotsFragment.this, rootView.getContext());
        mRV.setAdapter(mAdapter);

        return rootView;

    }

    @Override
    public void onItemSelected(String imgUrl) {
        Intent intent = new Intent(getContext(), FullscreenImageActivity.class);
        intent.putExtra("url", imgUrl);
        startActivity(intent);
    }
}
