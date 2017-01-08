package com.colinbradley.xboxoneutilitiesapp.store_page.deals_with_gold;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colinbradley.xboxoneutilitiesapp.R;

import java.util.List;

/**
 * Created by colinbradley on 1/3/17.
 */

public class DWGAdapter extends RecyclerView.Adapter<DWGViewHolder> {
    public static final String TAG = "DWGAdapter";

    private List<DealWithGold> mDWGList;
    private OnItemSelectedListener mOnItemSelectedListener;
    private Context mContext;

    public DWGAdapter(List<DealWithGold> mDWGList, OnItemSelectedListener mOnItemSelectedListener, Context mContext) {
        this.mDWGList = mDWGList;
        this.mOnItemSelectedListener = mOnItemSelectedListener;
        this.mContext = mContext;
    }

    interface OnItemSelectedListener{
        void onItemSelected(String id, String oldp, String newp);
    }

    @Override
    public DWGViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dwg_rv_item, parent, false);
        return new DWGViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DWGViewHolder holder, final int position) {
        holder.mTitle.setText(mDWGList.get(position).getmTitle());
        holder.mOriginalPrice.setText(mDWGList.get(position).getmOriginalPrice());
        holder.mNewPrice.setText(mDWGList.get(position).getmNewPrice());
        holder.bindImageToView(mContext, mDWGList.get(position).getmURLforBoxArt());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemSelectedListener.onItemSelected(
                        mDWGList.get(position).getmGameID(),
                        mDWGList.get(position).getmOriginalPrice(),
                        mDWGList.get(position).getmNewPrice());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDWGList.size();
    }
}
