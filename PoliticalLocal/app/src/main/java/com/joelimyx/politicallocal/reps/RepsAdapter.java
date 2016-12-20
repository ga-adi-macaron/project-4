package com.joelimyx.politicallocal.reps;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joelimyx.politicallocal.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joe on 12/15/16.
 */

public class RepsAdapter extends RecyclerView.Adapter<RepsAdapter.RepsViewHolder> {
    private List<MyReps> mRepList;
    private OnRepsItemSelectedListener mListener;
    private String mState;
    private Context mContext;

    interface OnRepsItemSelectedListener{
        void OnRepsItemSelected(String id);
    }

    public RepsAdapter(List<MyReps> repList, String state, OnRepsItemSelectedListener listener, Context context) {
        mRepList = repList;
        mListener = listener;
        mState = state;
        mContext = context;
    }

    @Override
    public RepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RepsViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_reps,parent,false));
    }

    @Override
    public void onBindViewHolder(RepsViewHolder holder, int position) {
        final MyReps current = mRepList.get(position);
        Picasso.with(mContext)
                .load(mContext.getFileStreamPath(current.getFileName()))
                .fit()
                .into(holder.mRepsPortrait);

        String repsNameParty = current.getName()+" ("+current.getParty()+"-"+mState+")";
        holder.mRepsName.setText(repsNameParty);

        //Setting district/rank to either senate or house position
        String districtRank;
        if (current.getChamber().equalsIgnoreCase("Senate")){
            districtRank="Senator Class "+current.getDistrictClass();
        }else{
            if (current.getDistrictClass()==0){
                districtRank=mState+" At-Large District";
            }else{
                districtRank=mState+" District "+current.getDistrictClass();
            }
        }
        holder.mRepsDistrictRank.setText(districtRank);
        holder.mRepsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.OnRepsItemSelected(current.getBioId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRepList.size();
    }

    class RepsViewHolder extends RecyclerView.ViewHolder{
        private TextView mRepsName, mRepsDistrictRank;
        private RelativeLayout mRepsItem;
        private ImageView mRepsPortrait;

        public RepsViewHolder(View itemView) {
            super(itemView);

            mRepsName = (TextView) itemView.findViewById(R.id.reps_name);
            mRepsDistrictRank = (TextView) itemView.findViewById(R.id.reps_district_rank);
            mRepsItem = (RelativeLayout) itemView.findViewById(R.id.reps_item);
            mRepsPortrait = (ImageView) itemView.findViewById(R.id.reps_portrait);
        }
    }
}
