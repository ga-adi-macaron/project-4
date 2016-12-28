package com.example.jon.eventmeets.event_detail_components;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jon.eventmeets.R;
import com.example.jon.eventmeets.model.game_models.VideoGamingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jon on 12/22/2016.
 */

public class VideoGamesRecyclerAdapter extends RecyclerView.Adapter<VideoGameViewHolder> {
    private List<VideoGamingEvent> mVideogames;

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
        String[] platforms = event.getPlatforms();
        ArrayList<String> consolidated = new ArrayList<>(7);
        boolean hasNintendo = false;
        boolean hasXbox = false;
        boolean hasPc = false;
        boolean hasPlaystation = false;
        for(int i=0;i<platforms.length;i++) {
            switch(platforms[i]) {
                case "Game Boy":
                case "Game Boy Advance":
                case "Game Boy Color":
                case "Super Nintendo Entertainment System":
                case "Nintendo Entertainment System":
                    //Classic Nintendo
                    consolidated.add("GB");
                    break;
                case "Mac":
                case "PC":
                case "Linux":
                    //Computer
                    consolidated.add("PC");
                    hasPc = true;
                    break;
                case "Nintendo 3DS":
                case "New Nintendo 3DS":
                case "Nintendo DS":
                    //Modern Nintendo Handhelds
                    consolidated.add("DS");
                    break;
                case "Nintendo Switch":
                case "Wii U":
                case "GameCube":
                case "Wii":
                case "Nintendo 64":
                    //Modern Nintendo Consoles
                    consolidated.add("NTN");
                    hasNintendo = true;
                    break;
                case "Xbox One":
                case "Xbox 360":
                case "Xbox":
                    //Microsoft Consoles
                    consolidated.add("XBX");
                    hasXbox = true;
                    break;
                case "PlayStation 4":
                case "PlayStation 3":
                case "PlayStation 2":
                case "PlayStation":
                    //Sony Consoles
                    consolidated.add("PS");
                    hasPlaystation = true;
                    break;
                case "PlayStation Portable":
                case "PlayStation Vita":
                    //Sony Handhelds
                    consolidated.add("PSP");
                    break;
            }
        }
        if(hasNintendo) {
            holder.mNintendo.setVisibility(View.VISIBLE);
        }
        if(hasPc) {
            holder.mPc.setVisibility(View.VISIBLE);
        }
        if(hasXbox) {
            holder.mXbox.setVisibility(View.VISIBLE);
        }
        if(hasPlaystation) {
            holder.mPlaystation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mVideogames.size();
    }
}
