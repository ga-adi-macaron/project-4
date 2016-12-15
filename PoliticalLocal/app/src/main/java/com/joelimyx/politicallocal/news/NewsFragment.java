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

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.news.Gson.News;
import com.joelimyx.politicallocal.news.Gson.Value;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements NewsAdapter.OnNewsItemSelectedListener{
    private static String baseUrl = "https://api.cognitive.microsoft.com/";
    private SwipeRefreshLayout mRefreshLayout;

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
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.news_swipe_refresh);
        mRefreshLayout.setRefreshing(true);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<News> call = retrofit.create(NewsService.class).getNews("bernie", 12);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                List<Value> newsList = response.body().getValue();
                newsRecyclerView.setAdapter(new NewsAdapter(newsList,getContext(),NewsFragment.this));
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

    @Override
    public void onNewsItemSelected(final String url) {
        String packageName = "com.android.chrome";
        CustomTabsClient.bindCustomTabsService(getContext(), packageName, new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                client.warmup(0L);

                Uri uri = Uri.parse(url);
                CustomTabsSession session = client.newSession(null);
                session.mayLaunchUrl(uri,null,null);

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
}
