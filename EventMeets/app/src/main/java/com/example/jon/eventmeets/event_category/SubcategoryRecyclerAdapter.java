package com.example.jon.eventmeets.event_category;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jon.eventmeets.R;
import com.example.jon.eventmeets.model.EventParent;

/**
 * Created by Jon on 12/21/2016.
 */

class SubcategoryRecyclerAdapter extends RecyclerView.Adapter<SubcategoryRecyclerAdapter.SubcategoryViewHolder>{
    private String[] mSubcategoryList;

    SubcategoryRecyclerAdapter(EventParent parent) {
        mSubcategoryList = parent.getChildren();
    }

    @Override
    public SubcategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubcategoryViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subcategory_recycler_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(SubcategoryViewHolder holder, int position) {
        holder.mSubcategoryName.setText(mSubcategoryList[position]);
    }

    @Override
    public int getItemCount() {
        return mSubcategoryList.length;
    }

    class SubcategoryViewHolder extends RecyclerView.ViewHolder {
        TextView mSubcategoryName;
        SubcategoryViewHolder(View itemView) {
            super(itemView);

            mSubcategoryName = (TextView)itemView.findViewById(R.id.subcategory_name);
        }
    }
}
