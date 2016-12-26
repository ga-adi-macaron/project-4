package com.joelimyx.politicallocal.bills.detail;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.bills.BillFragment;
import com.joelimyx.politicallocal.bills.PropublicaService;
import com.joelimyx.politicallocal.bills.detail.gson.propublica.DetailBill;
import com.joelimyx.politicallocal.bills.detail.gson.propublica.Result;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailBillActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private Result mDetailBill;
    private String mBill,mCoSponsor;

    private TextView mDetailBillTitle,mDetailBillSponsor,mDetailBillPDF;
    private ActionBar mActionBar;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private static final String TAG = "DetailBillActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bill);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.bill_collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.bill_appbar_layout);
        appBarLayout.addOnOffsetChangedListener(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        Picasso.with(this)
                .load("http://www.psdgraphics.com/file/old-paper-texture.jpg").fit()
                .into((ImageView)findViewById(R.id.bill_background));

        mDetailBillTitle= (TextView) findViewById(R.id.detail_bill_title);
        mDetailBillSponsor= (TextView) findViewById(R.id.detail_bill_sponsor);
        mDetailBillPDF= (TextView) findViewById(R.id.detail_bill_pdf);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BillFragment.propublica_baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<DetailBill> call = retrofit.create(PropublicaService.class).getDetailBill(getIntent().getStringExtra("id"));
        call.enqueue(new Callback<DetailBill>() {
            @Override
            public void onResponse(Call<DetailBill> call, Response<DetailBill> response) {
                if (call.isExecuted()) {
                    mDetailBill = response.body().getResults().get(0);

                    //Toolbar titles
                    mBill = mDetailBill.getBill();
                    mCoSponsor = mDetailBill.getCosponsors();


                    mDetailBillTitle.setText(mDetailBill.getTitle());
                    mDetailBillTitle.setEllipsize(TextUtils.TruncateAt.END);
                    mDetailBillTitle.setMarqueeRepeatLimit(3);
                    mDetailBillSponsor.setText(mDetailBill.getSponsor());
                    SpannableString content = new SpannableString("More detail");
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    mDetailBillPDF.setText(content);
                    mDetailBillPDF.setOnClickListener(v ->{
                            String packageName = "com.android.chrome";
                            CustomTabsClient.bindCustomTabsService(DetailBillActivity.this, packageName, new CustomTabsServiceConnection() {
                                @Override
                                public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                                    client.warmup(10L);

                                    Uri uri = Uri.parse(mDetailBill.getGpoPdfUri());
                                    CustomTabsSession session = client.newSession(null);
                                    session.mayLaunchUrl(uri,null,null);

                                    //Create intent with the loaded session and change the toolbar color of custom chrome tab
                                    CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(session)
                                            .setToolbarColor(ContextCompat.getColor(DetailBillActivity.this,R.color.colorPrimary))
                                            .build();
                                    customTabsIntent.launchUrl(DetailBillActivity.this,uri);
                                }

                                @Override
                                public void onServiceDisconnected(ComponentName componentName) {
                                    Toast.makeText(DetailBillActivity.this, "Connection Lost", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    );
                }
            }

            @Override
            public void onFailure(Call<DetailBill> call, Throwable t) {

            }
        });
        DetailBillPagerAdapter detailBillPagerAdapter = new DetailBillPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.detail_bill_container);
        viewPager.setAdapter(detailBillPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.detail_bill_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int scrollRange = appBarLayout.getTotalScrollRange();
        if (scrollRange==Math.abs(verticalOffset)){
            mCollapsingToolbarLayout.setTitle(mBill);
            mActionBar.setSubtitle(mCoSponsor);
        }else{
            mCollapsingToolbarLayout.setTitle(" ");
        }
    }


    public class DetailBillPagerAdapter extends FragmentPagerAdapter {

        public DetailBillPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position==1) {
                return UpdateFragment.newInstance(getIntent().getStringExtra("id"));
            }
            return SummaryFragment.newInstance(getIntent().getStringExtra("id"));
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Summary";
                case 1:
                    return "Update";
                default:
                    return null;
            }
        }
    }
}
