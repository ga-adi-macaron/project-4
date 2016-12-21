package com.colinbradley.xboxoneutilitiesapp.profile_page.gameclips;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colinbradley.xboxoneutilitiesapp.R;

import java.util.List;

/**
 * Created by colinbradley on 12/19/16.
 */

public class GameClipsAdapter extends RecyclerView.Adapter<GameClipsViewHolder>{
    List<GameClip> mGameClipsList;
    OnItemSelectedListener mOnItemSelectedListener;
    Context mContext;

    public GameClipsAdapter(List<GameClip> gameClipsList, OnItemSelectedListener onItemSelectedListener, Context context) {
        mGameClipsList = gameClipsList;
        mOnItemSelectedListener = onItemSelectedListener;
        mContext = context;
    }

    public interface OnItemSelectedListener{
        void onItemSelected(String url);
    }

    @Override
    public GameClipsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gamesclips_rv_item, parent, false);
        return new GameClipsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GameClipsViewHolder holder, final int position) {
        final GameClip currentClip = mGameClipsList.get(position);
        holder.mGame.setText(mGameClipsList.get(position).getGameName());
        holder.mDescription.setText(mGameClipsList.get(position).getClipDescription());
        holder.mTitle.setText(mGameClipsList.get(position).getClipName());
        holder.bindImage(mGameClipsList.get(position).getImgURL(), mContext);

        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemSelectedListener.onItemSelected(currentClip.getClipURL());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mGameClipsList.size();
    }
}
