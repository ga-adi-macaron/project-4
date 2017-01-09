package com.joelimyx.politicallocal.reps.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.reps.gson.opensecret.Attributes_;
import com.joelimyx.politicallocal.reps.gson.opensecret.Contributor;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Joe on 12/19/16.
 */

public class ContributorAdapter extends RecyclerView.Adapter<ContributorAdapter.ContributorViewHolder> {

    private List<Contributor> mContributorList;

    public ContributorAdapter(List<Contributor> contributorList) {
        mContributorList = contributorList;
    }

    @Override
    public ContributorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContributorViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_contributor,parent,false));
    }

    @Override
    public void onBindViewHolder(ContributorViewHolder holder, int position) {
        Attributes_ current = mContributorList.get(position).getAttributes();
        holder.mContributorName.setText(current.getOrgName());

        //Take in total amount in string, convert it to Integer for easier formatting
        int total = Integer.parseInt(current.getTotal());
        String fill = NumberFormat.getNumberInstance(Locale.US).format(total);
        holder.mContributorAmount.setText("$"+fill);
    }

    @Override
    public int getItemCount() {
        return mContributorList.size();
    }

    class ContributorViewHolder extends RecyclerView.ViewHolder {
        private TextView mContributorName, mContributorAmount;

        public ContributorViewHolder(View itemView) {
            super(itemView);
            mContributorName = (TextView) itemView.findViewById(R.id.contributor_name);
            mContributorAmount = (TextView) itemView.findViewById(R.id.contributor_amount);
        }
    }
}
