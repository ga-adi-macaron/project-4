package com.crocusgames.destinyinventorymanager.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crocusgames.destinyinventorymanager.AccountInfoObjects.AccountMainObject;
import com.crocusgames.destinyinventorymanager.AccountInfoObjects.Character;
import com.crocusgames.destinyinventorymanager.AppConstants;
import com.crocusgames.destinyinventorymanager.CharacterInfoObject;
import com.crocusgames.destinyinventorymanager.CharacterInfoSingleton;
import com.crocusgames.destinyinventorymanager.DatabaseObjects.AssetHelper;
import com.crocusgames.destinyinventorymanager.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.crocusgames.destinyinventorymanager.AppConstants.ACCESS_TOKEN_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.ACCESS_TOKEN_SAVE_TIME;
import static com.crocusgames.destinyinventorymanager.AppConstants.AUTHORIZATION_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.LOGIN_SAVE_TIME;
import static com.crocusgames.destinyinventorymanager.AppConstants.REFRESH_TOKEN_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.TIME_APP_PAUSED;
import static com.crocusgames.destinyinventorymanager.AppConstants.USER_PREFERENCES;
import static com.crocusgames.destinyinventorymanager.AppConstants.X_API_KEY_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.X_API_KEY_VALUE;

public class MainActivity extends AppCompatActivity {
    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES,
                Context.MODE_PRIVATE);

        String accessToken = sharedPreferences.getString(ACCESS_TOKEN_NAME,"");
        String refreshToken = sharedPreferences.getString(REFRESH_TOKEN_NAME, "");
        Long accessTokenSaveTime = sharedPreferences.getLong(ACCESS_TOKEN_SAVE_TIME, -1);
        Long loginSaveTime = sharedPreferences.getLong(LOGIN_SAVE_TIME, -1);

        LinearLayout progressBarLayout = (LinearLayout) findViewById(R.id.progress_bar_main);
        TextView title = (TextView) findViewById(R.id.title);
        TextView description = (TextView) findViewById(R.id.description);
        TextView loginButton = (TextView) findViewById(R.id.button_login);
        TextView pleaseLogin = (TextView) findViewById(R.id.textview_please_login);

        Date date = new Date(System.currentTimeMillis());
        long currentDate = date.getTime();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(TIME_APP_PAUSED, currentDate);
        editor.commit();

        getSupportActionBar().hide();

        Typeface destinyTypeface = Typeface.createFromAsset(getAssets(),
                "fonts/futurist.TTF");
        title.setTypeface(destinyTypeface);
        description.setTypeface(destinyTypeface);
        title.setText("THE\nVAULT");
        description.setText("item manager");

        Log.d(AppConstants.TAG, "onCreate: " + secondsPassed(accessTokenSaveTime));

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (!accessToken.equals("")) {
                if (secondsPassed(loginSaveTime) < 30240000) {
                    //API restriction; if 30 mins has passed you need to refresh the access token; if
                    //90 days has passed since last login; user needs to login again.
                    //Toast.makeText(this, "seconds passed < 350 days no login", Toast.LENGTH_SHORT).show();
                    if (secondsPassed(accessTokenSaveTime) >= 1900 && secondsPassed(accessTokenSaveTime) < 7775000) {
                        refreshAccessToken(refreshToken);
                        //Toast.makeText(this, "30 mins passed and smaller < 90 days no login", Toast.LENGTH_SHORT).show();
                        pleaseLogin.setVisibility(View.GONE);
                        loginButton.setVisibility(View.GONE);
                        progressBarLayout.setVisibility(View.VISIBLE);
                    } else if (secondsPassed(accessTokenSaveTime) >= 7775000) {
                        //Log.d(AppConstants.TAG, "onCreate: 90 days passed login");
                        //90 days has passed since last login do not redirect; let user login again.
                    } else if (secondsPassed(accessTokenSaveTime) < 1900) {
                        //
                        //Toast.makeText(this, "< 30 mins just go to the char election", Toast.LENGTH_SHORT).show();
                        getAccountDetails(accessToken);
                        pleaseLogin.setVisibility(View.GONE);
                        loginButton.setVisibility(View.GONE);
                        progressBarLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    //Toast.makeText(this, "350 days passed login", Toast.LENGTH_SHORT).show();
                    //350 days have passed since login so force user login again.
                }
            } else {
                //Toast.makeText(this, "First timer login", Toast.LENGTH_SHORT).show();
                //First timer; no need to redirect.
            }
        } else {
            Toast.makeText(MainActivity.this, "You are offline. Please go online and try again.", Toast.LENGTH_SHORT).show();
        }

        AssetHelper dbSetup = new AssetHelper(this);
        dbSetup.getReadableDatabase();

        loginButton.setTypeface(destinyTypeface);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer player = new MediaPlayer();
                AssetFileDescriptor afd = null;
                try {
                    afd = getAssets().openFd("character_select.mp3");
                    player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "You are offline. Please go online and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public Long secondsPassed(Long accessTokenSaveTime) {
        Date date = new Date(System.currentTimeMillis());
        long currentDate = date.getTime();
        Long difference = currentDate - accessTokenSaveTime;
        return difference / 1000;
    }

    public void refreshAccessToken(final String refreshToken) {
        RequestQueue queue = Volley.newRequestQueue(this);

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

                    //Save Access Token to SharedPreferences:
                    SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ACCESS_TOKEN_NAME, accessTokenValue);
                    editor.putString(REFRESH_TOKEN_NAME, refreshTokenValue);
                    Date date = new Date(System.currentTimeMillis());
                    long currentDate = date.getTime();
                    editor.putLong(ACCESS_TOKEN_SAVE_TIME, currentDate);
                    editor.commit();

                    getAccountDetails(accessTokenValue);

