package com.joelimyx.politicallocal.bills;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.joelimyx.politicallocal.EndlessRecyclerViewScrollListener;
import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.bills.detail.DetailBillActivity;
import com.joelimyx.politicallocal.bills.gson.Bill;
import com.joelimyx.politicallocal.bills.gson.RecentBills;
import com.joelimyx.politicallocal.bills.gson.Result;
import com.joelimyx.politicallocal.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BillFragment extends Fragment
        implements BillAdapter.OnBillItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener,
        MainActivity.OnBottomMenuItemSelectedListener,
        MaterialSpinner.OnItemSelectedListener<String>{

    public static final String propublica_baseURL = "https://api.propublica.org/";
    private static final String TAG = "BillFragment";

    private SwipeRefreshLayout mRefreshLayout;
    private BillAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private VotersAdapter mVotersAdapter;
    private MaterialSpinner mChamberSpinner, mFilterSpinner;

    private Retrofit mRetrofit;

    public BillFragment() {
    }

    public static BillFragment newInstance() {
        return new BillFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mChamberSpinner = (MaterialSpinner) view.findViewById(R.id.chamber_spinner);
        mFilterSpinner= (MaterialSpinner) view.findViewById(R.id.filter_spinner);

        mChamberSpinner.setItems("House", "Senate");
        mFilterSpinner.setItems("Introduced", "Passed", "Updated");

        mChamberSpinner.setOnItemSelectedListener(this);
        mFilterSpinner.setOnItemSelectedListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.bill_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new BillAdapter(new ArrayList<>(),getContext(),this, (String) mFilterSpinner.getText());
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.bill_swiperefresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(propublica_baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        makeCall(String.valueOf(mChamberSpinner.getText()), String.valueOf(mFilterSpinner.getText()), 0);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener( manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                makeCall(String.valueOf(mChamberSpinner.getText()), String.valueOf(mFilterSpinner.getText()), page);
            }
        });
    }

    @Override
    public void onRefresh() {
        makeCall(String.valueOf(mChamberSpinner.getText()), String.valueOf(mFilterSpinner.getText()), 0);
    }

    public MainActivity.OnBottomMenuItemSelectedListener getListener(){
        return this;
    }

    /*---------------------------------------------------------------------------------
    // Helper Method AREA
    ---------------------------------------------------------------------------------*/
    /**
     * Enqueue call from Propublica Service
     */
    private void makeCall(String chamber, String filter, int page){
        mRefreshLayout.setRefreshing(true);

        Call<RecentBills> call = mRetrofit.create(PropublicaService.class).getRecentBills(chamber.toLowerCase(), filter.toLowerCase(), page*20);
        call.enqueue(new Callback<RecentBills>() {
            @Override
            public void onResponse(Call<RecentBills> call, Response<RecentBills> response) {
                Result result = response.body().getResults().get(0);
                List<Bill> bills = result.getBills();
                if (page>0){
                    mAdapter.addData(bills);
                }else {
                    mAdapter.swapData(bills, filter, result.getCongress());
                }
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RecentBills> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to get Bill list", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    /*---------------------------------------------------------------------------------
    // Interface AREA
    ---------------------------------------------------------------------------------*/
    @Override
    public void onBillItemSelected(String billId, String session) {
        Intent intent = new Intent(getContext(), DetailBillActivity.class);
        intent.putExtra("id",billId.toLowerCase());
        intent.putExtra("congress",Integer.valueOf(session));
        getActivity().startActivity(intent);
    }

    @Override
    public void OnBottomMenuItemSelected(String tag) {
        if (tag.equals(getString(R.string.bill_fragment)) )
           mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
        switch(view.getId()){
            case R.id.chamber_spinner:
                makeCall(item, String.valueOf(mFilterSpinner.getText()),0);
                break;

            case R.id.filter_spinner:
                makeCall(String.valueOf(mChamberSpinner.getText()),item,0);
                break;
        }
    }
}
