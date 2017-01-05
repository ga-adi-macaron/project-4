package com.colinbradley.xboxoneutilitiesapp.store_page.xbox_marketplace;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.colinbradley.xboxoneutilitiesapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by colinbradley on 12/29/16.
 */

public class XBMarketplaceViewHolder extends RecyclerView.ViewHolder {

    TextView mTitle;
    TextView mDevName;
    ImageView mImg;

    public XBMarketplaceViewHolder(View itemView) {
        super(itemView);
        mTitle = (TextView)itemView.findViewById(R.id.xbm_title);
        mDevName = (TextView)itemView.findViewById(R.id.xbm_dev);
        mImg = (ImageView)itemView.findViewById(R.id.xbm_main_art);

    }

    public void bindImage(String url, Context context){
        Picasso.with(context).load(url).into(mImg);
    }
}
