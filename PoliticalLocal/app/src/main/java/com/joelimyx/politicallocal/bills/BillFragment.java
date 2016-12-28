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

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.bills.detail.DetailBillActivity;
import com.joelimyx.politicallocal.bills.gson.Bill;
import com.joelimyx.politicallocal.bills.gson.RecentBills;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BillFragment extends Fragment
        implements BillAdapter.OnBillItemSelectedListener, SwipeRefreshLayout.OnRefreshListener{

    public static final String propublica_baseURL = "https://api.propublica.org/";
    private SwipeRefreshLayout mRefreshLayout;
    private BillAdapter mAdapter;

    private Retrofit mRetrofit;


    public BillFragment() {
        // Required empty public constructor
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
        RecyclerView billRecyclerview = (RecyclerView) view.findViewById(R.id.bill_recyclerview);
        billRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mAdapter = new BillAdapter(new ArrayList<>(),this);
        billRecyclerview.setAdapter(mAdapter);

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.bill_swiperefresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(propublica_baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        makeCall();
    }

    @Override
    public void onRefresh() {
        makeCall();
    }

    @Override
    public void onBillItemSelected(String billId) {
        Intent intent = new Intent(getContext(), DetailBillActivity.class);
        intent.putExtra("id",billId.toLowerCase());
        getActivity().startActivity(intent);
    }

    /*---------------------------------------------------------------------------------
    // Helper Method
    ---------------------------------------------------------------------------------*/
    /**
     * Enqueue call from Propublica Service
     */
    private void makeCall(){
        mRefreshLayout.setRefreshing(true);

        Call<RecentBills> call = mRetrofit.create(PropublicaService.class).getRecentBills(0);
        call.enqueue(new Callback<RecentBills>() {
            @Override
            public void onResponse(Call<RecentBills> call, Response<RecentBills> response) {
                List<Bill> bills = response.body().getResults().get(0).getBills();
                mAdapter.swapData(bills);
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RecentBills> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to get Bill list", Toast.LENGTH_SHORT).show();
                mRefreshLayout.setRefreshing(false);
            }
        });
    }
}
