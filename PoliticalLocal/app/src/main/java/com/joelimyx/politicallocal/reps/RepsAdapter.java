package com.joelimyx.politicallocal.reps;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.news.gson.Value;
import com.joelimyx.politicallocal.reps.gson.Result;

import java.util.List;

/**
 * Created by Joe on 12/15/16.
 */

public class RepsAdapter extends RecyclerView.Adapter<RepsAdapter.RepsViewHolder> {
    private List<Result> mRepList;
    private OnRepsItemSelectedListener mListener;

    interface OnRepsItemSelectedListener{
        void OnRepsItemSelected(String id);
    }

    public RepsAdapter(List<Result> repList, OnRepsItemSelectedListener listener) {
        mRepList = repList;
        mListener = listener;
    }

    @Override
    public RepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RepsViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_reps,parent,false));
    }

    @Override
    public void onBindViewHolder(RepsViewHolder holder, int position) {
        Result current = mRepList.get(position);
        holder.mRepsName.setText(current.getName());
        holder.mStateParty.setText(current.getParty()+"-NY");
    }

    @Override
    public int getItemCount() {
        return mRepList.size();
    }

    public void swapData(List<Result> updateList){
        mRepList = updateList;
        notifyDataSetChanged();
    }

    class RepsViewHolder extends RecyclerView.ViewHolder{
        private TextView mRepsName, mStateParty;

        public RepsViewHolder(View itemView) {
            super(itemView);
            mRepsName = (TextView) itemView.findViewById(R.id.reps_name);
            mStateParty= (TextView) itemView.findViewById(R.id.reps_state_party);
        }
    }
}
