package com.crocusgames.destinyinventorymanager.CharInventoryObjects;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crocusgames.destinyinventorymanager.R;

/**
 * Created by Serkan on 15/12/16.
 */

public class GridLayoutViewHolder extends RecyclerView.ViewHolder {
    public ImageView mItemIcon;
    public TextView mItemQuantity;

    public GridLayoutViewHolder(View itemView) {
        super(itemView);
        mItemIcon = (ImageView) itemView.findViewById(R.id.item_icon);
        mItemQuantity = (TextView) itemView.findViewById(R.id.item_quantity);
    }
}
