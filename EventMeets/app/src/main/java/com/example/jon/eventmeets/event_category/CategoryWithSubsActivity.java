package com.example.jon.eventmeets.event_category;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jon.eventmeets.R;

public class CategoryWithSubsActivity extends AppCompatActivity {
    private TextView mCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_with_subs);

        mCategoryName = (TextView)findViewById(R.id.category_name);

        Intent intent = getIntent();
        String name = intent.getStringExtra("category");

        mCategoryName.setText(name);
    }
}
