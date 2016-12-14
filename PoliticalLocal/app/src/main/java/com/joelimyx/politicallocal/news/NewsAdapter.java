package com.joelimyx.politicallocal.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.news.Gson.News;
import com.joelimyx.politicallocal.news.Gson.Value;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joe on 12/13/16.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    List<Value> mNewsList;
    private Context mContext;

    public NewsAdapter(List<Value> newsList, Context context) {
        mNewsList = newsList;
        mContext = context;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_news,parent,false));
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        Value current = mNewsList.get(position);
        if(current.getImage()!=null) {
            Picasso.with(mContext)
                    .load(current.getImage().getThumbnail().getContentUrl())
                    .fit()
                    .into(holder.mNewsImage);
        }
        holder.mNewsTitle.setText(current.getName());
        holder.mNewsSource.setText(current.getProvider().get(0).getName());
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{
        private ImageView mNewsImage;
        private TextView mNewsTitle, mNewsSource;
        public NewsViewHolder(View itemView) {
            super(itemView);
            mNewsImage = (ImageView) itemView.findViewById(R.id.news_image);
            mNewsTitle= (TextView) itemView.findViewById(R.id.news_title);
            mNewsSource = (TextView) itemView.findViewById(R.id.news_source);
        }
    }
}
