package com.example.jon.eventmeets.event_category;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.jon.eventmeets.R;

public class CategoryWithSubsActivity extends AppCompatActivity implements EventCategoriesContract.View, View.OnClickListener{
    private TextView mCategoryName;
    private EventCategoriesContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_with_subs);

        mPresenter = new Presenter(this);

        mCategoryName = (TextView)findViewById(R.id.category_name);

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
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {

        }
    }
}
