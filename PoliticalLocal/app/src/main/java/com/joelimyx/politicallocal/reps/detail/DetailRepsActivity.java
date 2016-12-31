package com.joelimyx.politicallocal.reps.detail;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.database.RepsSQLHelper;
import com.joelimyx.politicallocal.reps.MyReps;
import com.joelimyx.politicallocal.reps.service.TwitterIdClient;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.models.User;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRepsActivity extends AppCompatActivity
        implements View.OnClickListener,
        Toolbar.OnMenuItemClickListener{

    private DetailRepsPagerAdapter mDetailRepsPagerAdapter;
    private ViewPager mViewPager;

    private MyReps mMyReps;
    private TwitterIdClient mClient;
    private static final String TAG = "DetailRepsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reps);

        TwitterAuthConfig authConfig = new TwitterAuthConfig("V2RCaymY8YS5r2hQLtKPf1A5s","FX3mhvuWzZQJkBxF6Idm8SwrJdwPamBh8yL3UgYTyfYP9pwKCd");
        Fabric.with(DetailRepsActivity.this, new Twitter(authConfig));

        RepsSQLHelper db = RepsSQLHelper.getInstance(this);

        /*---------------------------------------------------------------------------------
        // Basic info and FAB menu
        ---------------------------------------------------------------------------------*/
        TextView namePartyText = (TextView) findViewById(R.id.detail_reps_name_party);
        ImageView detailRepsImage= (ImageView) findViewById(R.id.detail_reps_image);

        FloatingActionButton phoneFab = (FloatingActionButton) findViewById(R.id.phone_fab),
                             emailFab = (FloatingActionButton) findViewById(R.id.email_fab),
                             linkFab = (FloatingActionButton) findViewById(R.id.link_fab),
                             twitterFab = (FloatingActionButton) findViewById(R.id.twitter_fab);

        phoneFab.setOnClickListener(this);
        emailFab.setOnClickListener(this);
        linkFab.setOnClickListener(this);
        twitterFab.setOnClickListener(this);

        mMyReps = db.getMyRepByID(getIntent().getStringExtra("id"));
        Picasso.with(this)
                .load(getFileStreamPath(mMyReps.getFileName()))
                .fit().into(detailRepsImage);
        namePartyText.setText(mMyReps.getName());

        /*---------------------------------------------------------------------------------
        // Toolbar Area
        ---------------------------------------------------------------------------------*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*---------------------------------------------------------------------------------
        // ViewPagers and tab layout
        ---------------------------------------------------------------------------------*/
        mDetailRepsPagerAdapter = new DetailRepsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.detail_reps_container);
        mViewPager.setAdapter(mDetailRepsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.detail_reps_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    /*---------------------------------------------------------------------------------
    // On Click AREA
    ---------------------------------------------------------------------------------*/
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.phone_fab:
                //Show up the phone dial for user to decide whether to call
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mMyReps.getPhone()));
                startActivity(phoneIntent);

                break;
            case R.id.email_fab:
                //Open email app for user to send email with their desire email app
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{mMyReps.getEmail()});
                emailIntent.setType("plain/text");
                startActivity(Intent.createChooser(emailIntent,"Send Email..."));
                break;
            case R.id.link_fab:
                //Launch a custom chrome tab for the legislator website
                String packageName = "com.android.chrome";
                CustomTabsClient.bindCustomTabsService(this, packageName, new CustomTabsServiceConnection() {
                    @Override
                    public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                        client.warmup(10L);

                        Uri uri = Uri.parse(mMyReps.getWebsite());
                        CustomTabsSession session = client.newSession(null);
                        session.mayLaunchUrl(uri,null,null);

                        //Create intent with the loaded session and change the toolbar color of custom chrome tab
                        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(session)
                                .setToolbarColor(ContextCompat.getColor(DetailRepsActivity.this,R.color.colorPrimary))
                                .build();
                        customTabsIntent.launchUrl(DetailRepsActivity.this,uri);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {
                        Toast.makeText(DetailRepsActivity.this, "Connection Lost", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.twitter_fab:
                //Use custom call to get twitter id from the presented user name
                mClient = new TwitterIdClient(new OkHttpClient.Builder().build());
                Call<User> call = mClient.getIdService().getId(mMyReps.getTwitter());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        long id = response.body().id;
                        Intent twitterIntent;
                        try {
                            //Launch the official twitter app
                            getPackageManager().getPackageInfo("com.twitter.android",0);
                            Log.d(TAG, "onClick: "+ mMyReps.getTwitter());
                            twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id="+id));
                        } catch (Exception e) {
                            // no Twitter app, revert to browser
                            twitterIntent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/"+mMyReps.getTwitter()));
                        }
                        startActivity(twitterIntent);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return false;
    }
    
    /*---------------------------------------------------------------------------------
    // Page Adapter Section
    ---------------------------------------------------------------------------------*/

    public class DetailRepsPagerAdapter extends FragmentPagerAdapter {

        public DetailRepsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position==1){
                return ContributorFragment.newInstance(mMyReps.getCId());
            }
            // TODO: 12/20/16 Add a issue fragment basic info
            return ContributorFragment.newInstance(mMyReps.getCId());
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
