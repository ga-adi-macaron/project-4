package com.korbkenny.peoplesplaylist.playlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.korbkenny.peoplesplaylist.R;

/**
 * Created by KorbBookProReturns on 12/19/16.
 */

public class PlaylistViewHolder extends RecyclerView.ViewHolder {
    TextView mSongTitle, mUserName, mSongNumber;
    RelativeLayout mLayout;

    public PlaylistViewHolder(View itemView) {
        super(itemView);
        mSongTitle = (TextView)itemView.findViewById(R.id.vh_song_title);
        mUserName = (TextView)itemView.findViewById(R.id.vh_user_name);
        mSongNumber = (TextView)itemView.findViewById(R.id.vh_song_number);

        mLayout = (RelativeLayout)itemView.findViewById(R.id.vh_layout);
    }
}
