package com.joelimyx.politicallocal.search;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.main.MainActivity;
import com.joelimyx.politicallocal.reps.detail.ContributorFragment;
import com.joelimyx.politicallocal.reps.detail.DetailRepsActivity;
import com.joelimyx.politicallocal.reps.gson.congress.RepsList;
import com.joelimyx.politicallocal.reps.gson.congress.Result;
import com.joelimyx.politicallocal.reps.service.SunlightService;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailRepsResultActivity extends AppCompatActivity
        implements View.OnClickListener,
        Toolbar.OnMenuItemClickListener{

    private Result mRep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reps);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.sunlight_base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        /*---------------------------------------------------------------------------------
        // Basic info and FAB menu
        ---------------------------------------------------------------------------------*/
        TextView namePartyText = (TextView) findViewById(R.id.detail_reps_name_party);
        ImageView detailRepsImage= (ImageView) findViewById(R.id.detail_reps_image);
        TwitterAuthConfig authConfig = new TwitterAuthConfig("V2RCaymY8YS5r2hQLtKPf1A5s","FX3mhvuWzZQJkBxF6Idm8SwrJdwPamBh8yL3UgYTyfYP9pwKCd");
        Fabric.with(this, new Twitter(authConfig));

        FloatingActionButton phoneFab = (FloatingActionButton) findViewById(R.id.phone_fab),
                emailFab = (FloatingActionButton) findViewById(R.id.email_fab),
                linkFab = (FloatingActionButton) findViewById(R.id.link_fab),
                twitterFab = (FloatingActionButton) findViewById(R.id.twitter_fab);


        Call<RepsList> call = retrofit.create(SunlightService.class).getRepByID(getIntent().getStringExtra("id"));
        call.enqueue(new Callback<RepsList>() {
            @Override
            public void onResponse(Call<RepsList> call, Response<RepsList> response) {
                mRep = response.body().getResults().get(0);

                phoneFab.setOnClickListener(DetailRepsResultActivity.this);
                emailFab.setOnClickListener(DetailRepsResultActivity.this);
                linkFab.setOnClickListener(DetailRepsResultActivity.this);
                twitterFab.setOnClickListener(DetailRepsResultActivity.this);

                Picasso.with(DetailRepsResultActivity.this)
                        .load("https://theunitedstates.io/images/congress/original/"+mRep.getBioguideId()+".jpg")
                        .fit().into(detailRepsImage);

                StringBuilder builder = new StringBuilder();
                builder.append(mRep.getTitle())
                        .append(mRep.getFirstName())
                        .append(" ")
                        .append(mRep.getMiddleName())
                        .append(mRep.getMiddleName() == null ? "":" ")
                        .append(mRep.getLastName());

                namePartyText.setText(builder.toString());

                 /*---------------------------------------------------------------------------------
                // ViewPagers and tab layout
                ---------------------------------------------------------------------------------*/
                DetailRepsPagerAdapter mDetailRepsPagerAdapter = new DetailRepsPagerAdapter(getSupportFragmentManager());
                ViewPager mViewPager = (ViewPager) findViewById(R.id.detail_reps_container);
                mViewPager.setAdapter(mDetailRepsPagerAdapter);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.detail_reps_tabs);
                tabLayout.setupWithViewPager(mViewPager);
            }

            @Override
            public void onFailure(Call<RepsList> call, Throwable t) {
                Toast.makeText(DetailRepsResultActivity.this, "Failed to get Detail Info", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.phone_fab:
                //Show up the phone dial for user to decide whether to call
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ mRep.getPhone()));
                startActivity(phoneIntent);

                break;
            case R.id.email_fab:
                //Open email app for user to send email with their desire email app
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{mRep.getOcEmail()});
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

                        Uri uri = Uri.parse(mRep.getWebsite());
                        CustomTabsSession session = client.newSession(null);
                        session.mayLaunchUrl(uri,null,null);

                        //Create intent with the loaded session and change the toolbar color of custom chrome tab
                        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(session)
                                .setToolbarColor(ContextCompat.getColor(DetailRepsResultActivity.this,R.color.colorPrimary))
                                .build();
                        customTabsIntent.launchUrl(DetailRepsResultActivity.this,uri);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {
                        Toast.makeText(DetailRepsResultActivity.this, "Connection Lost", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.twitter_fab:
                //Use custom call to get twitter id from the presented user name
                TwitterIdClient mClient = new TwitterIdClient(new OkHttpClient.Builder().build());
                Call<User> call = mClient.getIdService().getId(mRep.getTwitterId());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        long id = response.body().id;
                        Intent twitterIntent;
                        try {
                            //Launch the official twitter app
                            getPackageManager().getPackageInfo("com.twitter.android",0);
                            twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id="+id));
                        } catch (Exception e) {
                            // no Twitter app, revert to browser
                            twitterIntent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/"+ mRep.getTwitterId()));
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
        if (item.getItemId()==android.R.id.home)
            finish();
        return false;
    }
    public class DetailRepsPagerAdapter extends FragmentPagerAdapter {

        public DetailRepsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position==1){
                return ContributorFragment.newInstance(mRep.getCrpId());
            }
            // TODO: 12/20/16 Add a issue fragment basic info
            return ContributorFragment.newInstance(mRep.getCrpId());
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
