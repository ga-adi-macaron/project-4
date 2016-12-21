package com.ezequielc.successplanner;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DailyActivity extends AppCompatActivity {
    TextView mCurrentDate;
    Button mAddGoals, mAddAffirmations, mAddSchedule;
    RecyclerView mGoalsRecyclerView, mAffirmationsRecyclerView, mScheduleRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        mCurrentDate = (TextView) findViewById(R.id.current_date);
        mAddGoals = (Button) findViewById(R.id.add_goals_button);
        mAddAffirmations = (Button) findViewById(R.id.add_affirmations_button);
        mAddSchedule = (Button) findViewById(R.id.add_schedule_button);
        mGoalsRecyclerView = (RecyclerView) findViewById(R.id.goals_recycler_view);
        mAffirmationsRecyclerView = (RecyclerView) findViewById(R.id.affirmations_recycler_view);
        mScheduleRecyclerView = (RecyclerView) findViewById(R.id.schedule_recycler_view);

        String currentDate = getIntent().getStringExtra(MainActivity.DATE_FORMATTED);
        mCurrentDate.setText(currentDate);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.add_goals_button:
                        alertDialog(R.layout.dialog_add_goals, R.id.goal_edit_text);
                        break;
                    case R.id.add_affirmations_button:
                        alertDialog(R.layout.dialog_add_affirmations, R.id.affirmations_edit_text);
                        break;
                    case R.id.add_schedule_button:
                        alertDialog(R.layout.dialog_add_schedule, R.id.schedule_edit_text);
                        break;
                }
            }
        };

        mAddGoals.setOnClickListener(onClickListener);
        mAddAffirmations.setOnClickListener(onClickListener);
        mAddSchedule.setOnClickListener(onClickListener);
    }

    public void alertDialog(int layout, int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(DailyActivity.this);
        LayoutInflater inflater = LayoutInflater.from(DailyActivity.this);
        View view = inflater.inflate(layout, null);
        builder.setView(view);

        final EditText editText = (EditText) view.findViewById(id);

        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (editText.length() == 0) {
                    editText.setError("Please fill field!");
                }
                String input = editText.getText().toString();
                // TODO: Remove code below and add RecyclerView
                mCurrentDate.setText(input);
            }
        })
                .setNegativeButton("Cancel", null);
        builder.create().show();
    }
}
