package com.ezequielc.successplanner.recyclerviews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezequielc.successplanner.models.Affirmation;

import java.util.List;

/**
 * Created by student on 12/20/16.
 */

public class AffirmationRecyclerViewAdapter extends RecyclerView.Adapter<AffirmationRecyclerViewAdapter.AffirmationViewHolder> {
    private List<Affirmation> mAffirmationList;

    public AffirmationRecyclerViewAdapter(List<Affirmation> mAffirmationList) {
        this.mAffirmationList = mAffirmationList;
    }

    @Override
    public AffirmationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AffirmationViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(android.R.layout.simple_list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(AffirmationViewHolder holder, int position) {
        holder.mAffirmation.setText(mAffirmationList.get(position).getAffirmation());
    }

    @Override
    public int getItemCount() {
        return mAffirmationList.size();
    }

    class AffirmationViewHolder extends RecyclerView.ViewHolder {
        TextView mAffirmation;

        public AffirmationViewHolder(View itemView) {
            super(itemView);

            mAffirmation = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
