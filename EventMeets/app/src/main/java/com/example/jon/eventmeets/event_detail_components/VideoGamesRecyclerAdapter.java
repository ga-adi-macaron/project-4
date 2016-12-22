package com.example.jon.eventmeets.event_detail_components;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.jon.eventmeets.model.game_models.VideoGamingEvent;

import java.util.List;

/**
 * Created by Jon on 12/22/2016.
 */

public class VideoGamesRecyclerAdapter extends RecyclerView.Adapter<VideoGameViewHolder> {
    private List<VideoGamingEvent> mVideogames;

    @Override
    public VideoGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(VideoGameViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mVideogames.size();
    }
}
