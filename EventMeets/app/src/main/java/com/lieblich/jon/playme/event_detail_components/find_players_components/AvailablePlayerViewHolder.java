package com.lieblich.jon.playme.event_detail_components.find_players_components;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lieblich.jon.playme.R;

/**
 * Created by Jon on 1/3/2017.
 */

class AvailablePlayerViewHolder extends RecyclerView.ViewHolder {
    TextView mDisplayName, mPlayerInitial;
    Context mContext;
    RelativeLayout mLayout;
    FrameLayout mIcon;

    AvailablePlayerViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        mDisplayName = (TextView)itemView.findViewById(R.id.player_display_name);
        mLayout = (RelativeLayout)itemView.findViewById(R.id.available_player);
        mPlayerInitial = (TextView)itemView.findViewById(R.id.available_player_letter);
        mIcon = (FrameLayout)itemView.findViewById(R.id.available_player_icon);
    }
}
