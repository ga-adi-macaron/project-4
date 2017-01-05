package com.colinbradley.xboxoneutilitiesapp.profile_page.screenshots;

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

public class ScreenshotsAdapter extends RecyclerView.Adapter<ScreenshotsViewHolder> {

    List<Screenshot> mScreenshotList;
    OnItemSelectedListener mOnItemSelectedListener;
    Context mContext;

    public ScreenshotsAdapter(List<Screenshot> screenshotList, OnItemSelectedListener onItemSelectedListener, Context context) {
        mScreenshotList = screenshotList;
        mOnItemSelectedListener = onItemSelectedListener;
        mContext = context;
    }

    public interface OnItemSelectedListener{
        void onItemSelected(String imgUrl);
        void downloadImg(String imgUrl, String title);
    }

    @Override
    public ScreenshotsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.screenshot_rv_item, parent, false);
        return new ScreenshotsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ScreenshotsViewHolder holder, int position) {
        final Screenshot currentSS = mScreenshotList.get(position);

        holder.mTitle.setText(mScreenshotList.get(position).getTitle());
        holder.mGame.setText(mScreenshotList.get(position).getGame());
        holder.bindImage(mScreenshotList.get(position).getImgURL(), mContext);

        Uri imgURL = Uri.parse(mScreenshotList.get(position).getImgURL());
        ShareLinkContent fbShare = new ShareLinkContent.Builder()
                .setImageUrl(imgURL)
                .setContentTitle(mScreenshotList.get(position).getTitle() + " from " + mScreenshotList.get(position).getGame())
                .build();
        holder.mShareButton.setShareContent(fbShare);

        holder.mDLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemSelectedListener.downloadImg(currentSS.getImgURL(), "Screenshot_");
            }
        });


        holder.mScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemSelectedListener.onItemSelected(currentSS.getImgURL());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mScreenshotList.size();
    }
}
