package com.example.jon.eventmeets.EventCategoryBrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jon.eventmeets.EventCategory.CategoryWithSubsActivity;
import com.example.jon.eventmeets.Model.Category;
import com.example.jon.eventmeets.R;

import java.util.List;

public class EventBrowseViewActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mConversation, mDrink, mGame, mNature, mTaste, mTheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_browse_view);

        mConversation = (TextView)findViewById(R.id.category_conversation);
        mDrink = (TextView)findViewById(R.id.category_drink);
        mGame = (TextView)findViewById(R.id.category_game);
        mNature = (TextView)findViewById(R.id.category_nature);
        mTaste = (TextView)findViewById(R.id.category_taste);
        mTheater = (TextView)findViewById(R.id.category_theater);

        mConversation.setOnClickListener(this);
        mDrink.setOnClickListener(this);
        mGame.setOnClickListener(this);
        mNature.setOnClickListener(this);
        mTaste.setOnClickListener(this);
        mTheater.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String category;
        switch(id) {
            case R.id.category_conversation:
                category = "conversation";
                break;
            case R.id.category_drink:
                category = "drink";
                break;
            case R.id.category_game:
                category = "game";
                break;
            case R.id.category_nature:
                category = "nature";
                break;
            case R.id.category_taste:
                category = "taste";
                break;
            case R.id.category_theater:
                category = "theater";
                break;
            default:
                category = "error";
        }
        Intent intent = new Intent(this, CategoryWithSubsActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}
