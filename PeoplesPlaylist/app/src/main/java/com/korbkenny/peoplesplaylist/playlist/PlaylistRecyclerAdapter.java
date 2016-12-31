package com.korbkenny.peoplesplaylist.playlist;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.korbkenny.peoplesplaylist.R;
import com.korbkenny.peoplesplaylist.objects.Song;
import com.squareup.picasso.Picasso;

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
        holder.mSongNumber.setText("hey");

        Picasso.with(holder.mUserIcon.getContext())
                .load(mSongList.get(position).getUserImage())
                .into(holder.mUserIcon);

        if (position % 2 == 1) {
            holder.mLayout.setBackgroundColor(Color.parseColor("#dddddd"));
        }




    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }
}
