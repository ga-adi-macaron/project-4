package com.joelimyx.politicallocal.bills;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.joelimyx.politicallocal.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Joe on 1/3/17.
 */

class VotersAdapter extends RecyclerView.Adapter<VotersAdapter.VotersViewHolder> {
    private List<Voter> mVoterList;
    private Context mContext;
    private static final String TAG = "VotersAdapter";

    VotersAdapter(List<Voter> voterList, Context context) {
        mVoterList = voterList;
        mContext = context;
    }

    @Override
    public VotersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VotersViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_voter,parent,false));
    }

    @Override
    public void onBindViewHolder(VotersViewHolder holder, int position) {
        Picasso.with(mContext)
                .load("https://theunitedstates.io/images/congress/original/"+mVoterList.get(position).getBioID()+".jpg")
                .placeholder(R.drawable.ic_reps)
                .into(holder.mPortraitImage);
        if (mVoterList.get(position).getDecision().equals("Yea")){
            holder.mDecision.setImageResource(R.drawable.ic_yea);
        }else if (mVoterList.get(position).getDecision().equals("Nay")){
            holder.mDecision.setImageResource(R.drawable.ic_nay);

        }
    }

    @Override
    public int getItemCount() {
        return mVoterList.size();
    }

    void addVote(Voter voter){
        mVoterList.add(voter);
        notifyItemInserted(mVoterList.size()-1);
    }

    class VotersViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mPortraitImage;
        private ImageView mDecision;

        VotersViewHolder(View itemView) {
            super(itemView);
            mPortraitImage = (CircleImageView) itemView.findViewById(R.id.voter_portrait_image);
            mDecision = (ImageView) itemView.findViewById(R.id.voter_decision);
        }
    }
}
