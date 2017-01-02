package com.example.jon.eventmeets.event_detail_components;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.jon.eventmeets.R;
import com.example.jon.eventmeets.model.GameResultObject;
import com.example.jon.eventmeets.model.VideoGamingEvent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jon on 12/22/2016.
 */

public class VideoGamesRecyclerAdapter extends RecyclerView.Adapter<VideoGameViewHolder> {
    private List<GameResultObject> mVideogames;
    private Intent mIntent;
    private boolean mNintendo, mXbox, mPc, mPlaystation;

    public VideoGamesRecyclerAdapter(List<GameResultObject> list) {
        mVideogames = list;
    }


    @Override
    public VideoGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoGameViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.game_event_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final VideoGameViewHolder holder, int position) {
        final GameResultObject game = mVideogames.get(position);

//        double dHeight = (double)game.getCover().getHeight();
//        double dWidth = (double)game.getCover().getWidth();
//        double ratio = dWidth/dHeight;
//        dWidth = 150*ratio;
//
//        Log.d("tag", "onBindViewHolder: "+dHeight+", "+dWidth);
//
//        holder.mCoverArt.getLayoutParams().height = 150;
//        holder.mCoverArt.getLayoutParams().width = (int)dWidth;
//        holder.mCoverArt.requestLayout();

        if(game.getCover() != null&&game.getCover().getUrl().length() > 0)
            Picasso.with(holder.mContext).load("https:"+game.getCover().getUrl()).into(holder.mCoverArt);

        holder.mGameTitle.setText(game.getName());

        holder.mGameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent= new Intent(holder.mContext, EventDetailActivity.class);
                String image = game.getScreenshots().get(0).getCloudinary_id();

                mIntent.putExtra("summary", game.getSummary());
                mIntent.putExtra("name", game.getName());
                mIntent.putExtra("id", game.getId());
                mIntent.putExtra("image", image);
                holder.mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideogames.size();
    }
}
