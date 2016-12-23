package com.ezequielc.successplanner.recyclerviews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezequielc.successplanner.DatabaseHelper;
import com.ezequielc.successplanner.models.Schedule;

import java.util.List;

/**
 * Created by student on 12/20/16.
 */

public class ScheduleRecyclerViewAdapter extends RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ScheduleViewHolder> {
    private List<Schedule> mScheduleList;

    public ScheduleRecyclerViewAdapter(List<Schedule> mScheduleList) {
        this.mScheduleList = mScheduleList;
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(android.R.layout.simple_list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(final ScheduleViewHolder holder, int position) {
        holder.mSchedule.setText(mScheduleList.get(position).getSchedule());

        holder.mSchedule.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(view.getContext());
                databaseHelper.deleteSchedule(mScheduleList.get(holder.getAdapterPosition()));
                mScheduleList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mScheduleList.size();
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView mSchedule;

        public ScheduleViewHolder(View itemView) {
            super(itemView);

            mSchedule = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
