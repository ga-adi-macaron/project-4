package com.joelimyx.politicallocal;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.joelimyx.politicallocal.news.News;
import com.joelimyx.politicallocal.news.NewsFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity implements OnTabSelectListener{
    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomBar = (BottomBar) findViewById(R.id.bottom_bar);
        mBottomBar.setOnTabSelectListener(this);
    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId){
            case R.id.news:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, NewsFragment.newInstance())
                        .commit();
                break;

            case R.id.reps:
                break;

            case R.id.bills:
                break;

        }
    }
}
