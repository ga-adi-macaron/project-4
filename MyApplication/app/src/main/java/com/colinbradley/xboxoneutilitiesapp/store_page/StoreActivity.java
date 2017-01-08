package com.colinbradley.xboxoneutilitiesapp.store_page;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

        mViewPager = (ViewPager)findViewById(R.id.store_viewpager);
        mViewPager.setOffscreenPageLimit(2);
        mToolbar = (Toolbar)findViewById(R.id.store_toolbar);
        mTablayout = (TabLayout)findViewById(R.id.store_tablayout);

        mVPadapter = new StoreViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mVPadapter);
        mTablayout.setupWithViewPager(mViewPager);
    }
}
