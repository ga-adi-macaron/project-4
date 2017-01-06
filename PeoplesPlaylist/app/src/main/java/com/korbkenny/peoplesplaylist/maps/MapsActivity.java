package com.korbkenny.peoplesplaylist.maps;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.korbkenny.peoplesplaylist.LoginActivity;
import com.korbkenny.peoplesplaylist.MyUserInfoActivity;
import com.korbkenny.peoplesplaylist.R;
import com.korbkenny.peoplesplaylist.UserSingleton;
import com.korbkenny.peoplesplaylist.coloring.ColoringActivity;
import com.korbkenny.peoplesplaylist.objects.Playlist;
import com.korbkenny.peoplesplaylist.objects.User;
import com.korbkenny.peoplesplaylist.playlist.PlaylistActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{
    private static final String TAG = "MapsActivity: ";
    private static final int REQUEST_CODE_LOCATION = 505;
    public static final String SHARED_PREF = "MySettings";
    public static final String LOGGEDIN = "LoggedIn";

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
    private int geoTries = 0;
    private boolean loggedIn;
    private Button mSignInButton;
    private CardView cardView;
    private LocationManager mLocationManager;
    private List<Target> mTargetList;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        settings = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        loggedIn = settings.getBoolean(LOGGEDIN,false);
        buildGoogleApiClient();

        mTargetList = new ArrayList<>();

        sUserSingleton = UserSingleton.getInstance();
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
        stylizeMap();
        setUpMap();
        createAuthListener();
        setViewsIfLoggedIn();
        keepRequestingGeoQueries();

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

        //  When a marker is clicked, get the playlist Id contained in its tag,
        //  and start the playlist activity with the Id. But only if it's within
        //  a certain distance from your location.
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                getLastLocation();
                if(lastLocation!=null) {
                    double distance = getDistanceBetweenPoints(marker, lastLocation);
                    Log.d(TAG, "onMarkerClick: " + distance);

                    // I think it's in meters?
                    if (distance < 50) {
                        Intent intent = new Intent(MapsActivity.this, PlaylistActivity.class);
                        intent.putExtra("Playlist Id", marker.getTag().toString());
                        startActivity(intent);
                    } else {
                        Toast.makeText(MapsActivity.this, "Get a bit closer, wouldja?", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        vUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, MyUserInfoActivity.class);
                startActivity(intent);
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createAuthListener() {
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
                                        if (ME != null && ME.getUserImage() != null) {
                                            loadUserIcon();
                                        }
                                    }
                                }.execute();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

    }

    private void stylizeMap() {
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //     Create Playlist Dialog
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Dialog onCreateDialog(final Location location) {

        //  Create and Inflate
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
                        playlist.setUserIcon(ME.getUserImage());
                        playlist.setCover("null");

                        //  Push to the playlists branch of the database, and get the
                        //  unique, randomly generated key for use with geofire/other stuff.
                        final String playlistId = mDatabasePlaylistReference.push().getKey();
                        settings.edit().putString("SavedPlaylistId",playlistId).commit();
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
        moveMapToCurrentLocation(lastLocation);
        Log.d(TAG, "createGeoQuery: " + lastLocation.getLatitude() + " " + lastLocation.getLongitude());
        geoQuery = geoFire.queryAtLocation(new GeoLocation(lastLocation.getLatitude(),lastLocation.getLongitude()),100);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                DatabaseReference ref = mFirebaseDatabase.getReference("Playlists/"+key);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        new AsyncTask<Void,Void,Playlist>(){
                            @Override
                            protected Playlist doInBackground(Void... voids) {
                                Playlist playlist = dataSnapshot.getValue(Playlist.class);
                                return playlist;
                            }

                            @Override
                            protected void onPostExecute(final Playlist playlist) {
                                if(playlist.getCover()!=null){
                                    MarkerOptions options = new MarkerOptions()
                                            .position(new LatLng(playlist.getLat(),playlist.getLon()))
                                            .title(playlist.getTitle())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.dotmarker));
                                    Marker marker = mMap.addMarker(options);
                                    marker.setTag(playlist.getId());
                                    Target target = new PicassoMarker(marker);
                                    mTargetList.add(target);
                                    if(!playlist.getCover().equals("null")) {
                                        Picasso.with(MapsActivity.this).load(playlist.getCover()).placeholder(R.drawable.markerplaceholder).resize(150, 150).into(target);
                                    }
                                    else{
                                        Picasso.with(MapsActivity.this).load(R.drawable.coverplaceholder).resize(100,100).into(target);
                                    }
                                }
                            }
                        }.execute();
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
    //      Request User Icon and GeoQuery
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void keepRequestingGeoQueries() {
        if(lastLocation!=null && geoQuery == null) {
            moveMapToCurrentLocation(lastLocation);
            createGeoQuery();
        }
        else if (lastLocation == null && geoQuery == null && geoTries < 30){
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            keepRequestingGeoQueries();
                            geoTries++;
                        }
                    });
                }
            };
            timer.schedule(timerTask, 0, 1000);
        }
    }

    private void loadUserIcon() {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                vUserIcon.setImageBitmap(bitmap);
                ValueAnimator ani = ValueAnimator.ofFloat(0.3f, 1);
                ani.setDuration(700);
                ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        vUserIcon.setAlpha((float) animation.getAnimatedValue());
                        cardView.setAlpha((float) animation.getAnimatedValue());
                    }
                });
                ani.start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        vUserIcon.setTag(target);

        Picasso.with(this).load(ME.getUserImage()).into(target);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //     Build the Google Api Client and add Listeners
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //         Setup Views and Map and Stuff
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void setViewsIfLoggedIn(){
        fab = (FloatingActionButton)findViewById(R.id.fab);
        vUserIcon = (ImageView) findViewById(R.id.maps_user_icon);
        mSignInButton = (Button) findViewById(R.id.sign_in_maps);
        cardView = (CardView) findViewById(R.id.maps_user_icon_cardview);
        cardView.setAlpha(0.3f);

        if(!loggedIn){
            vUserIcon.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            mSignInButton.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
        }

        if(loggedIn){
            vUserIcon.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
            mSignInButton.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
        }
    }

    public void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setBuildingsEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setMaxZoomPreference(20);
        mMap.setMinZoomPreference(15);
        if (!checkPermission()) {
            requestPermission();
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //     Request Permissions/Check Permissions
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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

                    if (FinePermission || CoarsePermission) {
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

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //          Location Stuff
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void moveMapToCurrentLocation(Location lastLocation) {
        if (lastLocation != null) {
            LatLng current = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(current).zoom(18).tilt(90).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void getLastLocation(){
        if(checkPermission()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        else {
            requestPermission();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (checkPermission()) {
            getLastLocation();
            if (lastLocation == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
            createLocationRequest();
            mMap.setMyLocationEnabled(true);
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
        if(lastLocation != null) {
            createGeoQuery();
            moveMapToCurrentLocation(lastLocation);
        }
    }

    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 3000)        // 30 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //     Activity Lifecycle Overrides
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
//        if (geoQuery != null) {
//            geoQuery.removeAllListeners();
//        }
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
        if(mTargetList!=null){
            if(mTargetList.size()>0){
                mTargetList.clear();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(lastLocation != null){
            moveMapToCurrentLocation(lastLocation);
//            createGeoQuery();
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //     Extra methods I probably won't touch much.
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
}
