package com.joelimyx.politicallocal.bills.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.bills.detail.gson.propublica.Action;

import java.util.List;

/**
 * Created by Joe on 12/20/16.
 */

public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.UpdateViewHolder> {

    List<Action> mActionList;

    public UpdateAdapter(List<Action> actionList) {
        mActionList = actionList;
    }

    @Override
    public UpdateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UpdateViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_update,parent,false));
    }

    @Override
    public void onBindViewHolder(UpdateViewHolder holder, int position) {
        Action current = mActionList.get(position);
        holder.mUpdateAction.setText(current.getDescription());
        String date = current.getDatetime().substring(0,10);
        holder.mUpdateTime.setText(date);
    }

    @Override
    public int getItemCount() {
        return mActionList.size();
    }

    class UpdateViewHolder extends RecyclerView.ViewHolder{
        private TextView mUpdateAction, mUpdateTime;

        public UpdateViewHolder(View itemView) {
            super(itemView);
            mUpdateAction = (TextView) itemView.findViewById(R.id.update_action);
            mUpdateTime= (TextView) itemView.findViewById(R.id.update_time);
        }
    }
}
