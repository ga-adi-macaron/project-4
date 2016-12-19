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
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.database.RepsSQLHelper;
import com.joelimyx.politicallocal.reps.MyReps;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailRepsActivity extends AppCompatActivity implements View.OnClickListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private MyReps mMyReps;
    private static final String TAG = "DetailRepsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reps);

        RepsSQLHelper db = RepsSQLHelper.getInstance(this);
        /*---------------------------------------------------------------------------------
        // Basic info
        ---------------------------------------------------------------------------------*/
        TextView namePartyText = (TextView) findViewById(R.id.detail_reps_name_party);
        ImageView phoneImage = (ImageView) findViewById(R.id.detail_reps_phone);
        ImageView emailImage = (ImageView) findViewById(R.id.detail_reps_email);
        ImageView websiteImage= (ImageView) findViewById(R.id.detail_reps_website);
        ImageView detailRepsImage= (ImageView) findViewById(R.id.detail_reps_image);


        mMyReps = db.getMyRepByID(getIntent().getStringExtra("id"));
        Picasso.with(this)
                .load(getFileStreamPath(mMyReps.getFileName()))
                .fit().into(detailRepsImage);
        namePartyText.setText(mMyReps.getName());

        phoneImage.setOnClickListener(this);
        emailImage.setOnClickListener(this);
        websiteImage.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        // TODO: 12/18/16 App Linking
        switch (view.getId()){
            case R.id.detail_reps_phone:
                break;
            case R.id.detail_reps_email:
                break;
            case R.id.detail_reps_website:
                break;
        }
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position==1){
                return ContributorFragment.newInstance(mMyReps.getCId());
            }
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
                    return "Issues";
                case 1:
                    return "Contributors";
                default:
                    return null;
            }
        }
    }
}
