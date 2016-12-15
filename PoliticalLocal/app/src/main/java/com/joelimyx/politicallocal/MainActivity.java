package com.joelimyx.politicallocal;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.joelimyx.politicallocal.news.NewsFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private BottomNavigationView mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomBar = (BottomNavigationView) findViewById(R.id.bottom_bar);

        mBottomBar.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, NewsFragment.newInstance())
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.reps:
                break;

            case R.id.news:

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, NewsFragment.newInstance())
                        .commit();
                break;

            case R.id.bills:
                break;
        }
        return true;
    }
}
