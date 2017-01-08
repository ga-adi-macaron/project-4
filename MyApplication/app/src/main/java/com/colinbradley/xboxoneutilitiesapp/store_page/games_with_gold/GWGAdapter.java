package com.colinbradley.xboxoneutilitiesapp.store_page.games_with_gold;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colinbradley.xboxoneutilitiesapp.R;

import java.util.List;

/**
 * Created by colinbradley on 12/29/16.
 */

public class GWGAdapter extends RecyclerView.Adapter<GWGViewHolder> {
    public static final String TAG = "GWGAdapter";

    private List<GameWithGold> mGWGList;
    private OnItemSelectedListener mOnItemSelectedListener;
    private Context mContext;

    public GWGAdapter(List<GameWithGold> mGWGList, OnItemSelectedListener mOnItemSelectedListener, Context mContext) {
        this.mGWGList = mGWGList;
        this.mOnItemSelectedListener = mOnItemSelectedListener;
        this.mContext = mContext;
    }

    interface OnItemSelectedListener{
        void onItemSelected(String id);
    }

    @Override
    public GWGViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gwg_rv_item, parent, false);
        return new GWGViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GWGViewHolder holder, final int position) {
        holder.mTitle.setText(mGWGList.get(position).getmTitle());
        holder.mOriginalPrice.setText(mGWGList.get(position).getmOriginalPrice());
        holder.bindImageToView(mContext, mGWGList.get(position).getmURLforBoxArt());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemSelectedListener.onItemSelected(mGWGList.get(position).getmGameID());
                Log.d(TAG, "onClick: " + mGWGList.get(position).getmGameID());

            }
        });
    }

    @Override
    public int getItemCount() {
        return mGWGList.size();
    }
}
