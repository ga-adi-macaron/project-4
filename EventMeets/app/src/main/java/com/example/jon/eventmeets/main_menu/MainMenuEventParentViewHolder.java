package com.example.jon.eventmeets.main_menu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jon.eventmeets.R;

/**
 * Created by Jon on 12/16/2016.
 */

class MainMenuEventParentViewHolder extends RecyclerView.ViewHolder {
    public ImageView mParentIcon;
    public TextView mParentName, mLocationText;

    MainMenuEventParentViewHolder(View itemView) {
        super(itemView);

        mParentIcon = (ImageView)itemView.findViewById(R.id.category_icon);

        mParentName = (TextView)itemView.findViewById(R.id.event_item_title);
        mLocationText = (TextView)itemView.findViewById(R.id.event_item_location);
    }
}
