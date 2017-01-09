package com.lieblich.jon.playme.event_detail_components.find_players_components;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lieblich.jon.playme.R;
import com.lieblich.jon.playme.event_detail_components.message_components.ChatGroupActivity;
import com.lieblich.jon.playme.event_detail_components.message_components.MessageGroup;
import com.lieblich.jon.playme.model.AvailablePlayer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Jon on 1/3/2017.
 */

class AvailablePlayerRecycler extends RecyclerView.Adapter<AvailablePlayerViewHolder> {
    private FirebaseDatabase mDatabase;
    private List<AvailablePlayer> mPlayers;
    private AvailablePlayer mPlayer;
    private MessageGroup mGroup;

    AvailablePlayerRecycler(List<AvailablePlayer> list) {
        mPlayers = list;
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public AvailablePlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AvailablePlayerViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.available_player_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final AvailablePlayerViewHolder holder, int position) {
        String key = mPlayers.get(position).getUser();

        if(!key.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

            holder.mDisplayName.setText(mPlayers.get(position).getFirstName());
            holder.mPlayerInitial.setText(""+mPlayers.get(position).getFirstName().charAt(0));

            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String added = mPlayers.get(holder.getAdapterPosition()).getUser();
                    final String name = mPlayers.get(holder.getAdapterPosition()).getFirstName();
                    final String current = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    final String chatKey = current.substring(0, 6) + added.substring(0, 6);
                    mDatabase.getReference("users").child(current).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String myName = dataSnapshot.child("firstName").getValue(String.class);
                            mGroup = new MessageGroup();
                            mGroup.addUsers(mPlayers.get(holder.getAdapterPosition()),
                                    new AvailablePlayer(current, myName));
                            confirmPlayerSelection(myName, name, chatKey, holder.mContext, added, current);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        } else {
            holder.mDisplayName.setText("(YOU)");
            holder.mIcon.setVisibility(View.GONE);
            mPlayers.add(new AvailablePlayer("you", "you"));
            holder.mLayout.setBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

    private void confirmPlayerSelection(final String me, final String name, final String chatKey,
                                        final Context context, final String id, final String myId) {
        mDatabase.getReference("chats").child(chatKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("users")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                            .setTitle("Start a new chat with " + name + "?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mGroup.addCreateMessage(me);

                                    Intent intent = new Intent(context, ChatGroupActivity.class);
                                    intent.putExtra("chatKey", chatKey);

                                    mDatabase.getReference("users").child(id).child("chats").child(chatKey).setValue(me);
                                    mDatabase.getReference("users").child(myId).child("chats").child(chatKey).setValue(name);
                                    mDatabase.getReference("chats").child(chatKey).setValue(mGroup);

                                    context.startActivity(intent);
                                }
                            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    Intent intent = new Intent(context, ChatGroupActivity.class);
                    intent.putExtra("chatKey", chatKey);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
