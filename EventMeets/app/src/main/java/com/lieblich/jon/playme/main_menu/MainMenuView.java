package com.lieblich.jon.playme.main_menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lieblich.jon.playme.R;
import com.lieblich.jon.playme.event_detail_components.VideoGameEventsActivity;

import java.util.Map;

public class MainMenuView extends AppCompatActivity implements MainMenuContract.View, View.OnClickListener{
    private MainMenuPresenter mPresenter;
    private Button mBrowseButton;
    private RecyclerView mMainEventsRecycler;
    private MainMenuRecyclerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mBrowseButton = (Button)findViewById(R.id.main_browse_btn);

        mMainEventsRecycler = (RecyclerView)findViewById(R.id.main_menu_recycler);

        mBrowseButton.setOnClickListener(this);

        mPresenter = new MainMenuPresenter(this);
        mPresenter.onNewEventsNeeded();
    }

    @Override
    public void setupRecyclerView(Map<String, String> list) {
        if(list != null && list.size() > 0) {
            mAdapter = new MainMenuRecyclerAdapter(list);
            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mMainEventsRecycler.setLayoutManager(manager);
            mMainEventsRecycler.setAdapter(mAdapter);
        } else {
            mMainEventsRecycler.setVisibility(View.GONE);
        }
    }

    @Override
    public void openEventDetail(String chatId) {

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
