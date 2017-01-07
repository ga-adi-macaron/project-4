package com.crocusgames.destinyinventorymanager.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.crocusgames.destinyinventorymanager.CharInventoryObjects.CharViewPagerAdapter;
import com.crocusgames.destinyinventorymanager.CharacterInfoObject;
import com.crocusgames.destinyinventorymanager.CharacterInfoSingleton;
import com.crocusgames.destinyinventorymanager.DestinyApiCallObject;
import com.crocusgames.destinyinventorymanager.ItemCompleteObject;
import com.crocusgames.destinyinventorymanager.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

public class CharInvActivity extends AppCompatActivity {
    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char_inv);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(AppConstants.TAG, "onCreate: CHARINV ACT created");

        final CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();

        final ImageView toolbarChar0Icon = (ImageView) findViewById(R.id.toolbar_char0_icon);
        final ImageView toolbarChar1Icon = (ImageView) findViewById(R.id.toolbar_char1_icon);
        final ImageView toolbarChar2Icon = (ImageView) findViewById(R.id.toolbar_char2_icon);
        ImageView toolbarVaultIcon = (ImageView) findViewById(R.id.toolbar_vault_icon);
        final TextView toolbarChar0Name = (TextView) findViewById(R.id.toolbar_char0_name);
        final TextView toolbarChar1Name = (TextView) findViewById(R.id.toolbar_char1_name);
        final TextView toolbarChar2Name = (TextView) findViewById(R.id.toolbar_char2_name);
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
            Toast.makeText(CharInvActivity.this, "You are offline. Please go online and try again.", Toast.LENGTH_SHORT).show();
        }

        final ArrayList<CharacterInfoObject> otherCharIds = new ArrayList<>();
        String membershipType = characterInfo.getMembershipType().toString();

        if (membershipType.equals("-5")) {
            Log.d(AppConstants.TAG, "CharacterList: 0");
            Intent intent = new Intent(CharInvActivity.this, MainActivity.class);
            startActivity(intent);
        } else {

            //Below is for testing purposes.
//        int a = 0;
//        for (int i = 0; i < characterInfo.getCharacterList().size(); i++) {
//            otherCharIds.add(characterInfo.getCharacterList().get(i));
//            a++;
//            if (a == 2) {
//                break;
//            }
//        }

            for (int i = 0; i < characterInfo.getCharacterList().size(); i++) {
                otherCharIds.add(characterInfo.getCharacterList().get(i));
            }

            Log.d(AppConstants.TAG, "Toolbar Char Size " + otherCharIds.size());

            final String selectedId = characterInfo.getCharacterList().get(characterInfo.getSelectedCharacter()).getCharacterId();

            switch (otherCharIds.size()) {
                case 1:
                    Picasso.with(this).load(BUNGIE_NET_START_URL + otherCharIds.get(0).getEmblemUrl()).into(toolbarChar0Icon);
                    toolbarChar0Name.setText(otherCharIds.get(0).getClassName());
                    toolbarChar1Layout.setVisibility(View.GONE);
                    toolbarChar2Layout.setVisibility(View.GONE);
                    toolbarVaultName.setText("Vault");
                    Picasso.with(this).load(VAULT_ICON_URL).into(toolbarVaultIcon);
                    break;
                case 2:
                    Picasso.with(this).load(BUNGIE_NET_START_URL + otherCharIds.get(0).getEmblemUrl()).into(toolbarChar0Icon);
                    toolbarChar0Name.setText(otherCharIds.get(0).getClassName());
                    Picasso.with(this).load(BUNGIE_NET_START_URL + otherCharIds.get(1).getEmblemUrl()).into(toolbarChar1Icon);
                    toolbarChar1Name.setText(otherCharIds.get(1).getClassName());
                    toolbarChar2Layout.setVisibility(View.GONE);
                    toolbarVaultName.setText("Vault");
                    Picasso.with(this).load(VAULT_ICON_URL).into(toolbarVaultIcon);
                    break;
                case 3:
                    Picasso.with(this).load(BUNGIE_NET_START_URL + otherCharIds.get(0).getEmblemUrl()).into(toolbarChar0Icon);
                    toolbarChar0Name.setText(otherCharIds.get(0).getClassName());
                    Picasso.with(this).load(BUNGIE_NET_START_URL + otherCharIds.get(1).getEmblemUrl()).into(toolbarChar1Icon);
                    toolbarChar1Name.setText(otherCharIds.get(1).getClassName());
                    Picasso.with(this).load(BUNGIE_NET_START_URL + otherCharIds.get(2).getEmblemUrl()).into(toolbarChar2Icon);
                    toolbarChar2Name.setText(otherCharIds.get(2).getClassName());
                    toolbarVaultName.setText("Vault");
                    Picasso.with(this).load(VAULT_ICON_URL).into(toolbarVaultIcon);
                    break;
            }

            int charIndex = 0;

            for (int i = 0; i < otherCharIds.size(); i++) {
                if (selectedId.equals(otherCharIds.get(i).getCharacterId())) {
                    charIndex = i;
                }
            }

            switch (charIndex) {
                case 0:
                    toolbarChar0Name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    toolbarChar0Name.setTextColor(getResources().getColor(R.color.item_color_exotic));
                    break;
                case 1:
                    toolbarChar1Name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    toolbarChar1Name.setTextColor(getResources().getColor(R.color.item_color_exotic));
                    break;
                case 2:
                    toolbarChar2Name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    toolbarChar2Name.setTextColor(getResources().getColor(R.color.item_color_exotic));
                    break;
            }

            String bannerImageUrl = characterInfo.getCharacterList().get(characterInfo.getSelectedCharacter()).getBackgroundUrl();

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    toolbar.setBackground(new BitmapDrawable(getApplication().getResources(), bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Log.d(AppConstants.TAG, "onBitmapFailed: ");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    Log.d(AppConstants.TAG, "onPrepareLoad: ");
                }
            };

            toolbar.setTag(target);

            Picasso.with(this).load(BUNGIE_NET_START_URL + bannerImageUrl).into(target);

            final LinearLayout progressBar = (LinearLayout) findViewById(R.id.progressBarLayout);
            progressBar.setVisibility(View.VISIBLE);

            SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES,
                    Context.MODE_PRIVATE);

            String accessToken = sharedPreferences.getString(ACCESS_TOKEN_NAME, "");

            final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
            tabLayout.setupWithViewPager(viewPager);

            DestinyApiCallObject apiCallObject = new DestinyApiCallObject(getApplicationContext());
            //apiCallObject.getCharacterInventory(accessToken);
            //apiCallObject.getCharacterInventoryFromDb(accessToken);
            //apiCallObject.getCharacterInventoryFromDbByList(accessToken);
            apiCallObject.getCharacterInventoryFromDbAllAtOnce(accessToken);

            Intent intent = getIntent();
            final Integer tabNumber = intent.getIntExtra(AppConstants.TAB_NUMBER, -1);

            if (tabNumber != -1) {
                viewPager.setCurrentItem(tabNumber);
            }

            final CharViewPagerAdapter adapter = new CharViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(tabNumber);

            switch (characterInfo.getSelectedCharacter()) {
                case 0:
                    toolbarChar0Icon.setBackgroundResource(R.drawable.border_toolbar_selected);
                    break;
                case 1:
                    toolbarChar1Icon.setBackgroundResource(R.drawable.border_toolbar_selected);
                    break;
                case 2:
                    toolbarChar2Icon.setBackgroundResource(R.drawable.border_toolbar_selected);
                    break;
                default:
                    break;
            }

            toolbarChar0Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!selectedId.equals(characterInfo.getCharacterList().get(0).getCharacterId())) {
                        characterInfo.setSelectedCharacter(0);
                        finish();
                        Intent intent = new Intent(CharInvActivity.this, CharInvActivity.class);
                        intent.putExtra(AppConstants.TAB_NUMBER, viewPager.getCurrentItem());
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                    }
                }
            });

            toolbarChar1Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!selectedId.equals(characterInfo.getCharacterList().get(1).getCharacterId())) {
                        characterInfo.setSelectedCharacter(1);
                        finish();
                        Intent intent = new Intent(CharInvActivity.this, CharInvActivity.class);
                        intent.putExtra(AppConstants.TAB_NUMBER, viewPager.getCurrentItem());
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                    }
                }
            });

            toolbarChar2Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!selectedId.equals(characterInfo.getCharacterList().get(2).getCharacterId())) {
                        characterInfo.setSelectedCharacter(2);
                        finish();
                        Intent intent = new Intent(CharInvActivity.this, CharInvActivity.class);
                        intent.putExtra(AppConstants.TAB_NUMBER, viewPager.getCurrentItem());
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                    }
                }
            });

            toolbarVaultLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    Intent intent = new Intent(CharInvActivity.this, VaultActivity.class);
                    intent.putExtra(AppConstants.TAB_NUMBER, viewPager.getCurrentItem());
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                }
            });

            apiCallObject.setDestinyApiResponseListener(new DestinyApiCallObject.DestinyApiResponseListener() {
                @Override
                public void onObjectReady() {
                    try {
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                    adapter.setEmblemEquippedListener(new CharViewPagerAdapter.EmblemEquippedListener() {
                        @Override
                        public void onEmblemEquipped(ItemCompleteObject equippedEmblem) {
                            Picasso.with(CharInvActivity.this).load(BUNGIE_NET_START_URL + equippedEmblem.getBackgroundUrl()).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    toolbar.setBackground(new BitmapDrawable(getApplication().getResources(), bitmap));
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                    Log.d(AppConstants.TAG, "onBitmapFailed: ");
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    Log.d(AppConstants.TAG, "onPrepareLoad: ");
                                }
                            });

                            int selectedCharNo = characterInfo.getSelectedCharacter();

                            switch (selectedCharNo) {
                                case 0:
                                    Picasso.with(CharInvActivity.this).load(BUNGIE_NET_START_URL +
                                            equippedEmblem.getIconUrl()).into(toolbarChar0Icon);
                                    break;
                                case 1:
                                    Picasso.with(CharInvActivity.this).load(BUNGIE_NET_START_URL +
                                            equippedEmblem.getIconUrl()).into(toolbarChar1Icon);
                                    break;
                                case 2:
                                    Picasso.with(CharInvActivity.this).load(BUNGIE_NET_START_URL +
                                            equippedEmblem.getIconUrl()).into(toolbarChar2Icon);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //If String selectedId returns null for some reason; send the person to char screen instead of crashing.
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES,
                Context.MODE_PRIVATE);

        String refreshToken = sharedPreferences.getString(REFRESH_TOKEN_NAME, "");
        Long accessTokenSaveTime = sharedPreferences.getLong(ACCESS_TOKEN_SAVE_TIME, -1);
        Long appPauseTime = sharedPreferences.getLong(TIME_APP_PAUSED, -1L);

        CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();
        String membershipType = characterInfo.getMembershipType().toString();

        //trial
        Log.d(AppConstants.TAG, "onResume seconds passed: " + secondsPassed(appPauseTime));
        if (membershipType.equals("-5")) {
            Log.d(AppConstants.TAG, "CharacterList: 0");
            Intent intent = new Intent(CharInvActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            if (secondsPassed(appPauseTime) > 60 && secondsPassed(accessTokenSaveTime) <= 1900) {
                finish();
                Intent intent = new Intent(this, CharInvActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0, 0);
                startActivity(intent);
            } else if (secondsPassed(appPauseTime) > 60 && secondsPassed(accessTokenSaveTime) > 1900) {
                refreshAccessToken(refreshToken, true);
                //burayı condition'a bağla. true iken refresh should not be working.
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
                        Intent intent = new Intent(CharInvActivity.this, CharInvActivity.class);
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
        Log.d(AppConstants.TAG, "onPause: CALLED");

        SharedPreferences sharedPreferences =  getSharedPreferences(USER_PREFERENCES,
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
            Intent intent = new Intent(CharInvActivity.this, SettingsActivity.class);
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