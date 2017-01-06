package com.korbkenny.peoplesplaylist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.korbkenny.peoplesplaylist.coloring.ColoringActivity;
import com.korbkenny.peoplesplaylist.objects.User;
import com.korbkenny.peoplesplaylist.playlist.PlaylistActivity;
import com.squareup.picasso.Picasso;

public class MyUserInfoActivity extends AppCompatActivity {
    private static final String SHARED_PREF = "MySettings";
    private static final String TAG = "MyUserInfoActivity: ";
    public String savedPlaylistId;

    private ImageView mMyUserIcon, mSavedPlaylist, my1,my2,my3,my4,my5,my6,my7,my8;
    private User ME;
    private UserSingleton sUserSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_user_info);

        setupViews();
        SharedPreferences settings = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        savedPlaylistId = settings.getString("SavedPlaylistId",null);

        sUserSingleton = UserSingleton.getInstance();
        ME = sUserSingleton.getUser();

        Picasso.with(this).load(ME.getUserImage()).into(mMyUserIcon);
        if(savedPlaylistId == null){
            Picasso.with(this).load(R.drawable.nothingsavedyet).into(mSavedPlaylist);
        } else {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("Playlists")
                    .child(savedPlaylistId)
                    .child("cover");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    if(dataSnapshot!=null && !dataSnapshot.getValue(String.class).equals("null")){
                        new AsyncTask<Void,Void,String>(){
                            @Override
                            protected String doInBackground(Void... voids) {
                                return dataSnapshot.getValue(String.class);
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                Log.d(TAG, "onPostExecute: " + s);
                                Picasso.with(MyUserInfoActivity.this).load(s).into(mSavedPlaylist);
                            }
                        }.execute();
                    } else {
                        Picasso.with(MyUserInfoActivity.this).load(R.drawable.noalbumcover).into(mSavedPlaylist);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        mMyUserIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(MyUserInfoActivity.this, ColoringActivity.class);
                startActivity(intent);
                return false;
            }
        });

        mSavedPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(savedPlaylistId!=null){
                    Intent intent = new Intent(MyUserInfoActivity.this, PlaylistActivity.class);
                    intent.putExtra("Playlist Id",savedPlaylistId);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Picasso.with(this).load(ME.getUserImage()).into(mMyUserIcon);

    }

    public void setupViews(){
        mMyUserIcon = (ImageView)findViewById(R.id.my_icon);
        mSavedPlaylist = (ImageView)findViewById(R.id.my_saved);

        my1 = (ImageView)findViewById(R.id.my_1);
        my2 = (ImageView)findViewById(R.id.my_2);
        my3 = (ImageView)findViewById(R.id.my_3);
        my4 = (ImageView)findViewById(R.id.my_4);
        my5 = (ImageView)findViewById(R.id.my_5);
        my6 = (ImageView)findViewById(R.id.my_6);
        my7 = (ImageView)findViewById(R.id.my_7);
        my8 = (ImageView)findViewById(R.id.my_8);
    }
}
