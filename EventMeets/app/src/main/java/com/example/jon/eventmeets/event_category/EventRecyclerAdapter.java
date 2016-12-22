package com.example.jon.eventmeets.event_category;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.jon.eventmeets.R;
import com.example.jon.eventmeets.model.EventParent;

import java.util.List;

/**
 * Created by Jon on 12/22/2016.
 */

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerViewHolder> {
    private List<EventParent> mEventList;

    EventRecyclerAdapter(List<EventParent> list) {
        mEventList = list;
    }

    @Override
    public EventRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventRecyclerViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.event_in_category_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(EventRecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }
}
