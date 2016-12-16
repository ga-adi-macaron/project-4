package com.example.jon.eventmeets.main_menu;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.example.jon.eventmeets.Model.Category;
import com.example.jon.eventmeets.Model.EventParent;
import com.example.jon.eventmeets.R;

import java.util.List;

/**
 * Created by Jon on 12/16/2016.
 */

public class MainMenuView extends AppCompatActivity implements MainMenuContract.View{
    private MainMenuPresenter mPresenter;
    private Button mBrowseButton;
    private Button mLoginMainMenuButton;
    private RecyclerView mMainEventsRecycler;
    private TextView mEventHeader;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main_menu);

        mPresenter = new MainMenuPresenter(this);
    }

    @Override
    public void setupRecyclerView(List<EventParent> list, LinearLayoutManager manager, RecyclerView.Adapter adapter) {

    }

    @Override
    public void hideLoginButton() {

    }

    @Override
    public void openEventDetail(EventParent event) {

    }

    @Override
    public void openBrowseActivity(Category category) {

    }

    @Override
    public void displayLoginButton() {

    }

    @Override
    public void openSettingsActivity() {

    }
}
