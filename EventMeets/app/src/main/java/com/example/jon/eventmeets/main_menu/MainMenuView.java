package com.example.jon.eventmeets.main_menu;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jon.eventmeets.Model.Category;
import com.example.jon.eventmeets.Model.EventParent;
import com.example.jon.eventmeets.R;

import java.util.List;

public class MainMenuView extends AppCompatActivity implements MainMenuContract.View{
    private MainMenuPresenter mPresenter;
    private Button mBrowseButton;
    private Button mLoginMainMenuButton;
    private RecyclerView mMainEventsRecycler;
    private TextView mEventHeader;
    private MainMenuRecyclerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main_menu);

        mBrowseButton = (Button)findViewById(R.id.fab);
        mLoginMainMenuButton = (Button)findViewById(R.id.main_menu_login_btn);
        mEventHeader = (TextView)findViewById(R.id.main_event_header);

        Intent intent = getIntent();
        if(intent.getBooleanExtra("logged in", false))
            mLoginMainMenuButton.setVisibility(View.GONE);

        mPresenter = new MainMenuPresenter(this);
    }

    @Override
    public void setupRecyclerView(List<EventParent> list) {
        mAdapter = new MainMenuRecyclerAdapter(list);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMainEventsRecycler.setLayoutManager(manager);
        mMainEventsRecycler.setAdapter(mAdapter);
    }

    @Override
    public void hideLoginButton() {
        mLoginMainMenuButton.setVisibility(View.GONE);
    }

    @Override
    public void openEventDetail(EventParent event) {

    }

    @Override
    public void openBrowseActivity(Category category) {

    }

    @Override
    public void displayLoginButton() {
        mLoginMainMenuButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void openSettingsActivity() {

    }
}
