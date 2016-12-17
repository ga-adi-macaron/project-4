package com.joelimyx.politicallocal.reps;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.reps.gson.opensecret.Attributes;
import com.joelimyx.politicallocal.reps.gson.opensecret.Legislator;
import com.joelimyx.politicallocal.reps.gson.probulica.Result;

import java.util.List;

/**
 * Created by Joe on 12/15/16.
 */

public class RepsAdapter extends RecyclerView.Adapter<RepsAdapter.RepsViewHolder> {
    private List<Legislator> mRepList;
    private OnRepsItemSelectedListener mListener;

    interface OnRepsItemSelectedListener{
        void OnRepsItemSelected(String id);
    }

    public RepsAdapter(List<Legislator> repList, OnRepsItemSelectedListener listener) {
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
        final Attributes current = mRepList.get(position).getAttributes();
//        holder.mRepsName.setText(current.getName());
//        holder.mStateParty.setText(current.getParty()+"-NY");
        holder.mRepsName.setText(current.getFirstlast());
        holder.mStateParty.setText(current.getParty()+"-NY");
        holder.mRepsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.OnRepsItemSelected(current.getCid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRepList.size();
    }

    public void swapData(List<Legislator> updateList){
        mRepList = updateList;
        notifyDataSetChanged();
    }

    class RepsViewHolder extends RecyclerView.ViewHolder{
        private TextView mRepsName, mStateParty;
        private LinearLayout mRepsItem;

        public RepsViewHolder(View itemView) {
            super(itemView);

            mRepsName = (TextView) itemView.findViewById(R.id.reps_name);
            mStateParty= (TextView) itemView.findViewById(R.id.reps_state_party);
            mRepsItem = (LinearLayout) itemView.findViewById(R.id.reps_item);
        }
    }
}
