package com.example.jon.eventmeets.EventCategoryBrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.jon.eventmeets.Model.Category;
import com.example.jon.eventmeets.R;

import java.util.List;

public class EventBrowseViewActivity extends AppCompatActivity implements EventBrowserContract.View {
    private EventBrowserContract.Presenter mPresenter;
    private RecyclerView mBrowserRecycler;
    private EventBrowserRecycler mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_browse_view);

        mPresenter = new EventBrowserPresenter(this);
    }

    @Override
    public void setBrowserAdapter(List<Category> list) {
        mAdapter = new EventBrowserRecycler(list);
        mBrowserRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mBrowserRecycler.setAdapter(mAdapter);
    }

    @Override
    public void openCategory(Category category) {
        // TODO: Open new activity based on category clicked
    }
}
