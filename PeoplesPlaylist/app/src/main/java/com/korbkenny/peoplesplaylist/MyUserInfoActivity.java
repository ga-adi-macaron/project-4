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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
    private List<String> pListIds, pListCovers;

    private FirebaseDatabase db;
    private List<ImageView> mMyPlaylistsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_user_info);

        context = this;

        db = FirebaseDatabase.getInstance();

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
        final DatabaseReference getPlaylists = db.getReference("UserPlaylists").child(ME.getId());
        getPlaylists.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... voids) {
                        if(dataSnapshot!=null){
                            for (DataSnapshot ds:dataSnapshot.getChildren()) {
                                pListIds.add(ds.getKey());
                                pListCovers.add(ds.getValue(String.class));
                            }
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        for (int i = 0; i < pListIds.size(); i++) {
                            Picasso.with(context).load(pListCovers.get(i)).into(mMyPlaylistsList.get(i));
                            final int finalI = i;
                            mMyPlaylistsList.get(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(MyUserInfoActivity.this,PlaylistActivity.class);
                                    intent.putExtra("Playlist Id",pListIds.get(finalI));
                                    startActivity(intent);
                                }
                            });
                            final int finalI1 = i;
                            mMyPlaylistsList.get(i).setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    deletePlaylist(pListIds.get(finalI1));
                                    return false;
                                }
                            });
                        }
                    }
                }.execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//
//        //  First, get your playlist ids from the database
//         getPlaylists.addListenerForSingleValueEvent(new ValueEventListener() {
//             @Override
//             public void onDataChange(final DataSnapshot dataSnapshot) {
//                 if(dataSnapshot!=null){
//                     new AsyncTask<Void,Void,List<String>>(){
//                         @Override
//                         protected List<String> doInBackground(Void... voids) {
//                             pList = new ArrayList<>();
//                             for (DataSnapshot ds:dataSnapshot.getChildren()) {
//                                 pList.add(ds.getValue(String.class));
//                             }
//                             return pList;
//                         }
//
//                         @Override
//                         protected void onPostExecute(final List<String> strings) {
////                             deletePlaylist();
//                             for (final String s:strings) {
//                                 DatabaseReference playlistCoverRef = db.getReference("Playlists").child(s).child("cover");
//                                 playlistCoverRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                     @Override
//                                     public void onDataChange(final DataSnapshot dataSnapshot) {
//                                         new AsyncTask<Void,Void,String>(){
//                                             @Override
//                                             protected String doInBackground(Void... voids) {
//                                                 return dataSnapshot.getValue(String.class);
//
//                                             }
//
//                                             @Override
//                                             protected void onPostExecute(String string) {
//                                                 Picasso.with(MyUserInfoActivity.this)
//                                                             .load(string)
//                                                             .into(mMyPlaylistsList.get(count));
//                                                 mMyPlaylistsList.get(count).setOnClickListener(new View.OnClickListener() {
//                                                     @Override
//                                                     public void onClick(View view) {
//                                                         Intent intent = new Intent(MyUserInfoActivity.this,PlaylistActivity.class);
//                                                         intent.putExtra("Playlist Id",s);
//                                                         startActivity(intent);
//                                                         finish();
//                                                     }
//                                                 });
//                                                 count++;
//                                                 }
//
//                                         }.execute();
//
//
//                                     }
//
//                                     @Override
//                                     public void onCancelled(DatabaseError databaseError) {
//
//                                     }
//                                 });
//                             }
//
//
//                         }
//                     }.execute();
//                 }
//             }
//
//             @Override
//             public void onCancelled(DatabaseError databaseError) {
//
//             }
//         });


    }

    public void setupViews(){
        mMyUserIcon = (ImageView)findViewById(R.id.my_icon);
        mSavedPlaylist = (ImageView)findViewById(R.id.my_saved);

        mMyPlaylistsList = new ArrayList<>();

        pListIds = new ArrayList<>();
        pListCovers = new ArrayList<>();

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

    public void deletePlaylist(final String playlistId){
        new AlertDialog.Builder(MyUserInfoActivity.this)
                .setTitle("Delete Playlist")
                .setMessage("Are you sure you want to delete this playlist? Everybody's tracks will be erased.")
                .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new AsyncTask<Void,Void,Void>(){
                            @Override
                            protected Void doInBackground(Void... voids) {
                                DatabaseReference removePlaylistRef = db.getReference("Playlists").child(playlistId);
                                removePlaylistRef.removeValue();

                                DatabaseReference removeUserPlaylistRef = db.getReference("UserPlaylists").child(ME.getId()).child(playlistId);
                                removeUserPlaylistRef.removeValue();

                                ME.setPlaylistCount(ME.getPlaylistCount() - 1);
                                DatabaseReference setNewCountRef = db.getReference("Users").child(ME.getId()).child("playlistCount");
                                setNewCountRef.setValue(ME.getPlaylistCount());

                                StorageReference playlistCoverRef = FirebaseStorage.getInstance().getReference("albumcovers").child(playlistId).child("playlistcover.png");
                                playlistCoverRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                Intent intent = new Intent(MyUserInfoActivity.this, MyUserInfoActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }.execute();
                    }
                })
                .setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
