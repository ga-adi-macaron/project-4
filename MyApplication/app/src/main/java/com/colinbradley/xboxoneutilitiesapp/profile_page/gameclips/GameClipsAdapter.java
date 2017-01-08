package com.colinbradley.xboxoneutilitiesapp.profile_page.gameclips;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colinbradley.xboxoneutilitiesapp.R;
import com.facebook.share.model.ShareLinkContent;

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

    interface OnItemSelectedListener{
        void onItemSelectedToPlay(String clipURL, String imgURL, String title);
        void downloadVideo(String clipURL, String title);
    }

    @Override
    public GameClipsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gamesclips_rv_item, parent, false);
        return new GameClipsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final GameClipsViewHolder holder, final int position) {
        final GameClip currentClip = mGameClipsList.get(position);
        holder.mGame.setText(mGameClipsList.get(position).getmGameName());
        holder.mDescription.setText(mGameClipsList.get(position).getClipDescription());
        holder.mTitle.setText(mGameClipsList.get(position).getClipName());
        holder.bindImage(mGameClipsList.get(position).getmImgURL(), mContext);

        Uri imgUri = Uri.parse(mGameClipsList.get(position).getmImgURL());
        final Uri clipUri = Uri.parse(mGameClipsList.get(position).getmClipURL());
        ShareLinkContent fbShare = new ShareLinkContent.Builder()
                .setContentTitle(mGameClipsList.get(position).getClipName() + " from " + mGameClipsList.get(position).getmGameName())
                .setImageUrl(imgUri)
                .setContentUrl(clipUri)
                .build();
        holder.mShareButton.setShareContent(fbShare);


        holder.mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clipTitle = currentClip.getClipName() + "_from_" + currentClip.getmGameName();
                String noPoundSign = clipTitle.replace("#", "");
                String noSpacesTitle = noPoundSign.replace(" ", "_");
                mOnItemSelectedListener.downloadVideo(currentClip.getmClipURL(), noSpacesTitle);
            }
        });

        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemSelectedListener.onItemSelectedToPlay(currentClip.getmClipURL(), currentClip.getmImgURL(), currentClip.getClipName());

            }
        });

    }

    @Override
    public int getItemCount() {
        return mGameClipsList.size();
    }
}
