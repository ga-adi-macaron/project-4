package com.joelimyx.politicallocal.main;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.vision.CameraSource;
import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.bills.BillFragment;
import com.joelimyx.politicallocal.database.DBAssetHelper;
import com.joelimyx.politicallocal.database.RepsSQLHelper;
import com.joelimyx.politicallocal.news.NewsFragment;
import com.joelimyx.politicallocal.reps.RepsFragment;
import com.joelimyx.politicallocal.reps.gson.bingsearch.Portrait;
import com.joelimyx.politicallocal.reps.gson.congress.RepsList;
import com.joelimyx.politicallocal.reps.gson.congress.Result;
import com.joelimyx.politicallocal.reps.service.BingImageService;
import com.joelimyx.politicallocal.reps.service.CongressService;
import com.squareup.picasso.Picasso;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "V2RCaymY8YS5r2hQLtKPf1A5s";
    private static final String TWITTER_SECRET = "FX3mhvuWzZQJkBxF6Idm8SwrJdwPamBh8yL3UgYTyfYP9pwKCd";


    private static final String TAG = "MainActivity";
    public static final int LOCATION_REQUEST_CODE = 1;

    private BottomNavigationView mBottomBar;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    String mState;

    public static final String district_base_URL = "https://congress.api.sunlightfoundation.com/";
    public static final String open_Url = "https://www.opensecrets.org/";


    @Override
    protected void onStart() {
        super.onStart();
        // TODO: 12/17/16 Call this only once to get location
        mGoogleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                DBAssetHelper dbSetup = new DBAssetHelper(MainActivity.this);
                dbSetup.getReadableDatabase();
                return null;
            }
        }.execute();

        SharedPreferences preferences = getSharedPreferences(getString(R.string.district_file), Context.MODE_PRIVATE);
        mState = preferences.getString(getString(R.string.state),null);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mBottomBar = (BottomNavigationView) findViewById(R.id.bottom_bar);
        mBottomBar.setOnNavigationItemSelectedListener(this);

        //todo: Default show as news fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, BillFragment.newInstance())
                .commit();
        mBottomBar.getMenu().getItem(2).setChecked(true);
    }

    /*---------------------------------------------------------------------------------
    // Bottom Nav bar AREA
    ---------------------------------------------------------------------------------*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reps:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, RepsFragment.newInstance(mState))
                        .commit();
                break;

            case R.id.news:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, NewsFragment.newInstance())
                        .commit();
                break;

            case R.id.bills:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, BillFragment.newInstance())
                        .commit();
                break;
        }
        return true;
    }

    /*---------------------------------------------------------------------------------
    // PERMISION REQUEST AREA
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

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    /*---------------------------------------------------------------------------------
    // Helper Method AREA
    ---------------------------------------------------------------------------------*/
    /**
     * Helper method for getting current location
     */
    private void getCurrentLocation(){
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        double latitude = mCurrentLocation.getLatitude();
        double longitude = mCurrentLocation.getLongitude();

        SharedPreferences preference = getSharedPreferences(getString(R.string.district_file),MODE_PRIVATE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(district_base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<RepsList> call = retrofit.create(CongressService.class).getLegislatures(latitude,longitude);
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
                        loadImageFromWeb(name);
                    }
                }

                @Override
                public void onFailure(Call<RepsList> call, Throwable t) {

                }
            });
        }

    }

    // TODO: 12/20/16 Call only one time to get reps image
    private void loadImageFromWeb(final String person){

        //Search for the image
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.cognitive.microsoft.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<Portrait> call = retrofit.create(BingImageService.class).getImage(person+" congress",2);
        call.enqueue(new Callback<Portrait>() {
            @Override
            public void onResponse(Call<Portrait> call, Response<Portrait> response) {

                //Download the image to bitmap
                ImageRequest request = new ImageRequest(
                        response.body().getValue().get(0).getThumbnailUrl(),
                        response1 -> saveImageToFile(response1,person),
                        0,
                        0,
                        null,
                        null,
                        e-> Toast.makeText(MainActivity.this, "Failed image download", Toast.LENGTH_SHORT).show());
                Volley.newRequestQueue(MainActivity.this).add(request);            }

            @Override
            public void onFailure(Call<Portrait> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed image search", Toast.LENGTH_SHORT).show();
            }
        });
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
}
