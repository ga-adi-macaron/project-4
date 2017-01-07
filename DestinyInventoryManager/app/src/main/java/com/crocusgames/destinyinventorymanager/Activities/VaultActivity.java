package com.crocusgames.destinyinventorymanager.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crocusgames.destinyinventorymanager.AppConstants;
import com.crocusgames.destinyinventorymanager.CharacterInfoObject;
import com.crocusgames.destinyinventorymanager.CharacterInfoSingleton;
import com.crocusgames.destinyinventorymanager.DestinyApiCallObject;
import com.crocusgames.destinyinventorymanager.R;
import com.crocusgames.destinyinventorymanager.VaultInventoryObjects.VaultViewPagerAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.crocusgames.destinyinventorymanager.AppConstants.ACCESS_TOKEN_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.ACCESS_TOKEN_SAVE_TIME;
import static com.crocusgames.destinyinventorymanager.AppConstants.BUNGIE_NET_START_URL;
import static com.crocusgames.destinyinventorymanager.AppConstants.REFRESH_TOKEN_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.TIME_APP_PAUSED;
import static com.crocusgames.destinyinventorymanager.AppConstants.USER_PREFERENCES;
import static com.crocusgames.destinyinventorymanager.AppConstants.VAULT_ICON_URL;

public class VaultActivity extends AppCompatActivity {
    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView toolbarChar0Icon = (ImageView) findViewById(R.id.toolbar_char0_icon);
        ImageView toolbarChar1Icon = (ImageView) findViewById(R.id.toolbar_char1_icon);
        ImageView toolbarChar2Icon = (ImageView) findViewById(R.id.toolbar_char2_icon);
        ImageView toolbarVaultIcon = (ImageView) findViewById(R.id.toolbar_vault_icon);
        TextView toolbarChar0Name = (TextView) findViewById(R.id.toolbar_char0_name);
        TextView toolbarChar1Name = (TextView) findViewById(R.id.toolbar_char1_name);
        TextView toolbarChar2Name = (TextView) findViewById(R.id.toolbar_char2_name);
        TextView toolbarVaultName = (TextView) findViewById(R.id.toolbar_vault_name);
        LinearLayout toolbarChar0Layout = (LinearLayout) findViewById(R.id.toolbar_char0_layout);
        LinearLayout toolbarChar1Layout = (LinearLayout) findViewById(R.id.toolbar_char1_layout);
        LinearLayout toolbarChar2Layout = (LinearLayout) findViewById(R.id.toolbar_char2_layout);
        LinearLayout toolbarVaultLayout = (LinearLayout) findViewById(R.id.toolbar_vault_layout);
        toolbarChar1Name.getBackground().setAlpha(120);
        toolbarChar2Name.getBackground().setAlpha(120);
        toolbarVaultName.getBackground().setAlpha(120);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            Toast.makeText(VaultActivity.this, "You are offline. Please go online and try again.", Toast.LENGTH_SHORT).show();
        }

        final CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();
        String membershipType = characterInfo.getMembershipType().toString();

        if (membershipType.equals("-5")) {
            Log.d(AppConstants.TAG, "CharacterList: 0");
            Intent intent = new Intent(VaultActivity.this, MainActivity.class);
            startActivity(intent);
        }

        //Below is for testing purposes.
