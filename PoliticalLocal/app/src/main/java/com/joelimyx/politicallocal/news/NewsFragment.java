package com.joelimyx.politicallocal.news;


import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.joelimyx.politicallocal.EndlessRecyclerViewScrollListener;
import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.database.RepsSQLHelper;
import com.joelimyx.politicallocal.news.gson.News;
import com.joelimyx.politicallocal.news.gson.Value;
import com.joelimyx.politicallocal.reps.MyRep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsFragment extends Fragment
        implements NewsAdapter.OnNewsItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener{

    private static String baseUrl = "https://api.cognitive.microsoft.com/";
    private SwipeRefreshLayout mRefreshLayout;
    private Retrofit mRetrofit;
    private Call<News> mCall;

    private NewsAdapter mNewsAdapter;
    private EndlessRecyclerViewScrollListener mScrollListener;

    private Map<String, List<Value>> mNewsMap = new HashMap<>();

    public NewsFragment() {
    }

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final RecyclerView newsRecyclerView = (RecyclerView) view.findViewById(R.id.news_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        newsRecyclerView.setLayoutManager(manager);
        mNewsAdapter = new NewsAdapter(new ArrayList<>(),getContext(),NewsFragment.this);
        newsRecyclerView.setAdapter(mNewsAdapter);
        mScrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                addNews(page);
            }
        };
        newsRecyclerView.addOnScrollListener(mScrollListener);

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.news_swipe_refresh);
        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.setOnRefreshListener(this);

        mRetrofit= new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // TODO: 12/17/16 Customize call
        makeCall();
    }

    @Override
    public void onNewsItemSelected(final String url) {
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
    public void onRefresh() {
        makeCall();
    }

    /*---------------------------------------------------------------------------------
    // Helper Method AREA
    ---------------------------------------------------------------------------------*/
    private void makeCall(){
        mNewsAdapter.clearData();
        mScrollListener.resetState();
        addNews(0);
    }

    private void addNews(int page){
        List<MyRep> myReps = RepsSQLHelper.getInstance(getContext()).getRepsList();
        List<Value> myNewsFeed = new ArrayList<>(myReps.size()*3);
        for (MyRep current : myReps) {
            mCall = mRetrofit.create(BingSearchService.class).getNews(current.getName(), 20, page*20);

            mCall.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {

                    List<Value> newsList = response.body().getValue();
                    mNewsMap.put(current.getName(),newsList);
                    if(mNewsMap.size()==myReps.size()){
                        for (int i = 0; i < 20; i++) {
                            for (MyRep name : myReps) {
                                if(i<mNewsMap.get(name.getName()).size()) {
                                    myNewsFeed.add(
                                            mNewsMap.get(name.getName())
                                                    .get(i));
                                }
                            }
                        }
                        mNewsAdapter.addData(myNewsFeed);
                    }
                    mRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    Toast.makeText(getContext(), "Failed to get News", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    mRefreshLayout.setRefreshing(false);
                }
            });
        }
    }
}
