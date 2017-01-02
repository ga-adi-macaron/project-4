package com.joelimyx.politicallocal.bills;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.joelimyx.politicallocal.EndlessRecyclerViewScrollListener;
import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.bills.detail.DetailBillActivity;
import com.joelimyx.politicallocal.bills.gson.Bill;
import com.joelimyx.politicallocal.bills.gson.RecentBills;
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
        FilterDialogFragment.OnDialogPositiveClickedListener{

    public static final String propublica_baseURL = "https://api.propublica.org/";
    private static final String TAG = "BillFragment";

    private SwipeRefreshLayout mRefreshLayout;
    private BillAdapter mAdapter;
    private RecyclerView mRecyclerView;

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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.bill_recyclerview);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new BillAdapter(new ArrayList<>(),this);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.bill_swiperefresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(propublica_baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // TODO: 1/1/17 Change this to dynamic
        makeCall("senate", "passed", 0);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // TODO: 1/1/17 Change this to dynamic
                makeCall("senate", "passed", page);
            }
        });

        FloatingActionButton filterButton = (FloatingActionButton) view.findViewById(R.id.bill_filter_fab);
        filterButton.setOnClickListener(v -> showDialog());
    }

    @Override
    public void onRefresh() {
        // TODO: 1/1/17 Change this to dynamic
        makeCall("senate", "passed", 0);
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

        Log.d(TAG, "makeCall() called with: chamber = [" + chamber + "], filter = [" + filter + "], page = [" + page + "]");
        Call<RecentBills> call = mRetrofit.create(PropublicaService.class).getRecentBills(chamber, filter, page*20);
        call.enqueue(new Callback<RecentBills>() {
            @Override
            public void onResponse(Call<RecentBills> call, Response<RecentBills> response) {
                List<Bill> bills = response.body().getResults().get(0).getBills();
                if (page>0){
                    mAdapter.addData(bills);
                }else {
                    mAdapter.swapData(bills);
                }
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RecentBills> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to get Bill list", Toast.LENGTH_SHORT).show();
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showDialog(){
        FragmentManager manager = getFragmentManager();
        FilterDialogFragment fragment = FilterDialogFragment.newInstance("Senate","passed");
        fragment.setListener(this);
        fragment.show(manager,"Filter fragment");
    }

    /*---------------------------------------------------------------------------------
    // Interface AREA
    ---------------------------------------------------------------------------------*/
    @Override
    public void onBillItemSelected(String billId) {
        Intent intent = new Intent(getContext(), DetailBillActivity.class);
        intent.putExtra("id",billId.toLowerCase());
        getActivity().startActivity(intent);
    }

    @Override
    public void OnBottomMenuItemSelected(String tag) {
        if (tag.equals(getString(R.string.bill_fragment)) )
           mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void OnDialogPositiveClicked(String chamber, String filter) {
        makeCall(chamber, filter, 0);
        mRecyclerView.smoothScrollToPosition(0);
    }
}
