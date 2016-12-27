package com.ezequielc.successplanner.recyclerviews;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ezequielc.successplanner.DatabaseHelper;
import com.ezequielc.successplanner.R;
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
        .inflate(R.layout.goal_list_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final GoalViewHolder holder, int position) {
        holder.mGoal.setText(mGoalList.get(position).getGoal());

        holder.mGoal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(view.getContext());
                databaseHelper.deleteGoal(mGoalList.get(holder.getAdapterPosition()));
                mGoalList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                return true;
            }
        });

        holder.mEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                View dialogView = inflater.inflate(R.layout.dialog_add_goals, null);
                builder.setView(dialogView);

                final EditText editText = (EditText) dialogView.findViewById(R.id.goal_edit_text);
                editText.setText(mGoalList.get(holder.getAdapterPosition()).getGoal());

                builder.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (editText.getText().toString().trim().length() == 0) {
                            Toast.makeText(view.getContext(), "Please fill field!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String input = editText.getText().toString();
                        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(view.getContext());

                        mGoalList.get(holder.getAdapterPosition()).setGoal(input);
                        databaseHelper.updateGoal(mGoalList.get(holder.getAdapterPosition()));
                        notifyItemChanged(holder.getAdapterPosition());
                    }
                })
                        .setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGoalList.size();
    }

    class GoalViewHolder extends RecyclerView.ViewHolder {
        TextView mGoal;
        ImageView mEditImage;

        public GoalViewHolder(View itemView) {
            super(itemView);

            mGoal = (TextView) itemView.findViewById(R.id.goal_item);
            mEditImage = (ImageView) itemView.findViewById(R.id.edit_icon);
        }
    }
}
