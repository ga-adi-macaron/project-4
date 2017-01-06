package com.example.jon.eventmeets.main_menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jon.eventmeets.R;
import com.example.jon.eventmeets.event_detail_components.VideoGameEventsActivity;
import com.example.jon.eventmeets.model.GameResultObject;

import java.util.List;

public class MainMenuView extends AppCompatActivity implements MainMenuContract.View, View.OnClickListener{
    private MainMenuPresenter mPresenter;
    private Button mBrowseButton;
    private RecyclerView mMainEventsRecycler;
    private MainMenuRecyclerAdapter mAdapter;
    private ImageView mEventsPlaceholder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mBrowseButton = (Button)findViewById(R.id.main_browse_btn);

        mMainEventsRecycler = (RecyclerView)findViewById(R.id.main_menu_recycler);

        mEventsPlaceholder = (ImageView)findViewById(R.id.empty_events_placeholder);

        mBrowseButton.setOnClickListener(this);

        mPresenter = new MainMenuPresenter(this);
    }

    @Override
    public void setupRecyclerView(List<GameResultObject> list) {
        if(list != null && list.size() > 0) {
            mAdapter = new MainMenuRecyclerAdapter(list);
            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mMainEventsRecycler.setLayoutManager(manager);
            mMainEventsRecycler.setAdapter(mAdapter);
        } else {
            mMainEventsRecycler.setVisibility(View.GONE);
            mEventsPlaceholder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void openEventDetail(GameResultObject event) {

    }

    @Override
    public void openBrowseActivity() {
        Intent intent = new Intent(this, VideoGameEventsActivity.class);
        startActivity(intent);
    }

    @Override
    public void openSettingsActivity() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.main_browse_btn:
                mPresenter.onBrowsePressed();
                break;
            default:
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }
}
