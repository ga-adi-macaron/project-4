package com.example.jon.eventmeets.event_detail_components;

import android.content.Intent;
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

    public VideoGamesRecyclerAdapter(List<VideoGamingEvent> list) {
        mVideogames = list;
    }

    @Override
    public VideoGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoGameViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.game_event_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(VideoGameViewHolder holder, int position) {
        VideoGamingEvent event = mVideogames.get(position);

        holder.mGameTitle.setText(event.getName());

        if(event.getImage() != null&&event.getImage().getThumb_url() != null)
            Picasso.with(holder.mCoverArt.getContext()).load(event.getImage().getThumb_url()).into(holder.mCoverArt);

        holder.mGameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ArrayList<VideoGamePlatforms> platforms = event.getPlatforms();
        boolean hasNintendo = false;
        boolean hasXbox = false;
        boolean hasPc = false;
        boolean hasPlaystation = false;
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
                    hasPc = true;
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
                    hasNintendo = true;
                    break;
                case "Xbox One":
                case "Xbox 360":
                case "Xbox":
                    //Microsoft Consoles
                    hasXbox = true;
                    break;
                case "PlayStation 4":
                case "PlayStation 3":
                case "PlayStation 2":
                case "PlayStation":
                    //Sony Consoles
                    hasPlaystation = true;
                    break;
                case "PlayStation Portable":
                case "PlayStation Vita":
                    //Sony Handhelds
                    break;
            }
        }
        if(hasNintendo) {
            holder.mNintendo.setVisibility(View.VISIBLE);
        } else {
            holder.mNintendo.setVisibility(View.INVISIBLE);
        }
        if(hasPc) {
            holder.mPc.setVisibility(View.VISIBLE);
        } else {
            holder.mPc.setVisibility(View.INVISIBLE);
        }
        if(hasXbox) {
            holder.mXbox.setVisibility(View.VISIBLE);
        } else {
            holder.mXbox.setVisibility(View.INVISIBLE);
        }
        if(hasPlaystation) {
            holder.mPlaystation.setVisibility(View.VISIBLE);
        } else {
            holder.mPlaystation.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mVideogames.size();
    }
}
