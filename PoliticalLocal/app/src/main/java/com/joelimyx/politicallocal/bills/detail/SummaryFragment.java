package com.joelimyx.politicallocal.bills.detail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.bills.detail.gson.summary.BillSummary;
import com.joelimyx.politicallocal.bills.detail.gson.summary.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SummaryFragment extends Fragment {
    private static final String ARG_ID = "bill_id";
    public static final String congress_baseurl = "https://congress.api.sunlightfoundation.com/";
    private static final String TAG = "SummaryFragment";

    private String mBillId;
    private TextView mBillSummary, mBillTags;
    private String mLongSummary;
    private boolean mIsExpanded = false;

    public SummaryFragment() {
    }

    public static SummaryFragment newInstance(String bil_id) {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, bil_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBillId = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mBillSummary = (TextView) view.findViewById(R.id.bill_summary_text);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(congress_baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<BillSummary> call = retrofit.create(SummaryService.class).getBillSummary(mBillId+"-114");
        call.enqueue(new Callback<BillSummary>() {
            @Override
            public void onResponse(Call<BillSummary> call, Response<BillSummary> response) {
                Result summary = response.body().getResults().get(0);
                if (summary.getSummaryShort()!=null) {
                    mBillSummary.setText(summary.getSummaryShort());
                    mLongSummary =summary.getSummary();
                    mBillSummary.setOnClickListener(v -> {
                        if (!mIsExpanded) {
                            mBillSummary.setText(mLongSummary);
                            mIsExpanded = !mIsExpanded;
                        }else{
                            mBillSummary.setText(summary.getSummaryShort());
                            mIsExpanded = !mIsExpanded;
                        }
                    });
                }else{
                    mBillSummary.setText("No Summary available");
                }
                mBillTags.setText(summary.getKeywords().get(0));
            }

            @Override
            public void onFailure(Call<BillSummary> call, Throwable t) {

            }
        });
    }
}
