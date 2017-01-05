package com.joelimyx.politicallocal.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.news.gson.Value;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joe on 12/13/16.
 */

class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<Value> mNewsList;
    private Context mContext;
    private OnNewsItemSelectedListener mListener;

    /**
     * Callback to the NewsFragment to instantiate a Custom Chrome Tab
     */
    interface OnNewsItemSelectedListener{
        void onNewsItemSelected(String url);
    }

    NewsAdapter(List<Value> newsList, Context context, OnNewsItemSelectedListener listener) {
        mNewsList = newsList;
        mContext = context;
        mListener = listener;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_news,parent,false));
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        final Value current = mNewsList.get(position);
        if(current.getImage()!=null) {
            Picasso.with(mContext)
                    .load(current.getImage().getThumbnail().getContentUrl())
                    .fit()
                    .into(holder.mNewsImage);
        }else{
            holder.mNewsImage.setVisibility(View.GONE);
        }
        holder.mNewsTitle.setText(current.getName());
        holder.mNewsSource.setText(current.getProvider().get(0).getName());
        holder.mNewsItem.setOnClickListener(view -> mListener.onNewsItemSelected(current.getUrl()));
    }

    void clearData(){
        mNewsList.clear();
        notifyDataSetChanged();
    }

    void addData(List<Value> addOnList){
        int size = mNewsList.size()-1;
        mNewsList.addAll(addOnList);
        notifyItemRangeInserted(size,addOnList.size());
    }
    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{
        private ImageView mNewsImage;
        private TextView mNewsTitle, mNewsSource;
        private RelativeLayout mNewsItem;

        NewsViewHolder(View itemView) {
            super(itemView);
            mNewsImage = (ImageView) itemView.findViewById(R.id.news_image);
            mNewsTitle= (TextView) itemView.findViewById(R.id.news_title);
            mNewsSource = (TextView) itemView.findViewById(R.id.news_source);
            mNewsItem = (RelativeLayout) itemView.findViewById(R.id.news_item);
        }
    }
}
