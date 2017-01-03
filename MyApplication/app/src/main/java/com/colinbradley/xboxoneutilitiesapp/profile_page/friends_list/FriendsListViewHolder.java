package com.colinbradley.xboxoneutilitiesapp.profile_page.friends_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.colinbradley.xboxoneutilitiesapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by colinbradley on 12/19/16.
 */

public class FriendsListViewHolder extends RecyclerView.ViewHolder {

    TextView mGamertag;
    ImageView mProfilePic;
    ImageButton mGoToProfile;
    View mRootView;

    public FriendsListViewHolder(View itemView) {
        super(itemView);
        mGamertag = (TextView)itemView.findViewById(R.id.friends_gamertag);
        mProfilePic = (ImageView)itemView.findViewById(R.id.friends_profile_pic);
        mGoToProfile = (ImageButton)itemView.findViewById(R.id.friends_go_to);
        mRootView = itemView;
    }

    public void bindImage(String imgURL, Context context){
        Picasso.with(context).load(imgURL).into(mProfilePic);
    }
}
