package com.colinbradley.xboxoneutilitiesapp.store_page;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RelativeLayout;

import com.colinbradley.xboxoneutilitiesapp.R;

public class StoreActivity extends AppCompatActivity {
    public static final String TAG = "StoreActivity";
    
    Toolbar mToolbar;
    ViewPager mViewPager;
    TabLayout mTablayout;
    StoreViewPagerAdapter mVPadapter;
    RelativeLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        mLayout = (RelativeLayout)findViewById(R.id.activity_store);

        Log.d(TAG, "onCreate: got to page");

        mViewPager = (ViewPager)findViewById(R.id.store_viewpager);
        Log.d(TAG, "onCreate: instanciate view pager");
        
        mToolbar = (Toolbar)findViewById(R.id.store_toolbar);
        Log.d(TAG, "onCreate: instanciate toolbar");
        mTablayout = (TabLayout)findViewById(R.id.store_tablayout);
        Log.d(TAG, "onCreate: instanciate tab layout");

        mVPadapter = new StoreViewPagerAdapter(getSupportFragmentManager());
        Log.d(TAG, "onCreate: get support fragment manager");
        mViewPager.setAdapter(mVPadapter);
        Log.d(TAG, "onCreate: set adapter");
        mTablayout.setupWithViewPager(mViewPager);
        Log.d(TAG, "onCreate: set up with view pager");


    }
}
