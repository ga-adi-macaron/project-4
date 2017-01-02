package com.example.jon.eventmeets.event_detail_components;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jon.eventmeets.R;

/**
 * Created by Jon on 12/22/2016.
 */

public class VideoGameViewHolder extends RecyclerView.ViewHolder {
//    public ImageView mNintendo, mXbox, mPlaystation, mPc;
    public TextView mGameTitle;
    public ImageView mCoverArt;
    public RelativeLayout mGameLayout;
    public Context mContext;

    public VideoGameViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        mGameLayout = (RelativeLayout)itemView.findViewById(R.id.game_result_layout);

//        mNintendo = (ImageView)itemView.findViewById(R.id.nintendo_platform);
//        mXbox = (ImageView)itemView.findViewById(R.id.xbox_platform);
//        mPlaystation = (ImageView)itemView.findViewById(R.id.playstation_platform);
//        mPc = (ImageView)itemView.findViewById(R.id.pc_platform);

        mGameTitle = (TextView)itemView.findViewById(R.id.game_title);

        mCoverArt = (ImageView)itemView.findViewById(R.id.cover_art);
    }
}
