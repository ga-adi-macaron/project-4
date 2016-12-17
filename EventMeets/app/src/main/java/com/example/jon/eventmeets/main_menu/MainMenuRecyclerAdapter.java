package com.example.jon.eventmeets.main_menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.jon.eventmeets.Model.EventParent;
import com.example.jon.eventmeets.R;

import java.util.List;

public class MainMenuRecyclerAdapter extends RecyclerView.Adapter<MainMenuEventParentViewHolder>{
    private List<EventParent> mEventList;

    public MainMenuRecyclerAdapter(List<EventParent> list) {
        mEventList = list;
    }

    @Override
    public MainMenuEventParentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(parent.getId()) {
            case R.layout.game_event_layout:
                return new MainMenuEventParentViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.game_event_layout, parent, false));
            case R.layout.conversation_event_layout:
                return new MainMenuEventParentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conversation_event_layout, parent, false));
            case R.layout.drink_event_layout:
                return new MainMenuEventParentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drink_event_layout, parent, false));
            case R.layout.nature_event_layout:
                return new MainMenuEventParentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nature_event_layout, parent, false));
            case R.layout.taste_event_layout:
                return new MainMenuEventParentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.taste_event_layout, parent, false));
            case R.layout.theater_event_layout:
                return new MainMenuEventParentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.theater_event_layout, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(MainMenuEventParentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }
}
