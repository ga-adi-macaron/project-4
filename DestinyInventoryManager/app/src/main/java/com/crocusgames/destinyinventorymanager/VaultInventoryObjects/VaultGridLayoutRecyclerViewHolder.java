package com.crocusgames.destinyinventorymanager.VaultInventoryObjects;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crocusgames.destinyinventorymanager.R;

/**
 * Created by Serkan on 23/12/16.
 */

public class VaultGridLayoutRecyclerViewHolder extends RecyclerView.ViewHolder {
    public ImageView mItemIcon;
    public TextView mItemQuantity;
    public TextView mItemLightLevel;

    public VaultGridLayoutRecyclerViewHolder(View itemView) {
        super(itemView);
        mItemIcon = (ImageView) itemView.findViewById(R.id.item_icon);
        mItemQuantity = (TextView) itemView.findViewById(R.id.item_quantity);
        mItemLightLevel = (TextView) itemView.findViewById(R.id.item_lightlevel);

    }
}
