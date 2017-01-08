package com.colinbradley.xboxoneutilitiesapp.profile_page.screenshots;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.colinbradley.xboxoneutilitiesapp.R;
import com.facebook.share.widget.ShareButton;
import com.squareup.picasso.Picasso;

/**
 * Created by colinbradley on 12/19/16.
 */

public class ScreenshotsViewHolder extends RecyclerView.ViewHolder {

    TextView mTitle, mGame;
    ImageView mScreenshot;
    ImageView mDLButton;
    View mRootView;
    ShareButton mShareButton;

    public ScreenshotsViewHolder(View itemView) {
        super(itemView);
        mTitle = (TextView)itemView.findViewById(R.id.screenshot_item_title);
        mGame = (TextView)itemView.findViewById(R.id.screeshot_item_game);
        mScreenshot = (ImageView)itemView.findViewById(R.id.screenshot_item_img);
        mShareButton = (ShareButton)itemView.findViewById(R.id.ss_fb_share);
        mDLButton = (ImageView)itemView.findViewById(R.id.ss_download);
        mRootView = itemView;
    }

    public void bindImage(String imgURL, Context context){
        Picasso.with(context).load(imgURL).into(mScreenshot);
    }
}
