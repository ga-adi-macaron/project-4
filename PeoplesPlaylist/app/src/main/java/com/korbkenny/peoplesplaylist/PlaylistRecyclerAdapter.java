package com.korbkenny.peoplesplaylist;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by KorbBookProReturns on 12/19/16.
 */

public class PlaylistRecyclerAdapter extends RecyclerView.Adapter<PlaylistViewHolder> {
    List<Song> mSongList;

    public PlaylistRecyclerAdapter(List<Song> songList) {
        mSongList = songList;
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlaylistViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_viewholder,parent,false));
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, int position) {
        holder.mSongTitle.setText(mSongList.get(position).getTitle());
        holder.mUserName.setText("UserName123");
        if(position % 2 == 1){
            holder.mLayout.setBackgroundColor(Color.parseColor("#dddddd"));
        }
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }
}
