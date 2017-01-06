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
    public TextView mParentName, mUserInitial;

    MainMenuEventParentViewHolder(View itemView) {
        super(itemView);

        mParentName = (TextView)itemView.findViewById(R.id.event_item_title);
        mUserInitial = (TextView)itemView.findViewById(R.id.user_main).findViewById(R.id.user_initial_main);
    }
}
