package com.ezequielc.successplanner.recyclerviews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezequielc.successplanner.models.Goal;

import java.util.List;

/**
 * Created by student on 12/20/16.
 */

public class GoalRecyclerViewAdapter extends RecyclerView.Adapter<GoalRecyclerViewAdapter.GoalViewHolder> {
    private List<Goal> mGoalList;

    public GoalRecyclerViewAdapter(List<Goal> mGoalList) {
        this.mGoalList = mGoalList;
    }

    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoalViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(android.R.layout.simple_list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(GoalViewHolder holder, int position) {
        holder.mGoal.setText(mGoalList.get(position).getGoal());
    }

    @Override
    public int getItemCount() {
        return mGoalList.size();
    }

    class GoalViewHolder extends RecyclerView.ViewHolder {
        TextView mGoal;

        public GoalViewHolder(View itemView) {
            super(itemView);

            mGoal = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
