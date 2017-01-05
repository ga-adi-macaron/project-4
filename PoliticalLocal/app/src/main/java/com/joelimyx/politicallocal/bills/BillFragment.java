package com.joelimyx.politicallocal.bills;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
        FilterDialogFragment.OnDialogPositiveClickedListener,
        MainActivity.OnBillFabClickedListener{

    public static final String propublica_baseURL = "https://api.propublica.org/";
    private static final String TAG = "BillFragment";

    private TextView mChamberText, mFilterText;
    private SwipeRefreshLayout mRefreshLayout;
    private BillAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private VotersAdapter mVotersAdapter;

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

        mChamberText = (TextView) view.findViewById(R.id.current_chamber_text);
        mFilterText = (TextView) view.findViewById(R.id.current_filter_text);

        String[] myPref = getFilterPref();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.bill_recyclerview);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new BillAdapter(new ArrayList<>(),getContext(),this, myPref[1]);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.bill_swiperefresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(propublica_baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        makeCall(myPref[0],myPref[1], 0);
        setFilterText(myPref[0],myPref[1]);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                String[] temp = getFilterPref();
                makeCall(temp[0],temp[1], page);
            }
        });
    }

    @Override
    public void onRefresh() {
        String[] temp = getFilterPref();
        makeCall(temp[0],temp[1], 0);
    }

    public MainActivity.OnBottomMenuItemSelectedListener getListener(){
        return this;
    }

    public MainActivity.OnBillFabClickedListener getFabListener(){
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

        Call<RecentBills> call = mRetrofit.create(PropublicaService.class).getRecentBills(chamber, filter, page*20);
        call.enqueue(new Callback<RecentBills>() {
            @Override
            public void onResponse(Call<RecentBills> call, Response<RecentBills> response) {
                List<Bill> bills = response.body().getResults().get(0).getBills();
                if (page>0){
                    mAdapter.addData(bills);
                }else {
                    mAdapter.swapData(bills, filter);
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

    /**
     * Create dialog fragment
     */
    private void showDialog(){
        String[] temp = getFilterPref();

        FragmentManager manager = getFragmentManager();
        FilterDialogFragment fragment = FilterDialogFragment.newInstance(temp[0], temp[1]);
        fragment.setListener(this);
        fragment.show(manager,"Filter fragment");
    }

    /**
     * Get the chamber and filter from the shared preference
     * @return String array with chamber and filter
     */
    private String[] getFilterPref(){
        String[] myPref = new String[2];
        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.bill_filter), Context.MODE_PRIVATE);
        myPref[0] = preferences.getString(getString(R.string.chamber),getString(R.string.chamber_default));
        myPref[1] = preferences.getString(getString(R.string.filter),getString(R.string.filter_default));
        return myPref;
    }

    private void setFilterText(String chamber, String filter){
        chamber = chamber.substring(0,1).toUpperCase() + chamber.substring(1);
        filter = filter.substring(0,1).toUpperCase() + filter.substring(1);
        mChamberText.setText("Chamber: "+chamber);
        mFilterText.setText("Filter: "+filter);
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
        setFilterText(chamber,filter);
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void OnBillFabClicked() {
        showDialog();
    }
}
