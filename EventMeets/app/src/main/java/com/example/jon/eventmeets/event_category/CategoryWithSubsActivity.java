package com.example.jon.eventmeets.event_category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jon.eventmeets.R;
import com.example.jon.eventmeets.model.EventParent;

public class CategoryWithSubsActivity extends AppCompatActivity implements EventCategoriesContract.View, View.OnClickListener{
    private TextView mCategoryName;
    private EventCategoriesContract.Presenter mPresenter;
    private SubcategoryRecyclerAdapter mSubsAdapter;
    private RecyclerView mSubcategoryRecycler, mEventInCategoryRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_with_subs);

        mPresenter = new Presenter(this);

        mCategoryName = (TextView)findViewById(R.id.subcategory_name);

        Intent intent = getIntent();
        String name = intent.getStringExtra("category");

        mPresenter.onChildrenTypeSpecified(name);

        mCategoryName.setText(name);
    }

    @Override
    public void displayChildren() {

    }

    @Override
    public void displaySubCategory(String subcategory) {

    }

    @Override
    public void setRecyclerView(EventParent category) {
        mSubsAdapter = new SubcategoryRecyclerAdapter(category);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mSubcategoryRecycler.setLayoutManager(manager);
        mSubcategoryRecycler.setAdapter(mSubsAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {

        }
    }
}
