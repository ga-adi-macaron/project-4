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
        return new MainMenuEventParentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_menu_event_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(MainMenuEventParentViewHolder holder, int position) {
//        EventParent event = mEventList.get(position);
//        event.setIcon(holder.mParentIcon);
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }
}
