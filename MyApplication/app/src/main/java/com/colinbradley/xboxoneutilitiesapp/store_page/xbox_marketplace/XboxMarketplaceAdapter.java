package com.colinbradley.xboxoneutilitiesapp.store_page.xbox_marketplace;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colinbradley.xboxoneutilitiesapp.R;

import java.util.List;

/**
 * Created by colinbradley on 12/29/16.
 */

public class XboxMarketplaceAdapter extends RecyclerView.Adapter<XboxMarketplaceViewHolder> {

    private List<Game> mGameList;
    private Context mContext;
    private OnItemSelectedListener mOnItemSelectedListener;

    public XboxMarketplaceAdapter(List<Game> mGameList, Context mContext, OnItemSelectedListener mOnItemSelectedListener) {
        this.mGameList = mGameList;
        this.mContext = mContext;
        this.mOnItemSelectedListener = mOnItemSelectedListener;
    }

    interface OnItemSelectedListener{
        void onItemSelected(String id);
    }

    @Override
    public XboxMarketplaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.marketplace_rv_item, parent, false);
        return new XboxMarketplaceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(XboxMarketplaceViewHolder holder, final int position) {
        holder.mTitle.setText(mGameList.get(position).getmTitle());
        holder.mDevName.setText(mGameList.get(position).getmDevName());
        holder.bindImage(mGameList.get(position).getmMainImgURL(), mContext);

        holder.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemSelectedListener.onItemSelected(mGameList.get(position).getmGameID());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mGameList.size();
    }
}
