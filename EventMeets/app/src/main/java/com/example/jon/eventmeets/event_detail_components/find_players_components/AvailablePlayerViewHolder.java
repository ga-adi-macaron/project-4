package com.example.jon.eventmeets.event_detail_components.find_players_components;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jon.eventmeets.R;

/**
 * Created by Jon on 1/3/2017.
 */

class AvailablePlayerViewHolder extends RecyclerView.ViewHolder {
    ImageView mThumbnail;
    TextView mDisplayName;
    Context mContext;
    RelativeLayout mLayout;

    AvailablePlayerViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        mThumbnail = (ImageView)itemView.findViewById(R.id.player_thumb);
        mDisplayName = (TextView)itemView.findViewById(R.id.player_display_name);
        mLayout = (RelativeLayout)itemView.findViewById(R.id.available_player);
    }
}
