package com.example.jon.eventmeets.main_menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.jon.eventmeets.R;
import com.example.jon.eventmeets.model.VideoGamingEvent;

import java.util.List;

public class MainMenuRecyclerAdapter extends RecyclerView.Adapter<MainMenuEventParentViewHolder>{
    private List<VideoGamingEvent> mEventList;

    public MainMenuRecyclerAdapter(List<VideoGamingEvent> list) {
        mEventList = list;
    }

    @Override
    public MainMenuEventParentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainMenuEventParentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_menu_event_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(MainMenuEventParentViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }
}
