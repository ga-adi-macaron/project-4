package com.korbkenny.peoplesplaylist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyUserInfoActivity extends AppCompatActivity {
    private static final String SHARED_PREF = "MySettings";
    private static final String TAG = "MyUserInfoActivity: ";
    public String savedPlaylistId;

    private ImageView mMyUserIcon, mSavedPlaylist, my1,my2,my3,my4,my5,my6,my7,my8;
    private User ME;
    private UserSingleton sUserSingleton;
    private int playlistCount;
    private Context context;
    private List<String> pList, myPlaylistsNumbers;
    private int count = 0;

    private List<ImageView> mMyPlaylistsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_user_info);

        context = this;

        setupViews();
        SharedPreferences settings = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        savedPlaylistId = settings.getString("SavedPlaylistId",null);

        sUserSingleton = UserSingleton.getInstance();
        ME = sUserSingleton.getUser();

        loadMyPlaylists();

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
                                Picasso.with(context).load(s).into(mSavedPlaylist);
                            }
                        }.execute();
                    } else {
                        Picasso.with(context).load(R.drawable.noalbumcover).into(mSavedPlaylist);
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

        for (ImageView i:mMyPlaylistsList) {
            i.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(MyUserInfoActivity.this)
                            .setTitle("Delete Playlist")
                            .setMessage("Are you sure you want to delete this playlist? Everybody's tracks will be erased.")
                            .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return false;
                }
            });
        }

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

    public void loadMyPlaylists(){
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference getPlaylists = db.getReference("UserPlaylists").child(ME.getId());

        //  First, get your playlist ids from the database
         getPlaylists.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(final DataSnapshot dataSnapshot) {
                 if(dataSnapshot!=null){
                     new AsyncTask<Void,Void,List<String>>(){
                         @Override
                         protected List<String> doInBackground(Void... voids) {
                             pList = new ArrayList<>();
                             for (DataSnapshot ds:dataSnapshot.getChildren()) {
                                 pList.add(ds.getValue(String.class));
                             }
                             return pList;
                         }

                         @Override
                         protected void onPostExecute(final List<String> strings) {
                             for (final String s:strings) {
                                 DatabaseReference playlistCoverRef = db.getReference("Playlists").child(s).child("cover");
                                 playlistCoverRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(final DataSnapshot dataSnapshot) {
                                         new AsyncTask<Void,Void,String>(){
                                             @Override
                                             protected String doInBackground(Void... voids) {
                                                 return dataSnapshot.getValue(String.class);

                                             }

                                             @Override
                                             protected void onPostExecute(String string) {
                                                 Picasso.with(MyUserInfoActivity.this)
                                                             .load(string)
                                                             .into(mMyPlaylistsList.get(count));
                                                 mMyPlaylistsList.get(count).setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View view) {
                                                         Intent intent = new Intent(MyUserInfoActivity.this,PlaylistActivity.class);
                                                         intent.putExtra("Playlist Id",s);
                                                         startActivity(intent);
                                                         finish();
                                                     }
                                                 });
                                                 count++;
                                                 }

                                         }.execute();


                                     }

                                     @Override
                                     public void onCancelled(DatabaseError databaseError) {

                                     }
                                 });
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

    public void setupViews(){
        mMyUserIcon = (ImageView)findViewById(R.id.my_icon);
        mSavedPlaylist = (ImageView)findViewById(R.id.my_saved);

        mMyPlaylistsList = new ArrayList<>();

        my1 = (ImageView)findViewById(R.id.my_1);
        my2 = (ImageView)findViewById(R.id.my_2);
        my3 = (ImageView)findViewById(R.id.my_3);
        my4 = (ImageView)findViewById(R.id.my_4);
        my5 = (ImageView)findViewById(R.id.my_5);
        my6 = (ImageView)findViewById(R.id.my_6);
        my7 = (ImageView)findViewById(R.id.my_7);
        my8 = (ImageView)findViewById(R.id.my_8);

        mMyPlaylistsList.add(my1);
        mMyPlaylistsList.add(my2);
        mMyPlaylistsList.add(my3);
        mMyPlaylistsList.add(my4);
        mMyPlaylistsList.add(my5);
        mMyPlaylistsList.add(my6);
        mMyPlaylistsList.add(my7);
        mMyPlaylistsList.add(my8);

    }

    public void deletePlaylist(){

    }
}
