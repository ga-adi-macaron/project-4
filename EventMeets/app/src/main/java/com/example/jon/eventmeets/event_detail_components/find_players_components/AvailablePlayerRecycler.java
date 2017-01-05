package com.example.jon.eventmeets.event_detail_components.find_players_components;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jon.eventmeets.R;
import com.example.jon.eventmeets.event_detail_components.message_components.ChatGroupActivity;
import com.example.jon.eventmeets.event_detail_components.message_components.MessageGroup;
import com.example.jon.eventmeets.model.AvailablePlayer;
import com.example.jon.eventmeets.model.BaseUser;
import com.google.firebase.auth.FirebaseAuth;
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

class AvailablePlayerRecycler extends RecyclerView.Adapter<AvailablePlayerViewHolder> {
    private List<String> mPlayerKeys;
    private FirebaseDatabase mDatabase;
    private List<AvailablePlayer> mPlayers;

    AvailablePlayerRecycler(List<String> list) {
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

        if(!key.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            mDatabase.getReference("users").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String firstName = (String)dataSnapshot.child("firstName").getValue();
                    String picture = (String)dataSnapshot.child("thumb").getValue();
                    String key = dataSnapshot.getKey();
                    mPlayers.add(new AvailablePlayer(key, firstName));

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
                    String added = mPlayers.get(holder.getAdapterPosition()).getUser();
                    String name = mPlayers.get(holder.getAdapterPosition()).getDisplay();
                    String current = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String chatKey = current.substring(0, 6) + added.substring(0, 6);
                    confirmPlayerSelection(mPlayers.get(holder.getAdapterPosition()), name, chatKey, holder.mContext);
                }
            });
        } else {
            holder.mDisplayName.setText("(YOU)");
            holder.mLayout.setBackgroundColor(Color.BLUE);
        }
    }

    @Override
    public int getItemCount() {
        return mPlayerKeys.size();
    }

    private void confirmPlayerSelection(final AvailablePlayer player, String name, final String chatKey, final Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                .setTitle("Start a new chat with "+name+"?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final MessageGroup group = new MessageGroup();
                        group.addUsers(new AvailablePlayer(BaseUser.getInstance().getUsername(),
                                BaseUser.getInstance().getFirstName()), player);
                        group.addCreateMessage(BaseUser.getInstance().getFirstName());

                        mDatabase.getReference("chats").child(chatKey).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Intent intent = new Intent(context, ChatGroupActivity.class);
                                intent.putExtra("chatKey", chatKey);

                                if(!dataSnapshot.hasChild("users")) {
                                    mDatabase.getReference("chats").child(chatKey).setValue(group);
                                }

                                context.startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }
}
