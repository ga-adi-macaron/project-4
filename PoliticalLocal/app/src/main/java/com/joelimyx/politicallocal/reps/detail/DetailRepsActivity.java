package com.joelimyx.politicallocal.reps.detail;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.reps.RepsFragment;
import com.joelimyx.politicallocal.reps.detail.gson.Attributes;
import com.joelimyx.politicallocal.reps.detail.gson.DetailInfo;
import com.joelimyx.politicallocal.reps.gson.opensecret.ListOfLegislator;
import com.joelimyx.politicallocal.reps.service.OpenSecretService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailRepsActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private static final String TAG = "DetailRepsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reps);

        /*---------------------------------------------------------------------------------
        // Basic info
        ---------------------------------------------------------------------------------*/
        final TextView namePartyText = (TextView) findViewById(R.id.detail_reps_name_party);
        final TextView phoneText = (TextView) findViewById(R.id.detail_reps_phone);
        final TextView websiteText= (TextView) findViewById(R.id.detail_reps_website);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RepsFragment.open_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<DetailInfo> call = retrofit.create(OpenSecretService.class).getDetail(getIntent().getStringExtra("id"));
        call.enqueue(new Callback<DetailInfo>() {
            @Override
            public void onResponse(Call<DetailInfo> call, Response<DetailInfo> response) {
                Attributes temp = response.body().getResponse().getLegislator().getAttributes();
                namePartyText.setText(temp.getFirstlast()+"("+temp.getParty()+"-NY)");
                phoneText.setText(temp.getPhone());
                websiteText.setText(temp.getWebsite());
            }

            @Override
            public void onFailure(Call<DetailInfo> call, Throwable t) {
                Toast.makeText(DetailRepsActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
            }
        });

        /*---------------------------------------------------------------------------------
        // Toolbar Area
        ---------------------------------------------------------------------------------*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*---------------------------------------------------------------------------------
        // ViewPagers and tab layout
        ---------------------------------------------------------------------------------*/
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.detail_reps_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail_reps, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Contributors";
                case 1:
                    return "Issues";
            }
            return null;
        }
    }
}
