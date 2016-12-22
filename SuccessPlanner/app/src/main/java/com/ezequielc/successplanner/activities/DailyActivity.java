package com.ezequielc.successplanner.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezequielc.successplanner.DatabaseHelper;
import com.ezequielc.successplanner.models.DailyData;
import com.ezequielc.successplanner.models.Goal;
import com.ezequielc.successplanner.recyclerviews.GoalRecyclerViewAdapter;
import com.ezequielc.successplanner.R;
import com.ezequielc.successplanner.models.Schedule;
import com.ezequielc.successplanner.recyclerviews.ScheduleRecyclerViewAdapter;
import com.ezequielc.successplanner.models.Affirmation;
import com.ezequielc.successplanner.recyclerviews.AffirmationRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class DailyActivity extends AppCompatActivity {
    TextView mCurrentDate;
    Button mAddGoals, mAddAffirmations, mAddSchedule;
    RecyclerView mGoalsRecyclerView, mAffirmationsRecyclerView, mScheduleRecyclerView;

    GoalRecyclerViewAdapter mGoalAdapter;
    AffirmationRecyclerViewAdapter mAffirmationAdapter;
    ScheduleRecyclerViewAdapter mScheduleAdapter;

    List<Goal> mGoalList;
    List<Affirmation> mAffirmationList;
    List<Schedule> mScheduleList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        // References to Views
        mCurrentDate = (TextView) findViewById(R.id.current_date);
        mAddGoals = (Button) findViewById(R.id.add_goals_button);
        mAddAffirmations = (Button) findViewById(R.id.add_affirmations_button);
        mAddSchedule = (Button) findViewById(R.id.add_schedule_button);
        mGoalsRecyclerView = (RecyclerView) findViewById(R.id.goals_recycler_view);
        mAffirmationsRecyclerView = (RecyclerView) findViewById(R.id.affirmations_recycler_view);
        mScheduleRecyclerView = (RecyclerView) findViewById(R.id.schedule_recycler_view);

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        String currentDate = getIntent().getStringExtra(MainActivity.DATE_FORMATTED);
        mCurrentDate.setText(currentDate);

        // RecyclerView for Goals
        mGoalList = databaseHelper.getGoalsForDate(currentDate);

        mGoalAdapter = new GoalRecyclerViewAdapter(mGoalList);
        LinearLayoutManager goalLinearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mGoalsRecyclerView.setLayoutManager(goalLinearLayoutManager);
        mGoalsRecyclerView.setAdapter(mGoalAdapter);

        // RecyclerView for Affirmations
        mAffirmationList = databaseHelper.getAffirmationsForDate(currentDate);
        mAffirmationAdapter = new AffirmationRecyclerViewAdapter(mAffirmationList);
        LinearLayoutManager affirmationLinearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAffirmationsRecyclerView.setLayoutManager(affirmationLinearLayoutManager);
        mAffirmationsRecyclerView.setAdapter(mAffirmationAdapter);

        // RecyclerView for Schedules
        mScheduleList = databaseHelper.getScheduleForDate(currentDate);
        mScheduleAdapter = new ScheduleRecyclerViewAdapter(mScheduleList);
        LinearLayoutManager ScheduleLinearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mScheduleRecyclerView.setLayoutManager(ScheduleLinearLayoutManager);
        mScheduleRecyclerView.setAdapter(mScheduleAdapter);

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

    public void alertDialog(int layout, final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(DailyActivity.this);
        LayoutInflater inflater = LayoutInflater.from(DailyActivity.this);
        View view = inflater.inflate(layout, null);
        builder.setView(view);

        final EditText editText = (EditText) view.findViewById(id);

        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (editText.getText().toString().trim().length() == 0) {
                    Toast.makeText(DailyActivity.this, "Please fill field!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String input = editText.getText().toString();
                String currentDate = getIntent().getStringExtra(MainActivity.DATE_FORMATTED);
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());

                switch (id) {
                    case R.id.goal_edit_text:
                        Goal goal = new Goal(currentDate, input);

                        databaseHelper.insertGoals(goal);
                        mGoalList.add(goal);
                        mGoalAdapter.notifyItemInserted(mGoalList.size() - 1);
                        break;

                    case R.id.affirmations_edit_text:
                        Affirmation affirmation = new Affirmation(currentDate, input);

                        databaseHelper.insertAffirmations(affirmation);
                        mAffirmationList.add(affirmation);
                        mAffirmationAdapter.notifyItemInserted(mAffirmationList.size() - 1);
                        break;

                    case R.id.schedule_edit_text:
                        Schedule schedule = new Schedule(currentDate, input);
                        databaseHelper.insertSchedule(schedule);
                        mScheduleList.add(schedule);
                        mScheduleAdapter.notifyItemInserted(mScheduleList.size() - 1);
                        break;

                    default:
                        break;
                }
            }
        })
                .setNegativeButton("Cancel", null);
        builder.create().show();
    }
}
