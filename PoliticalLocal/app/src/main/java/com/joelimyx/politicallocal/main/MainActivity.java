package com.joelimyx.politicallocal.main;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.main.gson.District;
import com.joelimyx.politicallocal.main.gson.Result;
import com.joelimyx.politicallocal.news.NewsFragment;
import com.joelimyx.politicallocal.reps.RepsFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";
    public static final int LOCATION_REQUEST_CODE = 1;

    private BottomNavigationView mBottomBar;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    public static final String district_base_URL = "https://congress.api.sunlightfoundation.com/";

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                .replace(R.id.main_container, RepsFragment.newInstance())
                .commit();
        mBottomBar.getMenu().getItem(0).setChecked(true);
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
                        .replace(R.id.main_container, RepsFragment.newInstance())
                        .commit();
                break;

            case R.id.news:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, NewsFragment.newInstance())
                        .commit();
                break;

            case R.id.bills:
                break;
        }
        return true;
    }

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

    /**
     * Helper method for getting current location
     */
    private void getCurrentLocation(){
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        String latitude = String.valueOf(mCurrentLocation.getLatitude());
        String longitude = String.valueOf(mCurrentLocation.getLongitude());

        Log.d(TAG, "getCurrentLocation location: "+latitude+","+longitude);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(district_base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<District> call = retrofit.create(DistrictService.class).getDistrict(latitude, longitude);
        call.enqueue(new Callback<District>() {
            @Override
            public void onResponse(Call<District> call, Response<District> response) {
                if (response.isSuccessful()) {
                    Result temp = response.body().getResults().get(0);
                    SharedPreferences preferences = getSharedPreferences(getString(R.string.district),MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("state",temp.getState());
                    editor.commit();
                    Log.d("Main", "onResponse District:" + temp.getState() + temp.getDistrict());
                }
            }

            @Override
            public void onFailure(Call<District> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to get response", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: ");
            }
        });

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
}
