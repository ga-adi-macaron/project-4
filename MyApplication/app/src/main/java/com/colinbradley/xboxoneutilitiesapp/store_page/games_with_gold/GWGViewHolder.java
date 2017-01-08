package com.colinbradley.xboxoneutilitiesapp.store_page.games_with_gold;

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

public class GWGViewHolder extends RecyclerView.ViewHolder {

    TextView mOriginalPrice;
    ImageView mBoxArt;
    TextView mTitle;

    public GWGViewHolder(View itemView) {
        super(itemView);
        mTitle = (TextView)itemView.findViewById(R.id.gwg_title);
        mBoxArt = (ImageView)itemView.findViewById(R.id.gwg_box_art);
        mOriginalPrice = (TextView)itemView.findViewById(R.id.gwg_original_price);
    }

    public void bindImageToView(Context context, String imgURL){
        Picasso.with(context).load(imgURL).into(mBoxArt);
    }
}
