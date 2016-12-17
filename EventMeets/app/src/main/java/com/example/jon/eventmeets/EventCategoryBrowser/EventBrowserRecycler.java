package com.example.jon.eventmeets.EventCategoryBrowser;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jon.eventmeets.Model.Category;
import com.example.jon.eventmeets.R;

import java.util.List;

/**
 * Created by Jon on 12/16/2016.
 */

public class EventBrowserRecycler extends RecyclerView.Adapter<EventBrowserRecycler.EventBrowserViewHolder>{
    private List<Category> mCategories;

    public EventBrowserRecycler(List<Category> list) {
        mCategories = list;
    }

    @Override
    public EventBrowserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventBrowserViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.event_browser_category, parent, false));
    }

    @Override
    public void onBindViewHolder(EventBrowserViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    class EventBrowserViewHolder extends RecyclerView.ViewHolder {
        TextView mCategoryName;


        public EventBrowserViewHolder(View itemView) {
            super(itemView);
        }
    }
}
