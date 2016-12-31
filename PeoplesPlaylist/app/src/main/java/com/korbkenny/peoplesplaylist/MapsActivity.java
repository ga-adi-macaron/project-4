package com.korbkenny.peoplesplaylist;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.korbkenny.peoplesplaylist.coloring.ColoringActivity;
import com.korbkenny.peoplesplaylist.objects.Playlist;
import com.korbkenny.peoplesplaylist.objects.Song;
import com.korbkenny.peoplesplaylist.objects.User;
import com.korbkenny.peoplesplaylist.playlist.PlaylistActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{
    private static final String TAG = "MapsActivity: ";
    private static final int REQUEST_CODE_LOCATION = 505;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location lastLocation;
    private LocationRequest mLocationRequest;
    private FloatingActionButton fab;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabasePlaylistReference, mGeofireRef, mUserRef;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ImageView vUserIcon;
    private User ME;
    private UserSingleton sUserSingleton;
    private int tries = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        sUserSingleton = UserSingleton.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    mUserRef = mFirebaseDatabase.getReference("Users").child(user.getUid());
                    mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            if(dataSnapshot!=null){
                                new AsyncTask<Void,Void,Void>(){
                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        ME = dataSnapshot.getValue(User.class);
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        sUserSingleton.setUser(ME);
                                    }
                                }.execute();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Intent intent = new Intent(MapsActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        //Create Google Api Client
        buildGoogleApiClient();
        createLocationRequest();

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
        vUserIcon = (ImageView) findViewById(R.id.maps_user_icon);



        //  FAB to create new playlist. Opens a dialog to do this.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastLocation!=null && ME != null) {
                    onCreateDialog(lastLocation).show();
                } else {
                    Toast.makeText(MapsActivity.this, "Can't seem to make a new playlist...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(MapsActivity.this, ColoringActivity.class);
                startActivity(intent);
                return false;
            }
        });

        //  When a marker is clicked, get the playlist Id contained in its tag,
        //  and start the playlist activity with the Id. But only if it's within
        //  a certain distance from your location.
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                getLastLocation();
                double distance = getDistanceBetweenPoints(marker,lastLocation);
                Log.d(TAG, "onMarkerClick: " + distance);

                if (distance<100) {
                    Intent intent = new Intent(MapsActivity.this, PlaylistActivity.class);
                    intent.putExtra("Playlist Id", marker.getTag().toString());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MapsActivity.this, "Get a bit closer, wouldja?", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void requestUserImage() {
        if(ME != null && ME.getUserImage() != null){
            loadUserIcon();
        } else if(tries < 30){
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    tries++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            requestUserImage();
                        }
                    });
                }
            };
            timer.schedule(timerTask,0,1000);
        }
    }

    private void loadUserIcon() {

        Picasso.with(this).load(ME.getUserImage()).into(vUserIcon);
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //     Create Playlist Dialog
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Dialog onCreateDialog(final Location location) {
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

                        //  Create playlist object and set its title, description, lat & lon, etc.
                        final Playlist playlist = new Playlist();
                        playlist.setTitle(title.getText().toString());
                        playlist.setDescription(description.getText().toString());
                        playlist.setLat(location.getLatitude());
                        playlist.setLon(location.getLongitude());
                        playlist.setUserId(ME.getId());
                        playlist.setCover("https://firebasestorage.googleapis.com/v0/b/peoplesplaylist-9c5d9.appspot.com/o/userplaceholder.png?alt=media&token=69ff913e-2eb2-46b5-a210-390e69ddac31");

                        //  Push to the playlists branch of the database, and get the
                        //  unique, randomly generated key for geofire use.
                        final String playlistId = mDatabasePlaylistReference.push().getKey();
                        playlist.setId(playlistId);
                        mDatabasePlaylistReference.child(playlistId).setValue(playlist);
                        geoFire.setLocation(playlistId, new GeoLocation(playlist.getLat(), playlist.getLon()));

                        //  Put the Playlist Id into the intent so it can be accessed from
                        //  the database in the actual playlist activity.
                        Intent intent = new Intent(MapsActivity.this,PlaylistActivity.class);
                        intent.putExtra("Playlist Id", playlistId);
                        startActivity(intent);
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
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void createGeoQuery(){
        geoQuery = geoFire.queryAtLocation(new GeoLocation(lastLocation.getLatitude(),lastLocation.getLongitude()),100);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                DatabaseReference ref = mFirebaseDatabase.getReference("Playlists/"+key);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Playlist playlist = dataSnapshot.getValue(Playlist.class);
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(playlist.getLat(),playlist.getLon()))
                                        .title(playlist.getTitle()));
                        marker.setTag(playlist.getId());
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
    }





    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //     Extra methods I probably won't touch much.
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //  Get distance between you and the marker you click
    private double getDistanceBetweenPoints(Marker marker, Location lastLocation) {
        double lat1 = marker.getPosition().latitude;
        double lon1 = marker.getPosition().longitude;
        double lat2 = lastLocation.getLatitude();
        double lon2 = lastLocation.getLongitude();

        int R = 6371; // km
        double x = (lon2 - lon1) * Math.cos((lat1 + lat2) / 2);
        double y = (lat2 - lat1);

        return Math.sqrt(x * x + y * y) * R;
    }



    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //     Activity Lifecycle Overrides
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestUserImage();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && (!mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected())) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
            mGoogleApiClient.disconnect();
        }
        if (geoQuery != null) {
            geoQuery.removeAllListeners();
        }
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    //  Set up the map by getting permission.
    public void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (!checkPermission()) {
            requestPermission();
            return;
        }
    }

    //REQUEST STUFF

    private void requestPermission() {
        ActivityCompat.requestPermissions(MapsActivity.this, new
                String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION:
                if (grantResults.length> 0) {
                    boolean FinePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean CoarsePermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (FinePermission && CoarsePermission) {
                        Toast.makeText(MapsActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MapsActivity.this,"Permission Denied",
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                ACCESS_COARSE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    //END REQUEST STUFF


    private void moveMapToCurrentLocation(Location lastLocation) {
        if (lastLocation != null) {
            LatLng current = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(17f));
        }
    }

    public void getLastLocation(){
        if(checkPermission()) {
            if (LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)!=null) {
                lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }
            else{
                requestPermission();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (checkPermission()) {
            mMap.setMyLocationEnabled(true);
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastLocation == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }
        else{
            requestPermission();
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
        createGeoQuery();
        moveMapToCurrentLocation(lastLocation);
    }

    //=====================================
    //
    //          OnCreate Methods
    //
    //=====================================

    private void buildApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }




}
