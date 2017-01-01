package com.example.jon.eventmeets.event_detail_components;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jon.eventmeets.R;
import com.example.jon.eventmeets.model.VideoGamingEvent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jon on 12/22/2016.
 */

public class VideoGamesRecyclerAdapter extends RecyclerView.Adapter<VideoGameViewHolder> {
    private List<VideoGamingEvent> mVideogames;
    private Intent mIntent;
    private boolean mNintendo, mXbox, mPc, mPlaystation;

    public VideoGamesRecyclerAdapter(List<VideoGamingEvent> list) {
        mVideogames = list;
    }

    @Override
    public VideoGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoGameViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.game_event_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final VideoGameViewHolder holder, int position) {
        final VideoGamingEvent event = mVideogames.get(position);

        holder.mGameTitle.setText(event.getName());

        if(event.getImage() != null&&event.getImage().getThumb_url() != null)
            Picasso.with(holder.mContext).load(event.getImage().getThumb_url()).into(holder.mCoverArt);


        ArrayList<VideoGamePlatforms> platforms = event.getPlatforms();
        mNintendo = false;
        mXbox = false;
        mPc = false;
        mPlaystation = false;
        for(int i=0;i<platforms.size();i++) {
            switch(platforms.get(i).getName()) {
                case "Game Boy":
                case "Game Boy Advance":
                case "Game Boy Color":
                case "Super Nintendo Entertainment System":
                case "Nintendo Entertainment System":
                    //Classic Nintendo
                    break;
                case "Mac":
                case "PC":
                case "Linux":
                    //Computer
                    mPc = true;
                    break;
                case "Nintendo 3DS":
                case "New Nintendo 3DS":
                case "Nintendo DS":
                    //Modern Nintendo Handhelds
                    break;
                case "Nintendo Switch":
                case "Wii U":
                case "GameCube":
                case "Wii":
                case "Nintendo 64":
                    //Modern Nintendo Consoles
                    mNintendo = true;
                    break;
                case "Xbox One":
                case "Xbox 360":
                case "Xbox":
                    //Microsoft Consoles
                    mXbox = true;
                    break;
                case "PlayStation 4":
                case "PlayStation 3":
                case "PlayStation 2":
                case "PlayStation":
                    //Sony Consoles
                    mPlaystation = true;
                    break;
                case "PlayStation Portable":
                case "PlayStation Vita":
                    //Sony Handhelds
                    break;
            }
        }
        if(mNintendo) {
            holder.mNintendo.setVisibility(View.VISIBLE);
        } else {
            holder.mNintendo.setVisibility(View.INVISIBLE);
        }
        if(mPc) {
            holder.mPc.setVisibility(View.VISIBLE);
        } else {
            holder.mPc.setVisibility(View.INVISIBLE);
        }
        if(mXbox) {
            holder.mXbox.setVisibility(View.VISIBLE);
        } else {
            holder.mXbox.setVisibility(View.INVISIBLE);
        }
        if(mPlaystation) {
            holder.mPlaystation.setVisibility(View.VISIBLE);
        } else {
            holder.mPlaystation.setVisibility(View.INVISIBLE);
        }

        holder.mGameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(holder.mContext, EventDetailActivity.class);

                mIntent.putExtra("id'", event.getId());
                mIntent.putExtra("name", event.getName());
                mIntent.putExtra("image", event.getImage().getScreen_url());
                mIntent.putExtra("nintendo", mNintendo);
                mIntent.putExtra("pc", mPc);
                mIntent.putExtra("xbox", mXbox);
                mIntent.putExtra("playstation", mPlaystation);

                holder.mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideogames.size();
    }
}
