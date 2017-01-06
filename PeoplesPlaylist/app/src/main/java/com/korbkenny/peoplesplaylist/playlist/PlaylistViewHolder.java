package com.korbkenny.peoplesplaylist.playlist;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.korbkenny.peoplesplaylist.R;
import com.korbkenny.peoplesplaylist.objects.Song;

/**
 * Created by KorbBookProReturns on 12/19/16.
 */

public class PlaylistViewHolder extends RecyclerView.ViewHolder {
    TextView mSongTitle, mSongNumber;
    ImageView mUserIcon;
    RelativeLayout mLayout;

    public PlaylistViewHolder(View itemView) {
        super(itemView);
        mSongTitle = (TextView)itemView.findViewById(R.id.vh_song_title);
        mUserIcon = (ImageView)itemView.findViewById(R.id.song_user_image);
        mSongNumber = (TextView)itemView.findViewById(R.id.vh_song_number);
        mLayout = (RelativeLayout)itemView.findViewById(R.id.vh_layout);
    }

    public void bind(final Song song, final PlaylistRecyclerAdapter.RecyclerItemClickListener listener){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickListener(song, getLayoutPosition());
            }
        });
    }

//    public void bind2(final Song song, final Song song2, final PlaylistRecyclerAdapter.RecyclerNextTrackListener listener){
//        listener.onSongCompleted(song, song2, getLayoutPosition(), getLayoutPosition()+1);
//    }


}
