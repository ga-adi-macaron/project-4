package com.joelimyx.politicallocal;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.joelimyx.politicallocal.news.NewsFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private BottomNavigationView mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomBar = (BottomNavigationView) findViewById(R.id.bottom_bar);

        mBottomBar.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
        case R.id.news:
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, NewsFragment.newInstance())
                    .commit();
            item.setChecked(true);
            break;

        case R.id.reps:
            Toast.makeText(this, "reps", Toast.LENGTH_SHORT).show();
            break;

        case R.id.bills:
            Toast.makeText(this, "bills", Toast.LENGTH_SHORT).show();
            break;
        }
        return true;
    }
}
