package com.crocusgames.destinyinventorymanager.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
import com.crocusgames.destinyinventorymanager.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.crocusgames.destinyinventorymanager.AppConstants.ACCESS_TOKEN_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.ACCESS_TOKEN_SAVE_TIME;
import static com.crocusgames.destinyinventorymanager.AppConstants.AUTHORIZATION_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.LOGIN_SAVE_TIME;
import static com.crocusgames.destinyinventorymanager.AppConstants.REFRESH_TOKEN_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.USER_PREFERENCES;
import static com.crocusgames.destinyinventorymanager.AppConstants.X_API_KEY_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.X_API_KEY_VALUE;

public class LoginActivity extends AppCompatActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mWebView = (WebView) findViewById(R.id.webview);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(AppConstants.REDIRECT_URL_PART) && (url.length() > 29)) {
                    int index = url.indexOf("=");
                    String code = url.substring(index+1);
                    Log.d(AppConstants.TAG, "shouldOverrideUrlLoading: " + url.length());
                    Log.d(AppConstants.TAG, "shouldOverrideUrlLoading: " + url);
                    Log.d(AppConstants.TAG, "shouldOverrideUrlLoading: " + code);

                    getAccessToken(code);

                    return true;
                } else if (url.contains(AppConstants.REDIRECT_URL_PART) && (url.length()) == 29){
                    mWebView.setVisibility(View.GONE);
                    return true;
                } else {
                    return false;
                }
            }
        });

        mWebView.loadUrl(AppConstants.AUTHORIZATION_URL);
    }

    public void getAccessToken(String code) {
        RequestQueue queue = Volley.newRequestQueue(this);

        HashMap<String, String> params = new HashMap<>();
        params.put("code", code);

        String url = AppConstants.ACCESS_TOKEN_URL;

        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject responseObject = null;
                try {
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
                    editor.putLong(LOGIN_SAVE_TIME, currentDate);
                    editor.commit();

                    getAccountDetails(accessTokenValue);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(AppConstants.TAG, "onErrorResponse: " + "Get access token failed");
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
                String membershipTypeName = characterInfo.getMembershipType().toString();

                String membershipId = mainObject.getResponse().getDestinyAccounts().get(0).
                        getUserInfo().getMembershipId();
                Long membershipType = mainObject.getResponse().getDestinyAccounts().get(0).
                        getUserInfo().getMembershipType();

                characterInfo.setMembershipId(membershipId);
                characterInfo.setMembershipType(membershipType);

//                if (membershipTypeName.equals("-5")) {
//                    //Either the API is under maintenance; or the user has no characters.
//                    //Need to display some kind of warning here
//                    Intent intent = new Intent(CharacterSelectionActivity.this, MainActivity.class);
//                    startActivity(intent);
//                }

                List<Character> characters = mainObject.getResponse().getDestinyAccounts().get(0).getCharacters();
                Log.d(AppConstants.TAG, "Character Quantity " + characters.size());

                SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
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
                Intent intent = new Intent(LoginActivity.this, CharInvActivity.class);
                startActivity(intent);
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
}
