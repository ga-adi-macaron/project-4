package com.joelimyx.politicallocal.news;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joelimyx.politicallocal.R;

import java.util.List;

/**
 * Created by Joe on 12/13/16.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    List<News> mNewsList;

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_news,parent,false));
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{

        public NewsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
