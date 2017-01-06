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

    private String mQuery;

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
        RecyclerView resultRepsRecyclerview = (RecyclerView) view.findViewById(R.id.result_representatives_recyclerview),
                resultNewsRecyclerview = (RecyclerView) view.findViewById(R.id.result_news_recyclerview),
                resultBillsRecyclerview = (RecyclerView) view.findViewById(R.id.result_bills_recyclerview);

        LinearLayout resultRepsLayout = (LinearLayout) view.findViewById(R.id.result_representatives),
                resultNewsLayout = (LinearLayout) view.findViewById(R.id.result_news),
                resultBillsLayout = (LinearLayout) view.findViewById(R.id.result_bills);

        resultRepsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        resultNewsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        resultBillsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        /*---------------------------------------------------------------------------------
        // Reps Result
        ---------------------------------------------------------------------------------*/
        Retrofit sunlightRetrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.sunlight_base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<RepsList> repsCall = sunlightRetrofit.create(SunlightService.class).searchLegislatures(mQuery);
        repsCall.enqueue(new Callback<RepsList>() {
            @Override
            public void onResponse(Call<RepsList> call, Response<RepsList> response) {
                List<Result> results = response.body().getResults();
                if (results.size()>0) {
                    resultRepsRecyclerview.setAdapter(new ResultRepsAdapter(results, getContext(), ResultFragment.this));
                }else{
                    resultRepsLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<RepsList> call, Throwable t) {
                Toast.makeText(getContext(), "Representatives Search Fail", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

        /*---------------------------------------------------------------------------------
        // News Result
        ---------------------------------------------------------------------------------*/
        Retrofit newsRetrofit = new Retrofit.Builder()
                .baseUrl(NewsFragment.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<News> newsCall = newsRetrofit.create(BingSearchService.class).getNews(mQuery+" politic",5,0);
        newsCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.body().getValue().size()>0) {
                    resultNewsRecyclerview.setAdapter(new NewsAdapter(response.body().getValue(), getContext(), ResultFragment.this));
                }else{
                    resultNewsLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(getContext(), "News Search Fail", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

        /*---------------------------------------------------------------------------------
        // Bills Result
        ---------------------------------------------------------------------------------*/

        Call<BillSearch> billsCall = sunlightRetrofit.create(SunlightService.class).searchBill(mQuery,null);
        billsCall.enqueue(new Callback<BillSearch>() {
            @Override
            public void onResponse(Call<BillSearch> call, Response<BillSearch> response) {
                if (response.body().getResults().size()>0) {
                    resultBillsRecyclerview.setAdapter(new ResultBillAdapter(response.body().getResults(),ResultFragment.this));
                }else{
                    resultBillsLayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<BillSearch> call, Throwable t) {
                Toast.makeText(getContext(), "Bills Search Fail", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    // TODO: 1/5/17
    @Override
    public void OnRepsResultSelected(String id) {

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
    public void OnResultBillSelected(String billId) {
        Intent intent = new Intent(getContext(), DetailBillActivity.class);
        intent.putExtra("id",billId.toLowerCase());
        getActivity().startActivity(intent);

    }
}
