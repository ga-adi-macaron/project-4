package com.joelimyx.politicallocal.bills.detail.sponsors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.bills.detail.sponsors.detail.Sponsor;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Joe on 12/26/16.
 */

public class SponsorsAdapter extends RecyclerView.Adapter<SponsorsAdapter.SponsorViewHolder> {
    // TODO: 12/26/16 use new custom list
    private List<Sponsor> mSponsors;
    private Context mContext;
    private static final String TAG = "SponsorsAdapter";

    public SponsorsAdapter(List<Sponsor> sponsors, Context context) {
        mSponsors = sponsors;
        mContext = context;
    }

    @Override
    public SponsorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SponsorViewHolder(
                LayoutInflater.from(parent.getContext())
                        // TODO: 12/26/16 Create and Use new layout
                        .inflate(R.layout.list_item_reps,parent,false));
    }

    @Override
    public void onBindViewHolder(SponsorViewHolder holder, int position) {
        //TODO:Change layout for main sponsor
        if (position==0){

        }
        final Sponsor current = mSponsors.get(position);
        Picasso.with(mContext)
                .load("https://theunitedstates.io/images/congress/original/"+current.getBioId()+".jpg")
                .error(R.drawable.ic_reps)
                .fit()
                .into(holder.mSponsorPortrait);

        holder.mSponsorName.setText(current.getName()+" "+current.getPartyState());

        //Setting district/rank to either senate or house position
        String districtRank;
        if (current.getChamber().equalsIgnoreCase("Senate")){
            districtRank="Senator Class "+current.getDistrictRank();
        }else{
            //At Large district or particular district
            if (current.getDistrictRank()==0){
                districtRank=current.getState()+" At-Large District";
            }else{
                districtRank=current.getState()+" District "+current.getDistrictRank();
            }
        }
        holder.mSponsorDistrictRank.setText(districtRank);
    }

    /**
     *
     * @param sponsor sponsor variable to be added to list and update recyclerview
     * @param isMajor if true add to first item in the list
     */
    public void addSponsorToList(Sponsor sponsor,boolean isMajor){
        if (isMajor) {
            mSponsors.add(0,sponsor);
            notifyItemInserted(0);
        }else {
            mSponsors.add(sponsor);
            notifyItemInserted(mSponsors.size() - 1);
        }
    }
    @Override
    public int getItemCount() {
        return mSponsors.size();
    }

    class SponsorViewHolder extends RecyclerView.ViewHolder {
        private TextView mSponsorName, mSponsorDistrictRank;
        private RelativeLayout mSponsorItem;
        private CircleImageView mSponsorPortrait;

        public SponsorViewHolder(View itemView) {
            super(itemView);
            mSponsorName = (TextView) itemView.findViewById(R.id.reps_name);
            mSponsorDistrictRank = (TextView) itemView.findViewById(R.id.reps_district_rank);
            mSponsorItem = (RelativeLayout) itemView.findViewById(R.id.reps_item);
            mSponsorPortrait = (CircleImageView) itemView.findViewById(R.id.reps_portrait);
        }
    }
}
