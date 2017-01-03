package com.example.jon.eventmeets.event_detail_components.find_players_components;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jon.eventmeets.R;
import com.example.jon.eventmeets.model.AvailablePlayer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jon on 1/3/2017.
 */

public class AvailablePlayerRecycler extends RecyclerView.Adapter<AvailablePlayerViewHolder> {
    private List<String> mPlayerKeys;
    private FirebaseDatabase mDatabase;
    private List<AvailablePlayer> mPlayers;

    public AvailablePlayerRecycler(List<String> list) {
        mPlayerKeys = list;
        mDatabase = FirebaseDatabase.getInstance();
        mPlayers = new ArrayList<>();
    }

    @Override
    public AvailablePlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AvailablePlayerViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.available_player_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final AvailablePlayerViewHolder holder, int position) {
        String key = mPlayerKeys.get(position);

        mDatabase.getReference("users").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String firstName = (String)dataSnapshot.child("firstName").getValue();
                String picture = (String)dataSnapshot.child("thumb").getValue();
                String key = dataSnapshot.getKey();
                mPlayers.add(new AvailablePlayer(key, firstName, picture));

                holder.mDisplayName.setText(firstName);
                if(picture.equals("none")) {
                    holder.mThumbnail.setImageResource(R.drawable.ic_account_circle_black_48dp);
                } else {
                    Picasso.with(holder.mContext).load(picture).into(holder.mThumbnail);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("HERE", "onCancelled: "+databaseError);
            }
        });

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AvailablePlayer player = mPlayers.get(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlayerKeys.size();
    }
}
