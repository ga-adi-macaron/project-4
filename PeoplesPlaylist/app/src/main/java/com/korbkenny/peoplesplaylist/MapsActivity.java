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

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{
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
    private DatabaseReference mDatabasePlaylistReference, mGeofireRef;
    private LatLngBounds currentScreen;
    private GeoFire geoFire;
    private GeoQuery geoQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Create Google Api Client
        buildGoogleApiClient();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabasePlaylistReference = mFirebaseDatabase.getReference("Playlists");
        mGeofireRef = FirebaseDatabase.getInstance().getReference("geofire");
        geoFire = new GeoFire(mGeofireRef);

        //Create Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //     Do all view-related stuff here, map and fab.
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setUpMap();
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapsActivity.this, "Cool.", Toast.LENGTH_SHORT).show();
                Playlist playlist = new Playlist();
                playlist.setTitle("sweetyNicey");
                playlist.setDescription("coolio!");
                playlist.setLat(43.1234);
                playlist.setLon(-73.3456);
                String id = mDatabasePlaylistReference.push().getKey();
                geoFire.setLocation(id, new GeoLocation(playlist.getLat(), playlist.getLon()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error!=null) {
                            Toast.makeText(MapsActivity.this, "Nooooooo there was an error...", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MapsActivity.this, "RAD!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Log.d(TAG, "onClick: " + id);
                mDatabasePlaylistReference.child(id).setValue(playlist);

//                if (lastLocation!=null) {
//                    onCreateDialog().show();
//                    Log.d(TAG, "onClick: " + lastLocation.getLatitude() + " " + lastLocation.getLongitude());
//                } else {
//                    Log.d(TAG, "onClick: WELL COOL, NO CONNECTION?!");
//                }
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                geoQuery = geoFire.queryAtLocation(new GeoLocation(43.3,-73.3),1000);
                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                    @Override
                    public void onKeyEntered(String key, GeoLocation location) {
                        DatabaseReference ref = mFirebaseDatabase.getReference("Playlists/"+key);
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d(TAG, "onDataChange: "+ dataSnapshot.toString());
                                Playlist playlist = dataSnapshot.getValue(Playlist.class);
                                Log.d(TAG, "onDataChange: " + playlist.getTitle() + " " + playlist.getDescription());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onKeyExited(String key) {

                    }

                    @Override
                    public void onKeyMoved(String key, GeoLocation location) {

                    }

                    @Override
                    public void onGeoQueryReady() {

                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {

                    }
                });
                return true;
            }
        });

        currentScreen = mMap.getProjection().getVisibleRegion().latLngBounds;


    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //     Create Playlist Dialog
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
                        //mPlaylist = new Playlist(lastLocation.getLatitude(),lastLocation.getLongitude(),mPlaylistTitle,mPlaylistDescription);
                        mDatabasePlaylistReference.push().setValue(mPlaylist);
                        mDatabasePlaylistReference.getKey();
                        Toast.makeText(MapsActivity.this, "Even cooler", Toast.LENGTH_SHORT).show();
                        geoFire.setLocation("This Place Yeah!", new GeoLocation(lastLocation.getLatitude(), lastLocation.getLongitude()), new GeoFire.CompletionListener() {
                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                if (error != null){
                                    Log.d(TAG, "onComplete: BUT NOT COMLETE! OH SHOOT! IT FAILED TO UPLOAD!");
                                }
                                else{
                                    Log.d(TAG, "onComplete: AND WOOOO IT WORKED (SORTA PROBABLY)");
                                }
                            }
                        });

//                        Intent intent = new Intent(MapsActivity.this,PlaylistActivity.class);
//                        startActivity(intent);
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


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //     For the GeoQuery, add markers when a key enters,
    //     and remove markers when they exit (or don't, not sure yet!)
    //     There are also extra methods at the bottom, but probably won't need them.
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//    @Override
//    public void onKeyEntered(String key, GeoLocation location) {
//        Log.d(TAG, "onKeyEntered: " + key + " " + location.latitude + " " + location.longitude);
//    }
//
//    @Override
//    public void onKeyExited(String key) {
//
//    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //     Extra methods I probably won't touch much.
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
        geoQuery.removeAllListeners();
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
            mMap.moveCamera(CameraUpdateFactory.zoomTo(17f));
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
                    createLocationRequest();
                    startLocationUpdates();
                    moveMapToCurrentLocation(lastLocation);
                } else{
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
                }

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




//    @Override
//    public void onKeyMoved(String key, GeoLocation location) {
//
//    }
//
//    @Override
//    public void onGeoQueryReady() {
//
//    }
//
//    @Override
//    public void onGeoQueryError(DatabaseError error) {
//
//    }
}
