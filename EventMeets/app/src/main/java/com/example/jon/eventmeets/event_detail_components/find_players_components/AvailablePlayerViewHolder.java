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

public class AvailablePlayerViewHolder extends RecyclerView.ViewHolder {
    public ImageView mThumbnail;
    public TextView mDisplayName;
    public Context mContext;
    public RelativeLayout mLayout;

    public AvailablePlayerViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        mThumbnail = (ImageView)itemView.findViewById(R.id.player_thumb);
        mDisplayName = (TextView)itemView.findViewById(R.id.player_display_name);
        mLayout = (RelativeLayout)itemView.findViewById(R.id.available_player);
    }
}