//                    Intent intent = new Intent(MainActivity.this, CharacterSelectionActivity.class);
//                    startActivity(intent);

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

    public void getAccountDetails(final String accessToken) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = AppConstants.GET_CHARACTERS_URL;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                AccountMainObject mainObject = gson.fromJson(response, AccountMainObject.class);

                CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();

                try {
                    List<Character> characters = mainObject.getResponse().getDestinyAccounts().get(0).getCharacters();
                    Log.d(AppConstants.TAG, "Character Quantity " + characters.size());

                    String membershipId = mainObject.getResponse().getDestinyAccounts().get(0).
                            getUserInfo().getMembershipId();
                    Long membershipType = mainObject.getResponse().getDestinyAccounts().get(0).
                            getUserInfo().getMembershipType();

                    characterInfo.setMembershipId(membershipId);
                    characterInfo.setMembershipType(membershipType);

                    SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(AppConstants.MEMBERSHIP_ID, membershipId);
                    editor.putLong(AppConstants.MEMBERSHIP_TYPE, membershipType);
                    editor.putInt(AppConstants.NUMBER_OF_CHARS, characters.size());
                    switch (characters.size()) {
                        case 1:
                            editor.putString(AppConstants.CHAR0_ID, characters.get(0).getCharacterId());
                            break;
                        case 2:
                            editor.putString(AppConstants.CHAR0_ID, characters.get(0).getCharacterId());
                            editor.putString(AppConstants.CHAR1_ID, characters.get(1).getCharacterId());
                            break;
                        case 3:
                            editor.putString(AppConstants.CHAR0_ID, characters.get(0).getCharacterId());
                            editor.putString(AppConstants.CHAR1_ID, characters.get(1).getCharacterId());
                            editor.putString(AppConstants.CHAR2_ID, characters.get(2).getCharacterId());
                            break;
                        default:
                            break;
                    }
                    editor.commit();

                    //BİRŞEYLER PATLARSA AŞAĞIDAKİNİ UNCOMMENTLE
                    characterInfo.clearAllCharacterList();

                    for (int i = 0; i < characters.size(); i++) {
                        String className = characters.get(i).getCharacterClass().getClassName();
                        String backgroundUrl = characters.get(i).getBackgroundPath();
                        String emblemUrl = characters.get(i).getEmblemPath();
                        String characterId = characters.get(i).getCharacterId();
                        String genderName = characters.get(i).getGender().getGenderName();
                        String raceName = characters.get(i).getRace().getRaceName();
                        String lightLevel = characters.get(i).getPowerLevel().toString();
                        String normalLevel = characters.get(i).getLevel().toString();

                        characterInfo.addToCharacterList(new CharacterInfoObject(backgroundUrl, characterId,
                                className, emblemUrl, genderName, lightLevel, normalLevel, raceName));
                    }

                    characterInfo.setSelectedCharacter(0);
                    Intent intent = new Intent(MainActivity.this, CharInvActivity.class);
                    startActivity(intent);

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Unable to get account information. Please try again later.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(AppConstants.TAG, "onErrorResponse: Failed");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(X_API_KEY_NAME, X_API_KEY_VALUE);
                params.put(AUTHORIZATION_NAME, "Bearer " + accessToken);
                return params;
            }
        };
        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        if (!isBackPressed) {
            isBackPressed = true;
            Toast.makeText(this, "Press back again to quit.", Toast.LENGTH_SHORT).show();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    isBackPressed = false;
                }
            }, 3000);
        } else {
            this.finishAffinity();
        }
    }
}
