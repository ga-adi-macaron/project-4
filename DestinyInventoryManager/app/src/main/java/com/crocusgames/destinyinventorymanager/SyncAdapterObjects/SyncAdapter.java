package com.crocusgames.destinyinventorymanager.SyncAdapterObjects;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crocusgames.destinyinventorymanager.AppConstants;
import com.crocusgames.destinyinventorymanager.CharacterInfoSingleton;
import com.crocusgames.destinyinventorymanager.DestinyApiCallObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.crocusgames.destinyinventorymanager.AppConstants.ACCESS_TOKEN_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.ACCESS_TOKEN_SAVE_TIME;
import static com.crocusgames.destinyinventorymanager.AppConstants.CHAR0_ID;
import static com.crocusgames.destinyinventorymanager.AppConstants.CHAR1_ID;
import static com.crocusgames.destinyinventorymanager.AppConstants.CHAR2_ID;
import static com.crocusgames.destinyinventorymanager.AppConstants.GRIND_MODE_STATUS;
import static com.crocusgames.destinyinventorymanager.AppConstants.MEMBERSHIP_ID;
import static com.crocusgames.destinyinventorymanager.AppConstants.MEMBERSHIP_TYPE;
import static com.crocusgames.destinyinventorymanager.AppConstants.NUMBER_OF_CHARS;
import static com.crocusgames.destinyinventorymanager.AppConstants.REFRESH_TOKEN_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.USER_PREFERENCES;

/**
 * Created by Serkan on 30/12/16.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private CharacterInfoSingleton mCharacterInfo;

    private static final String TAG = "SyncAdapter";
    private ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s,
                              ContentProviderClient contentProviderClient, SyncResult syncResult) {

        Log.d(TAG, "onPerformSync: SYNCHING");

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(USER_PREFERENCES,
                Context.MODE_PRIVATE);

        String accessToken = sharedPreferences.getString(ACCESS_TOKEN_NAME, "");
        String membershipId = sharedPreferences.getString(MEMBERSHIP_ID, "");
        Long membershipType = sharedPreferences.getLong(MEMBERSHIP_TYPE, -5);
        Integer numberOfCharacters = sharedPreferences.getInt(NUMBER_OF_CHARS, -1);
        String char0Id = sharedPreferences.getString(CHAR0_ID, "");
        String char1Id = sharedPreferences.getString(CHAR1_ID, "");
        String char2Id = sharedPreferences.getString(CHAR2_ID, "");
        Long accessTokenSaveTime = sharedPreferences.getLong(ACCESS_TOKEN_SAVE_TIME, -1L);
        String refreshToken = sharedPreferences.getString(REFRESH_TOKEN_NAME, "");
        boolean isGrinding = sharedPreferences.getBoolean(GRIND_MODE_STATUS, false);

        if (!isGrinding) {
            ContentResolver.removePeriodicSync(account, AppConstants.AUTHORITY, Bundle.EMPTY);
            Log.d(TAG, "onPerformSync: Cancelled sync because isGrinding was not TRUE!");
        } else {
            Log.d(TAG, "Seconds passed: " + secondsPassed(accessTokenSaveTime));
            if (secondsPassed(accessTokenSaveTime) > 1900) {
                refreshAccessToken(refreshToken, membershipId, membershipType, char0Id, char1Id, char2Id);
                Log.d(TAG, "SYNC: First refreshing the access token then continuing synching.");
            } else {
                moveEngrams(accessToken, membershipId, membershipType, char0Id, char1Id, char2Id);
            }
        }
    }

    public void moveEngrams(final String accessToken, final String membershipId, final Long membershipType,
                            String char0Id, final String char1Id, final String char2Id) {
        final DestinyApiCallObject destinyApiCallObject = new DestinyApiCallObject(getContext());

        if (!char0Id.equals("")) {
            destinyApiCallObject.getCharacterInventoryFromDbForGrindMode(accessToken, membershipId,
                    membershipType.toString(), char0Id, 0);
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!char1Id.equals("")) {
                    destinyApiCallObject.getCharacterInventoryFromDbForGrindMode(accessToken, membershipId,
                            membershipType.toString(), char1Id, 1);
                }
            }
        }, 2000);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!char2Id.equals("")) {
                    destinyApiCallObject.getCharacterInventoryFromDbForGrindMode(accessToken, membershipId,
                            membershipType.toString(), char2Id, 2);
                }
            }
        }, 4000);
    }

    public Long secondsPassed(Long accessTokenSaveTime) {
        Date date = new Date(System.currentTimeMillis());
        long currentDate = date.getTime();
        Long difference = currentDate - accessTokenSaveTime;
        return difference / 1000;
    }

    public void refreshAccessToken(final String refreshToken, final String membershipId,
                                   final Long membershipType, final String char0Id,
                                   final String char1Id, final String char2Id) {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        HashMap<String, String> params = new HashMap<>();
        params.put("refreshToken", refreshToken);

        String url = AppConstants.REFRESH_TOKEN_URL;

        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject responseObject = null;
                try {
                    Log.d(AppConstants.TAG, "onResponse: " + response);
                    responseObject = response.getJSONObject("Response");
                    JSONObject accessTokenObject = responseObject.getJSONObject("accessToken");
                    String accessTokenValue = accessTokenObject.getString("value");

                    JSONObject refreshTokenObject = responseObject.getJSONObject("refreshToken");
                    String refreshTokenValue = refreshTokenObject.getString("value");

                    Log.d(AppConstants.TAG, "ACCESS_TOKEN_NAME " + accessTokenValue);
                    Log.d(AppConstants.TAG, "REFRESH_TOKEN_NAME  " + refreshTokenValue);

                    //Save Access Token to SharedPreferences:
                    SharedPreferences sharedPreferences =  getContext().getSharedPreferences(USER_PREFERENCES,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ACCESS_TOKEN_NAME, accessTokenValue);
                    editor.putString(REFRESH_TOKEN_NAME, refreshTokenValue);
                    Date date = new Date(System.currentTimeMillis());
                    long currentDate = date.getTime();
                    editor.putLong(ACCESS_TOKEN_SAVE_TIME, currentDate);
                    editor.commit();

                    moveEngrams(accessTokenValue, membershipId, membershipType, char0Id, char1Id, char2Id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(AppConstants.TAG, "onErrorResponse: " + "Get access token failed on REFRESH_TOKEN");

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(AppConstants.X_API_KEY_NAME, AppConstants.X_API_KEY_VALUE);
                return params;
            }
        };
        queue.add(request);
    }
}
