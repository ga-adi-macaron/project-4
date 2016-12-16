package com.korbkenny.peoplesplaylist;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "MapsActivity: ";
    private static final int REQUEST_CODE_LOCATION = 505;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location lastLocation;
    private LocationRequest mLocationRequest;
    private FloatingActionButton fab;
    private String mPlaylistTitle, mPlaylistDescription;
    private Playlist mPlaylist;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabasePlaylistReference;
    private LatLngBounds currentScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Create Google Api Client
        buildGoogleApiClient();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabasePlaylistReference = mFirebaseDatabase.getReference("Playlists");

        //Create Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    //When the map is ready, this is where you call stuff.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setUpMap();
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapsActivity.this, "Cool.", Toast.LENGTH_SHORT).show();
                if (lastLocation!=null) {
                    onCreateDialog().show();
                    Log.d(TAG, "onClick: " + lastLocation.getLatitude() + " " + lastLocation.getLongitude());
                } else {
                    Log.d(TAG, "onClick: WELL COOL, NO CONNECTION?!");
                }
            }
        });

        currentScreen = mMap.getProjection().getVisibleRegion().latLngBounds;
        

    }

    public Dialog onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.add_playlist_dialog, null))
                // Add action buttons
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog view = (Dialog)dialog;
                        EditText title = (EditText) view.findViewById(R.id.add_playlist_title);
                        EditText description = (EditText) view.findViewById(R.id.add_playlist_description);
                        mPlaylistTitle = title.getText().toString();
                        mPlaylistDescription = description.getText().toString();
                        mPlaylist = new Playlist(lastLocation.getLatitude(),lastLocation.getLongitude(),mPlaylistTitle,mPlaylistDescription);
                        mDatabasePlaylistReference.push().setValue(mPlaylist);
                        Toast.makeText(MapsActivity.this, "Even cooler", Toast.LENGTH_SHORT).show();

                        mMap.addMarker(new MarkerOptions().position(new LatLng(mPlaylist.getLat(), mPlaylist.getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.covermatters2)).title(mPlaylist.getTitle()));
                    }
                })
                .setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    public void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
            return;
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ||ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private void moveMapToCurrentLocation(Location lastLocation) {
        if (lastLocation != null) {
            LatLng current = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(18f));
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ||ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ||ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)!=null) {
                    lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    Log.d(TAG, "onConnected: " + lastLocation.getLatitude() + "    " + lastLocation.getLongitude());
                } else{
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
                }
                createLocationRequest();
                startLocationUpdates();
                moveMapToCurrentLocation(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
    }
}
