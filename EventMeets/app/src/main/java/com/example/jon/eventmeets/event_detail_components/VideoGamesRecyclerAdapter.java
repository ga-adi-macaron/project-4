package com.example.jon.eventmeets.event_detail_components;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.jon.eventmeets.R;
import com.example.jon.eventmeets.model.GameResultObject;
import com.example.jon.eventmeets.model.VideoGamingEvent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jon on 12/22/2016.
 */

public class VideoGamesRecyclerAdapter extends RecyclerView.Adapter<VideoGameViewHolder> {
    private List<GameResultObject> mVideogames;
    private Intent mIntent;
    private boolean mNintendo, mXbox, mPc, mPlaystation;

    public VideoGamesRecyclerAdapter(List<GameResultObject> list) {
        mVideogames = list;
    }


    @Override
    public VideoGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoGameViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.game_event_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final VideoGameViewHolder holder, int position) {
        final GameResultObject game = mVideogames.get(position);

        mNintendo = false;
        mXbox = false;
        mPc = false;
        mPlaystation = false;

        for(int i=0;i<game.getRelease_dates().size();i++) {
            int platform = game.getRelease_dates().get(i).getPlatform();
            switch(platform) {
                case 4:
                case 5:
                case 18:
                case 19:
                case 21:
                case 41:
                case 130:
                    mNintendo = true;
                    break;
                case 3:
                case 6:
                case 14:
                    mPc = true;
                    break;
                case 7:
                case 8:
                case 9:
                case 38:
                case 46:
                case 48:
                    mPlaystation = true;
                    break;
                case 11:
                case 12:
                case 49:
                    mXbox = true;
                    break;
            }
        }

        if(game.getCover() != null&&game.getCover().getUrl().length() > 0) {
            String cover = "https://images.igdb.com/igdb/image/upload/t_cover_small/"
                    +game.getCover().getCloudinary_id()+".jpg";
            Picasso.with(holder.mContext).load(cover).into(holder.mCoverArt);
        }
        holder.mGameTitle.setText(game.getName());

        holder.mGameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent= new Intent(holder.mContext, EventDetailActivity.class);
                String image = game.getScreenshots().get(0).getCloudinary_id();

                consoleAssignments(mIntent);
                mIntent.putExtra("summary", game.getSummary());
                mIntent.putExtra("name", game.getName());
                mIntent.putExtra("id", game.getId());
                mIntent.putExtra("image", image);
                holder.mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideogames.size();
    }

    private void consoleAssignments(Intent intent) {
        int count = 0;
        ArrayList<String> consoles = new ArrayList<>();
        if(mXbox) {
            consoles.add("XBox");
            count++;
        }
        if(mNintendo) {
            consoles.add("Nintendo");
            count++;
        }
        if(mPlaystation) {
            consoles.add("PlayStation");
            count++;
        }
        if(mPc) {
            consoles.add("PC");
            count++;
        }
        intent.putExtra("platforms", consoles);
        intent.putExtra("numPlatforms", count);
    }
}