//        List<CharacterInfoObject> characterIdsList = new ArrayList<>();
//
//        int a = 0;
//        for (int i = 0; i < characterInfo.getCharacterList().size(); i++) {
//            characterIdsList.add(characterInfo.getCharacterList().get(i));
//            a++;
//            if (a == 2) {
//                break;
//            }
//        }

        final List<CharacterInfoObject> characterIdsList = characterInfo.getCharacterList();

        switch (characterIdsList.size()) {
            case 1:
                Picasso.with(this).load(BUNGIE_NET_START_URL + characterIdsList.get(0).getEmblemUrl()).into(toolbarChar0Icon);
                toolbarChar0Name.setText(characterIdsList.get(0).getClassName());
                toolbarChar0Name.setTypeface(Typeface.DEFAULT);
                toolbarChar1Layout.setVisibility(View.GONE);
                toolbarChar2Layout.setVisibility(View.GONE);
                toolbarVaultName.setText("Vault");
                Picasso.with(this).load(VAULT_ICON_URL).into(toolbarVaultIcon);
                toolbarVaultName.setTextColor(getResources().getColor(R.color.item_color_exotic));
                toolbarVaultName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            case 2:
                Picasso.with(this).load(BUNGIE_NET_START_URL + characterIdsList.get(0).getEmblemUrl()).into(toolbarChar0Icon);
                toolbarChar0Name.setText(characterIdsList.get(0).getClassName());
                toolbarChar0Name.setTypeface(Typeface.DEFAULT);
                Picasso.with(this).load(BUNGIE_NET_START_URL + characterIdsList.get(1).getEmblemUrl()).into(toolbarChar1Icon);
                toolbarChar1Name.setText(characterIdsList.get(1).getClassName());
                toolbarChar2Layout.setVisibility(View.GONE);
                toolbarVaultName.setText("Vault");
                Picasso.with(this).load(VAULT_ICON_URL).into(toolbarVaultIcon);
                toolbarVaultName.setTextColor(getResources().getColor(R.color.item_color_exotic));
                toolbarVaultName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            case 3:
                Picasso.with(this).load(BUNGIE_NET_START_URL + characterIdsList.get(0).getEmblemUrl()).into(toolbarChar0Icon);
                toolbarChar0Name.setText(characterIdsList.get(0).getClassName());
                toolbarChar0Name.setTypeface(Typeface.DEFAULT);
                Picasso.with(this).load(BUNGIE_NET_START_URL + characterIdsList.get(1).getEmblemUrl()).into(toolbarChar1Icon);
                toolbarChar1Name.setText(characterIdsList.get(1).getClassName());
                Picasso.with(this).load(BUNGIE_NET_START_URL + characterIdsList.get(2).getEmblemUrl()).into(toolbarChar2Icon);
                toolbarChar2Name.setText(characterIdsList.get(2).getClassName());
                toolbarVaultName.setText("Vault");
                Picasso.with(this).load(VAULT_ICON_URL).into(toolbarVaultIcon);
                toolbarVaultName.setTextColor(getResources().getColor(R.color.item_color_exotic));
                toolbarVaultName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
        }

        final LinearLayout progressBarLayout = (LinearLayout) findViewById(R.id.progressBarLayout);
        progressBarLayout.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES,
                Context.MODE_PRIVATE);

        String accessToken = sharedPreferences.getString(ACCESS_TOKEN_NAME,"");

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        final VaultViewPagerAdapter adapter = new VaultViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        DestinyApiCallObject destinyApiCallObject = new DestinyApiCallObject(this);
        destinyApiCallObject.getVaultInventoryFromDbAllAtOnce(accessToken);

        Intent intent = getIntent();
        final Integer tabNumber = intent.getIntExtra(AppConstants.TAB_NUMBER, -1);

        if (tabNumber != -1) {
            viewPager.setCurrentItem(tabNumber);
        }

        //

        toolbarChar0Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                characterInfo.setSelectedCharacter(0);
                finish();
                Intent intent = new Intent(VaultActivity.this, CharInvActivity.class);
                intent.putExtra(AppConstants.TAB_NUMBER, viewPager.getCurrentItem());
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });

        toolbarChar1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                characterInfo.setSelectedCharacter(1);
                finish();
                Intent intent = new Intent(VaultActivity.this, CharInvActivity.class);
                intent.putExtra(AppConstants.TAB_NUMBER, viewPager.getCurrentItem());
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });

        toolbarChar2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                characterInfo.setSelectedCharacter(2);
                finish();
                Intent intent = new Intent(VaultActivity.this, CharInvActivity.class);
                intent.putExtra(AppConstants.TAB_NUMBER, viewPager.getCurrentItem());
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });

        destinyApiCallObject.setDestinyApiResponseListener(new DestinyApiCallObject.DestinyApiResponseListener() {
            @Override
            public void onObjectReady() {
                progressBarLayout.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Singleton'ın destroy olduğu durumda Mainactivity'ye yönlendirmeyi unutma;
        //Şu şekilde bakıyoruz.

        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES,
                Context.MODE_PRIVATE);

        String refreshToken = sharedPreferences.getString(REFRESH_TOKEN_NAME, "");
        Long accessTokenSaveTime = sharedPreferences.getLong(ACCESS_TOKEN_SAVE_TIME, -1);
        Long appPauseTime = sharedPreferences.getLong(TIME_APP_PAUSED, -1);

        CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();
        String membershipType = characterInfo.getMembershipType().toString();

        if (membershipType.equals("-5")) {
            Log.d(AppConstants.TAG, "CharacterList: 0");
            Intent intent = new Intent(VaultActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            if (secondsPassed(appPauseTime) > 60 && secondsPassed(accessTokenSaveTime) <= 1900) {
                finish();
                Intent intent = new Intent(this, VaultActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0, 0);
                startActivity(intent);
            } else if (secondsPassed(appPauseTime) > 60 && secondsPassed(accessTokenSaveTime) > 1900) {
                refreshAccessToken(refreshToken, true);
            } else if (secondsPassed(appPauseTime) <= 60 && secondsPassed(accessTokenSaveTime) > 1900) {
                refreshAccessToken(refreshToken, false);
            }
        }
    }

    public Long secondsPassed(Long accessTokenSaveTime) {
        Date date = new Date(System.currentTimeMillis());
        long currentDate = date.getTime();
        Long difference = currentDate - accessTokenSaveTime;
        return difference / 1000;
    }

    public void refreshAccessToken(final String refreshToken, final boolean isActivityRefreshing) {
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

                    Log.d(AppConstants.TAG, "onResponse: 30 mins passed, requesting new token on resume");

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

                    if (isActivityRefreshing) {
                        finish();
                        Intent intent = new Intent(VaultActivity.this, CharInvActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                    }

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

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Date date = new Date(System.currentTimeMillis());
        long currentDate = date.getTime();
        editor.putLong(TIME_APP_PAUSED, currentDate);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_goToSettings) {
            Intent intent = new Intent(VaultActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        return true;
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
