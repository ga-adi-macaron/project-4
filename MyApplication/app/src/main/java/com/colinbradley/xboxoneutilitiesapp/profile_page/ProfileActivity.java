package com.colinbradley.xboxoneutilitiesapp.profile_page;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.colinbradley.xboxoneutilitiesapp.MainActivity;
import com.colinbradley.xboxoneutilitiesapp.R;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {
    public static final String TAG = "ProfileActivity";

    TextView mGSview, mGTview, mXUIDview, mAccountStatus;
    ImageView mProfilePic, mGamerscoreLogo;

    TextView mAcctStatus, mXUIDtitle;

    AsyncTask<Void,Void,Void> mTask;
    public static String mXUID;
    String mAccountTier;
    String mGamerscore;
    String mURLforPreferedColor;
    public static String mGamertag;
    String mURLforProfilePic;
    public static String mColorUrl;
    String mFavColor;
    RelativeLayout mLayout;

    ProgressBar mProgressBar;
    Toolbar mToolbar;
    ViewPager mViewPager;
    TabLayout mTablayout;
    ProfileViewPagerAdapter mVPadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mLayout = (RelativeLayout)findViewById(R.id.activity_profile);

        mToolbar = (Toolbar)findViewById(R.id.profile_toolbar);

        mViewPager = (ViewPager)findViewById(R.id.profile_viewpager);
        mVPadapter = new ProfileViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mVPadapter);

        mTablayout = (TabLayout)findViewById(R.id.profile_tablayout);
        mTablayout.setupWithViewPager(mViewPager);

        mGSview = (TextView)findViewById(R.id.profile_gamerscore);
        mGTview = (TextView)findViewById(R.id.profile_gamertag);
        mAccountStatus = (TextView)findViewById(R.id.profile_account_status);
        mXUIDview = (TextView)findViewById(R.id.profile_xuid_num);
        mProfilePic = (ImageView)findViewById(R.id.profile_pic);
        mProgressBar = (ProgressBar)findViewById(R.id.profile_progressbar);

        mAcctStatus = (TextView)findViewById(R.id.profile_account);
        mGamerscoreLogo = (ImageView)findViewById(R.id.profile_gs_logo);
        mXUIDtitle = (TextView)findViewById(R.id.profile_xuid);

        mGamerscoreLogo.setImageResource(R.drawable.gamerscorelogo);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        Intent intent = getIntent();
        mXUID = intent.getStringExtra("xuid");

        if (mGamertag == null || mGamerscore == null || mAccountTier == null || mXUID == null || mURLforProfilePic == null || mURLforPreferedColor == null) {
            fillProfile();

            SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("gt", mGamertag);
            editor.putString("gs", mGamerscore);
            editor.putString("status", mAccountTier);
            editor.putString("xuid", mXUID);
            editor.putString("img", mURLforProfilePic);
            editor.apply();
        }else {
            setViews();
        }
    }

    public void fillProfile(){
        Log.d(TAG, "fillProfile: XUID -- " + mXUID);
        mTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request profileRequest = new Request.Builder()
                        .headers(MainActivity.mHeaders)
                        .url("https://xboxapi.com/v2/" + mXUID + "/profile")
                        .build();

                try {
                    Response profileResponse = client.newCall(profileRequest).execute();
                    JSONObject profileJsonObject = new JSONObject(profileResponse.body().string());

                    mAccountTier = profileJsonObject.getString("AccountTier");
                    int gs = profileJsonObject.getInt("Gamerscore");
                    mGamerscore = Integer.toString(gs);
                    mURLforPreferedColor = profileJsonObject.getString("PreferredColor");
                    mURLforProfilePic = profileJsonObject.getString("GameDisplayPicRaw");
                    mGamertag = profileJsonObject.getString("Gamertag");
                    mColorUrl = profileJsonObject.getString("PreferredColor");

                    Log.d(TAG, "doInBackground: PROFILE API -- AccountTier -- " + mAccountTier);
                    Log.d(TAG, "doInBackground: PROFILE API -- GamerScore -- " + mGamerscore);
                    Log.d(TAG, "doInBackground: PROFILE API -- URL for Fav Color -- " + mURLforPreferedColor);
                    Log.d(TAG, "doInBackground: PROFILE API -- Gamertag -- " + mGamertag);
                    Log.d(TAG, "doInBackground: PROFILE API -- URL for Pic -- " + mURLforProfilePic);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                Request colorRequest = new Request.Builder()
                        .headers(MainActivity.mHeaders)
                        .url(mColorUrl)
                        .build();

                try {
                    Response colorResponse = client.newCall(colorRequest).execute();
                    JSONObject colorJSONobject = new JSONObject(colorResponse.body().string());

                    mFavColor = colorJSONobject.getString("primaryColor");
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                setViews();



            }
        }.execute();
    }

    public void setViews(){
        mGTview.setText(mGamertag);
        mGSview.setText(mGamerscore);
        mAccountStatus.setText(mAccountTier);
        mXUIDview.setText(mXUID);
        Picasso.with(getApplicationContext()).load(mURLforProfilePic).into(mProfilePic);

        mGTview.setVisibility(View.VISIBLE);
        mGSview.setVisibility(View.VISIBLE);
        mAccountStatus.setVisibility(View.VISIBLE);
        mXUIDview.setVisibility(View.VISIBLE);
        mProfilePic.setVisibility(View.VISIBLE);

        mAcctStatus.setVisibility(View.VISIBLE);
        mXUIDtitle.setVisibility(View.VISIBLE);
        mGamerscoreLogo.setVisibility(View.VISIBLE);

        mToolbar.setVisibility(View.VISIBLE);

        mProgressBar.setVisibility(View.GONE);


        mToolbar.setBackgroundColor(Color.parseColor("#" + mFavColor));
        mLayout.setBackgroundColor(Color.parseColor("#" + mFavColor));
    }
}
