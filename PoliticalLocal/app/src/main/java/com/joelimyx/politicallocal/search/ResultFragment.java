package com.joelimyx.politicallocal.search;


import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.bills.BillAdapter;
import com.joelimyx.politicallocal.bills.detail.DetailBillActivity;
import com.joelimyx.politicallocal.main.MainActivity;
import com.joelimyx.politicallocal.news.BingSearchService;
import com.joelimyx.politicallocal.news.NewsAdapter;
import com.joelimyx.politicallocal.news.NewsFragment;
import com.joelimyx.politicallocal.news.gson.News;
import com.joelimyx.politicallocal.reps.gson.congress.RepsList;
import com.joelimyx.politicallocal.reps.gson.congress.Result;
import com.joelimyx.politicallocal.reps.service.SunlightService;
import com.joelimyx.politicallocal.search.gson.BillSearch;
import com.twitter.sdk.android.core.models.Card;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultFragment extends Fragment
        implements ResultRepsAdapter.OnRepsResultSelectedListener,
        NewsAdapter.OnNewsItemSelectedListener, ResultBillAdapter.OnResultBillSelectedListener{
    private static final String ARG_QUERY = "query";

    private static final int VISI_NONE_CODE = 0;
    private static final int VISI_REPS_CODE = 1;
    private static final int VISI_BILL_CODE = 2;
    private static final int VISI_NEWS_CODE = 3;
    private static final int VISI_REPS_BILL_CODE = 4;
    private static final int VISI_NEWS_BILL_CODE = 5;
    private static final int VISI_REPS_NEWS_CODE = 6;

    private String mQuery;
    private Retrofit mSunlightRetrofit, mNewsRetrofit;
    private RecyclerView mResultRepsRecyclerview, mResultNewsRecyclerview, mResultBillsRecyclerview;
    private LinearLayout mResultRepsLayout, mResultNewsLayout, mResultBillsLayout;
    private CardView mNoResult;

    public ResultFragment() {
    }

    public static ResultFragment newInstance(String param1) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuery = getArguments().getString(ARG_QUERY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mResultRepsRecyclerview = (RecyclerView) view.findViewById(R.id.result_representatives_recyclerview);
        mResultNewsRecyclerview = (RecyclerView) view.findViewById(R.id.result_news_recyclerview);
        mResultBillsRecyclerview = (RecyclerView) view.findViewById(R.id.result_bills_recyclerview);

        mResultRepsLayout = (LinearLayout) view.findViewById(R.id.result_representatives);
        mResultNewsLayout = (LinearLayout) view.findViewById(R.id.result_news);
        mResultBillsLayout = (LinearLayout) view.findViewById(R.id.result_bills);

        mResultRepsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mResultNewsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mResultBillsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        mResultRepsRecyclerview.setNestedScrollingEnabled(false);
        mResultNewsRecyclerview.setNestedScrollingEnabled(false);
        mResultBillsRecyclerview.setNestedScrollingEnabled(false);

        mNoResult = (CardView) view.findViewById(R.id.no_result);
        /*---------------------------------------------------------------------------------
        // Retrofit AREA
        ---------------------------------------------------------------------------------*/
        mSunlightRetrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.sunlight_base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mNewsRetrofit = new Retrofit.Builder()
                .baseUrl(NewsFragment.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (mQuery.matches(".*\\d.*")){
            try{
                int zip = Integer.valueOf(mQuery);
                locateReps(String.valueOf(mQuery));
                hideLayouts(VISI_REPS_CODE);
            }catch (NumberFormatException e) {
                e.printStackTrace();
                hideLayouts(VISI_NONE_CODE);
            }
        }else{
            searchNews(mQuery);
            searchReps(mQuery);
            searchBills(mQuery);
        }

    }

    /*---------------------------------------------------------------------------------
    // Search helper method
    ---------------------------------------------------------------------------------*/

    /**
     * Search representative by name
     * @param query name of the representative
     */
    private void searchReps(String query){

        Call<RepsList> repsCall = mSunlightRetrofit.create(SunlightService.class).searchLegislatures(mQuery);
        repsCall.enqueue(new Callback<RepsList>() {
            @Override
            public void onResponse(Call<RepsList> call, Response<RepsList> response) {
                List<Result> results = response.body().getResults();
                if (results.size()>0) {
                    mResultRepsRecyclerview.setAdapter(new ResultRepsAdapter(results, getContext(), ResultFragment.this));
                }else{
                    mResultRepsLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<RepsList> call, Throwable t) {
                Toast.makeText(getContext(), "Representatives Search Fail", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    /**
     * Locate representative by zip
     * @param zip postal code
     */
    private void locateReps(String zip){
        Call<RepsList> repsCall = mSunlightRetrofit.create(SunlightService.class).getLegislatures(0,0,zip);
        repsCall.enqueue(new Callback<RepsList>() {
            @Override
            public void onResponse(Call<RepsList> call, Response<RepsList> response) {
                List<Result> results = response.body().getResults();
                if (results.size()>0) {
                    mResultRepsRecyclerview.setAdapter(new ResultRepsAdapter(results, getContext(), ResultFragment.this));
                }else{
                    mResultRepsLayout.setVisibility(View.GONE);
                    mNoResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<RepsList> call, Throwable t) {
                Toast.makeText(getContext(), "Representatives Search Fail", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    /**
     * Search on news related to politic with query
     * @param query search query
     */
    private void searchNews(String query){
        Call<News> newsCall = mNewsRetrofit.create(BingSearchService.class).getNews(mQuery+" politic",5,0);
        newsCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.body().getValue().size()>0) {
                    mResultNewsRecyclerview.setAdapter(new NewsAdapter(response.body().getValue(), getContext(), ResultFragment.this));
                }else{
                    mResultNewsLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(getContext(), "News Search Fail", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    /**
     * Search bill's tags or titles that contains
     * @param query tags or title
     */
    private void searchBills(String query){

        Call<BillSearch> billsCall = mSunlightRetrofit.create(SunlightService.class).searchBill(mQuery,null);
        billsCall.enqueue(new Callback<BillSearch>() {
            @Override
            public void onResponse(Call<BillSearch> call, Response<BillSearch> response) {
                if (response.body().getResults().size()>0) {
                    mResultBillsRecyclerview.setAdapter(new ResultBillAdapter(response.body().getResults(),ResultFragment.this));
                }else{
                    mResultBillsLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<BillSearch> call, Throwable t) {
                Toast.makeText(getContext(), "Bills Search Fail", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void hideLayouts(int code){
        switch (code){
            case VISI_NONE_CODE:
                mResultBillsLayout.setVisibility(View.GONE);
                mResultNewsLayout.setVisibility(View.GONE);
                mResultRepsLayout.setVisibility(View.GONE);
                mNoResult.setVisibility(View.VISIBLE);
                break;

            case VISI_REPS_CODE:
                mResultNewsLayout.setVisibility(View.GONE);
                mResultBillsLayout.setVisibility(View.GONE);
                break;

            case VISI_NEWS_CODE:
                mResultBillsLayout.setVisibility(View.GONE);
                mResultRepsLayout.setVisibility(View.GONE);
                break;

            case VISI_BILL_CODE:
                mResultNewsLayout.setVisibility(View.GONE);
                mResultRepsLayout.setVisibility(View.GONE);
                break;

            case VISI_REPS_BILL_CODE:
                mResultNewsLayout.setVisibility(View.GONE);
                break;

            case VISI_NEWS_BILL_CODE:
                mResultRepsLayout.setVisibility(View.GONE);
                break;

            case VISI_REPS_NEWS_CODE:
                mResultBillsLayout.setVisibility(View.GONE);
                break;

        }
    }
    /*---------------------------------------------------------------------------------
    // INTERFACE AREA
    ---------------------------------------------------------------------------------*/

    @Override
    public void OnRepsResultSelected(String id) {
        Intent intent = new Intent(getContext(),DetailRepsResultActivity.class);
        intent.putExtra("id",id.toUpperCase());
        startActivity(intent);
    }

    @Override
    public void onNewsItemSelected(String url) {
        String packageName = "com.android.chrome";
        CustomTabsClient.bindCustomTabsService(getContext(), packageName, new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                client.warmup(10L);

                Uri uri = Uri.parse(url);
                CustomTabsSession session = client.newSession(null);
                session.mayLaunchUrl(uri,null,null);

                //Create intent with the loaded session and change the toolbar color of custom chrome tab
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(session)
                        .setToolbarColor(ContextCompat.getColor(getContext(),R.color.colorPrimary))
                        .build();
                customTabsIntent.launchUrl(getContext(),uri);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Toast.makeText(getContext(), "Connection Lost", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void OnResultBillSelected(String billId, long congress) {
        Intent intent = new Intent(getContext(), DetailBillActivity.class);
        intent.putExtra("congress",(int)congress);
        intent.putExtra("id",billId.toLowerCase());
        getActivity().startActivity(intent);

    }
}
