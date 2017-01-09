package com.lieblich.jon.playme.event_detail_components;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lieblich.jon.playme.R;
import com.lieblich.jon.playme.model.GameResultObject;

import java.util.List;

public class VideoGameEventsActivity extends AppCompatActivity implements VideoGameSearchContract.View {
    private Presenter mPresenter;
    private List<GameResultObject> mGameList;
    private VideoGamesRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Button mGameSearch;
    private EditText mQueryText;
    private ProgressBar mBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_game_events);

        mRecyclerView = (RecyclerView)findViewById(R.id.game_search_results);
        mGameSearch = (Button)findViewById(R.id.search_games);
        mQueryText = (EditText)findViewById(R.id.video_game_search);
        mBar = (ProgressBar)findViewById(R.id.loading_search);

        mPresenter = new Presenter(this);

        TextView link = (TextView)findViewById(R.id.igdb);
        link.setMovementMethod(LinkMovementMethod.getInstance());

        mGameSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = mQueryText.getText().toString();
                if(query.length() < 1) {
                    mQueryText.setError("Search cannot be blank");
                } else {
                    if(mGameList != null) {
                        mGameList.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                    mBar.setVisibility(View.VISIBLE);
                    mPresenter.onSearchRequested(query);
                }
            }
        });
    }

    @Override
    public void displaySearchResults(List<GameResultObject> games) {
        mBar.setVisibility(View.GONE);
        mGameList = games;
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new VideoGamesRecyclerAdapter(mGameList);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        if(games.size() == 0) {
            Snackbar.make(findViewById(R.id.activity_video_game_events), "No results found", Snackbar.LENGTH_LONG).show();
        }
    }
}
