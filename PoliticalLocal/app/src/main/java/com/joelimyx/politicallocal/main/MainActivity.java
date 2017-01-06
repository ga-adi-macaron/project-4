package com.joelimyx.politicallocal.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.joelimyx.politicallocal.search.ResultFragment;
import com.joelimyx.politicallocal.welcome.LoginActivity;
import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.bills.BillFragment;
import com.joelimyx.politicallocal.database.DBAssetHelper;
import com.joelimyx.politicallocal.database.RepsSQLHelper;
import com.joelimyx.politicallocal.news.NewsFragment;
import com.joelimyx.politicallocal.reps.RepsFragment;
import com.joelimyx.politicallocal.reps.gson.congress.RepsList;
import com.joelimyx.politicallocal.reps.gson.congress.Result;
import com.joelimyx.politicallocal.reps.service.SunlightService;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

import java.io.FileOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        AppBarLayout.OnOffsetChangedListener{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "V2RCaymY8YS5r2hQLtKPf1A5s";
    private static final String TWITTER_SECRET = "FX3mhvuWzZQJkBxF6Idm8SwrJdwPamBh8yL3UgYTyfYP9pwKCd";

    //Request Code and tag
    private static final String TAG = "MainActivity";
    public static final int LOCATION_REQUEST_CODE = 1;
    public static final int SIGNIN_REQUEST_CODE = 2;

    //Main Menu View
    private FloatingSearchView mSearchView;
    private BottomNavigationView mBottomNavigationView;

    //Google and Firebase
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser mUser;

    //Local variables
    private FloatingActionButton mButton;
    private boolean mIsFirst, mIsBillFragment = false;
    private OnBottomMenuItemSelectedListener mListener;
    private OnBillFabClickedListener mBillFabClickedListener;

    public static final String sunlight_base_URL = "https://congress.api.sunlightfoundation.com/";

    public interface OnBillFabClickedListener{
        void OnBillFabClicked();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO: 12/17/16 Call this only once to get location
        if (!mIsFirst) {
            mGoogleApiClient.connect();
        }
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Twitter Instantiation
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_main);

        mIsFirst = getSharedPreferences(getString(R.string.district_file), Context.MODE_PRIVATE).getBoolean(getString(R.string.is_first),true);

        /*---------------------------------------------------------------------------------
        // Sign In AREA
        ---------------------------------------------------------------------------------*/
        if (mIsFirst) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, SIGNIN_REQUEST_CODE);
        }
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = firebaseAuth -> {
            mUser = firebaseAuth.getCurrentUser();
            if (mUser!=null){
                Log.d(TAG, "onCreate: "+mUser.getDisplayName());
            }
        };

        //DBAsset to grab data from database
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                DBAssetHelper dbSetup = new DBAssetHelper(MainActivity.this);
                dbSetup.getReadableDatabase();
                return null;
            }
        }.execute();

        //Instantiate google api
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        /*---------------------------------------------------------------------------------
        // SearchView
        ---------------------------------------------------------------------------------*/
        AppBarLayout appBar = (AppBarLayout) findViewById(R.id.bill_appbar_layout);
        appBar.addOnOffsetChangedListener(this);
        mSearchView = (FloatingSearchView) findViewById(R.id.search_view);
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                mBottomNavigationView.setVisibility(View.GONE);
                mSearchView.setLeftActionMode(FloatingSearchView.LEFT_ACTION_MODE_SHOW_HOME);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container,ResultFragment.newInstance(currentQuery))
                        .addToBackStack("Search")
                        .commit();
            }
        });

        mSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {

        });

        mSearchView.setOnHomeActionClickListener( () -> {
            getSupportFragmentManager().popBackStack();
            mSearchView.clearQuery();
            mSearchView.setLeftActionMode(FloatingSearchView.LEFT_ACTION_MODE_SHOW_SEARCH);
            mBottomNavigationView.setVisibility(View.VISIBLE);
        });

        /*---------------------------------------------------------------------------------
        // Bottom Nav Bar
        ---------------------------------------------------------------------------------*/
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_bar);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        //todo: Default show as news fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, BillFragment.newInstance(),getString(R.string.bill_fragment))
                .commit();
        mBottomNavigationView.getMenu().getItem(2).setChecked(true);

        mButton = (FloatingActionButton) findViewById(R.id.bill_filter_fab);
        mButton.setOnClickListener(v -> mBillFabClickedListener.OnBillFabClicked());
    }

    /*---------------------------------------------------------------------------------
    // Sign In Result AREA
    ---------------------------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==SIGNIN_REQUEST_CODE){
            if (resultCode==RESULT_OK){
                if (data!=null && data.getBooleanExtra("back",true)){
                    finish();
                }else {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    /*---------------------------------------------------------------------------------
    // Bottom Nav bar AREA
    ---------------------------------------------------------------------------------*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager transaction = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.reps:
                mIsBillFragment = false;
                String state = getSharedPreferences(getString(R.string.district_file), Context.MODE_PRIVATE).getString(getString(R.string.state),null);
                transaction
                    .beginTransaction()
                    .replace(R.id.main_container, RepsFragment.newInstance(state))
                    .commit();
                mBottomNavigationView.setBackgroundColor(R.color.colorPrimary);
                break;

            case R.id.news:
                mIsBillFragment = false;
                transaction
                    .beginTransaction()
                    .replace(R.id.main_container, NewsFragment.newInstance())
                    .commit();
                break;

            case R.id.bills:
                if (!(transaction.findFragmentByTag(getString(R.string.bill_fragment)) instanceof BillFragment)) {
                    mIsBillFragment = true;
                    BillFragment temp = BillFragment.newInstance();
                    transaction
                            .beginTransaction()
                            .replace(R.id.main_container, temp, getString(R.string.bill_fragment))
                            .commit();

                    mListener = temp.getListener();
                    mBillFabClickedListener = temp.getFabListener();

                    mButton.setVisibility(View.VISIBLE);
                    mBottomNavigationView.setBackgroundColor(Color.parseColor("#FB8C00"));
                }else {
                    mListener.OnBottomMenuItemSelected(getString(R.string.bill_fragment));
                }
                break;
        }
        return true;
    }

    /*---------------------------------------------------------------------------------
    // PERMISSION REQUEST AREA
    ---------------------------------------------------------------------------------*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==LOCATION_REQUEST_CODE){
            if (grantResults.length>0) {
                getCurrentLocation();
            }
        }
    }

    /*---------------------------------------------------------------------------------
    // Location API Connection AREA
    ---------------------------------------------------------------------------------*/
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
        }else{
            getCurrentLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: "+connectionResult.getErrorMessage());
        Toast.makeText(this, "No Network Available", Toast.LENGTH_SHORT).show();
    }

    /*---------------------------------------------------------------------------------
    // Main Activity Override
    ---------------------------------------------------------------------------------*/
    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mSearchView.clearQuery();
        mSearchView.setLeftActionMode(FloatingSearchView.LEFT_ACTION_MODE_SHOW_SEARCH);
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    //For hiding and showing the search view relative to the scroll
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        mSearchView.setTranslationY(verticalOffset);
        mBottomNavigationView.setTranslationY(verticalOffset*-1);
        if (verticalOffset == 0 && mIsBillFragment) {
            mButton.setVisibility(View.VISIBLE);
        }else
            mButton.setVisibility(View.GONE);
    }

    /*---------------------------------------------------------------------------------
    // Helper Method AREA
    ---------------------------------------------------------------------------------*/
    /**
     * Helper method for getting current location
     */
    private void getCurrentLocation(){
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();

        SharedPreferences preference = getSharedPreferences(getString(R.string.district_file),MODE_PRIVATE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(sunlight_base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<RepsList> call = retrofit.create(SunlightService.class).getLegislatures(latitude,longitude);
        if (preference.getBoolean(getString(R.string.is_first),true)) {
            call.enqueue(new Callback<RepsList>() {
                @Override
                public void onResponse(Call<RepsList> call, Response<RepsList> response) {
                    List<Result> result = response.body().getResults();
                    RepsSQLHelper db = RepsSQLHelper.getInstance(MainActivity.this);

                    SharedPreferences.Editor editor = preference.edit();
                    editor.putString(getString(R.string.state), result.get(0).getState());
                    editor.putBoolean(getString(R.string.is_first), false);
                    editor.commit();
                    for (Result current : result) {
                        String name = current.getFirstName() + " " + current.getLastName();
                        db.addRep(current, name);
                        loadImageFromWeb(name,current.getBioguideId());
                    }
                }

                @Override
                public void onFailure(Call<RepsList> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Failed to get your representatives", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    // TODO: 12/20/16 Call only one time to get reps image
    private void loadImageFromWeb(final String person, String bioId){

        //Download the image to bitmap
        ImageRequest request = new ImageRequest("https://theunitedstates.io/images/congress/original/"+bioId+".jpg",
                response1 -> saveImageToFile(response1,person),
                0,
                0,
                null,
                null,
                e-> Toast.makeText(MainActivity.this, "Failed image download", Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(MainActivity.this).add(request);

    }

    private void saveImageToFile(Bitmap image, String person){
        try {
            FileOutputStream fos = openFileOutput(person+".jpg",MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*---------------------------------------------------------------------------------
    // Interface AREA
    ---------------------------------------------------------------------------------*/
    public interface OnBottomMenuItemSelectedListener{
        /**
         * To smooth scroll to top of the list when the same bottom menu is selected
         * @param tag fragment tags
         */
        void OnBottomMenuItemSelected(String tag);
    }
}
