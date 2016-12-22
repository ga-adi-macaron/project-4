package com.example.jon.eventmeets.event_detail_components;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.jon.eventmeets.R;
import com.example.jon.eventmeets.model.game_models.VideoGamingEvent;

import java.util.List;

public class VideoGameEventsActivity extends AppCompatActivity implements VideoGameSearchContract.View {
    private Presenter mPresenter;
    private List<VideoGamingEvent> mGameList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_game_events);

        mPresenter = new Presenter(this);
    }

    @Override
    public void displaySearchResults(List<VideoGamingEvent> games) {

    }
}
