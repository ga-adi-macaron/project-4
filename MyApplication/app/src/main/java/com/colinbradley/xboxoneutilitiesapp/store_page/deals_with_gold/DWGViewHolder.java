package com.colinbradley.xboxoneutilitiesapp.store_page.deals_with_gold;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.colinbradley.xboxoneutilitiesapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by colinbradley on 1/3/17.
 */

public class DWGViewHolder extends RecyclerView.ViewHolder {
    TextView mTitle, mOriginalPrice, mNewPrice;
    ImageView mImage;

    public DWGViewHolder(View itemView) {
        super(itemView);
        mTitle = (TextView)itemView.findViewById(R.id.dwg_title);
        mOriginalPrice = (TextView)itemView.findViewById(R.id.dwg_original_price);
        mNewPrice = (TextView)itemView.findViewById(R.id.dwg_new_price);
        mImage = (ImageView)itemView.findViewById(R.id.dwg_box_art);
    }

    public void bindImageToView(Context context, String url){
        Picasso.with(context).load(url).into(mImage);
    }
}
