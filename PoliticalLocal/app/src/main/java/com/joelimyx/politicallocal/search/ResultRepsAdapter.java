package com.joelimyx.politicallocal.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.reps.MyRep;
import com.joelimyx.politicallocal.reps.gson.congress.Result;
import com.squareup.picasso.Picasso;

import java.util.DoubleSummaryStatistics;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Joe on 1/5/17.
 */

public class ResultRepsAdapter extends RecyclerView.Adapter<ResultRepsAdapter.ResultRepsViewHolder> {
    List<Result> mMyReps;
    private Context mContext;
    private OnRepsResultSelectedListener mListener;

    interface OnRepsResultSelectedListener{
        void OnRepsResultSelected(String id);
    }

    public ResultRepsAdapter(List<Result> myReps, Context context, OnRepsResultSelectedListener listener) {
        mMyReps = myReps;
        mContext = context;
        mListener = listener;
    }

    @Override
    public ResultRepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ResultRepsViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_reps,parent,false));
    }

    @Override
    public void onBindViewHolder(ResultRepsViewHolder holder, int position) {
        final Result current = mMyReps.get(position);
        Picasso.with(mContext)
                .load("https://theunitedstates.io/images/congress/original/"+current.getBioguideId()+".jpg")
                .error(R.drawable.ic_reps)
                .fit()
                .into(holder.mRepsPortrait);

        String repsNameParty = current.getFirstName()+" ("+current.getParty()+"-"+current.getState()+")";
        holder.mRepsName.setText(repsNameParty);

        //Setting district/rank to either senate or house position
        String districtRank;
        if (current.getChamber().equalsIgnoreCase("Senate")){
            districtRank="Senator Class "+current.getSenateClass();
        }else{
            //At Large district or particular district
            if ( ((Double)current.getDistrict()).intValue()==0){
                districtRank=current.getState()+" At-Large District";
            }else{
                districtRank=current.getState()+" District "+((Double)current.getDistrict()).intValue();
            }
        }
        holder.mRepsDistrictRank.setText(districtRank);
        holder.mRepsItem.setOnClickListener(v -> mListener.OnRepsResultSelected(current.getBioguideId()));
    }

    @Override
    public int getItemCount() {
        return mMyReps.size();
    }

    class ResultRepsViewHolder extends RecyclerView.ViewHolder {
        private TextView mRepsName, mRepsDistrictRank;
        private RelativeLayout mRepsItem;
        private CircleImageView mRepsPortrait;

        public ResultRepsViewHolder(View itemView) {
            super(itemView);
            mRepsName = (TextView) itemView.findViewById(R.id.reps_name);
            mRepsDistrictRank = (TextView) itemView.findViewById(R.id.reps_district_rank);
            mRepsItem = (RelativeLayout) itemView.findViewById(R.id.reps_item);
            mRepsPortrait = (CircleImageView) itemView.findViewById(R.id.reps_portrait);
        }
    }
}
