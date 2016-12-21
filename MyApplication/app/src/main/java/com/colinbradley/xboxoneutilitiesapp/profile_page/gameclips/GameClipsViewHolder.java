package com.colinbradley.xboxoneutilitiesapp.profile_page.gameclips;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.colinbradley.xboxoneutilitiesapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by colinbradley on 12/19/16.
 */

public class GameClipsViewHolder extends RecyclerView.ViewHolder {
    TextView mTitle, mDescription, mGame;
    ImageView mImage;
    View mRootView;

    public GameClipsViewHolder(View itemView) {
        super(itemView);
        mTitle = (TextView)itemView.findViewById(R.id.gameclips_item_title);
        mDescription = (TextView)itemView.findViewById(R.id.gameclips_item_description);
        mGame = (TextView)itemView.findViewById(R.id.gameclips_item_game);
        mImage = (ImageView)itemView.findViewById(R.id.gameclips_item_img);
        mRootView = itemView;
    }

    public void bindImage(String imgURL, Context context){
        Picasso.with(context).load(imgURL).into(mImage);
    }
}
