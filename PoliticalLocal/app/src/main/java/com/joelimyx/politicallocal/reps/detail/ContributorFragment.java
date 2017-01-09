package com.joelimyx.politicallocal.reps.detail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.reps.gson.opensecret.Contributor;
import com.joelimyx.politicallocal.reps.gson.opensecret.Contributors;
import com.joelimyx.politicallocal.reps.gson.opensecret.ContributorsList;
import com.joelimyx.politicallocal.reps.service.OpenSecretService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContributorFragment extends Fragment {
    private static final String ARG_ID = "id";
    public static final String opensecret_URL = "https://www.opensecrets.org/";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mCId;

    public ContributorFragment() {
        // Required empty public constructor
    }

    public static ContributorFragment newInstance(String param1) {
        ContributorFragment fragment = new ContributorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCId = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contributor, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView contributor_recyclerView = (RecyclerView) view.findViewById(R.id.contributor_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.contributor_refresh);
        mSwipeRefreshLayout.setRefreshing(true);

        contributor_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(opensecret_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<ContributorsList> call = retrofit.create(OpenSecretService.class).getContributors(mCId);
        call.enqueue(new Callback<ContributorsList>() {
            @Override
            public void onResponse(Call<ContributorsList> call, Response<ContributorsList> response) {
                List<Contributor> contributors = response.body().getResponse().getContributors().getContributor();
                contributor_recyclerView.setAdapter(new ContributorAdapter(contributors));
                mSwipeRefreshLayout.setEnabled(false);
            }

            @Override
            public void onFailure(Call<ContributorsList> call, Throwable t) {
                mSwipeRefreshLayout.setEnabled(false);
            }
        });
    }
}
