package com.joelimyx.politicallocal.bills.detail.summary;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private static final String ARG_ID = "bill_id", ARG_SESSION = "session";
    public static final String congress_baseurl = "https://congress.api.sunlightfoundation.com/";

    private String mBillId, mLongSummary, mSession;
    private TextView mBillSummary, mBillTags;
    private ImageView mExpandMoreOrLess;
    private boolean mIsExpanded = false;

    public SummaryFragment() {
    }

    public static SummaryFragment newInstance(String bil_id, String session) {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, bil_id);
        args.putString(ARG_SESSION, session);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBillId = getArguments().getString(ARG_ID);
            mSession= getArguments().getString(ARG_SESSION);
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
        mBillTags = (TextView) view.findViewById(R.id.bill_tags);
        mExpandMoreOrLess= (ImageView) view.findViewById(R.id.expand_more_or_less);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(congress_baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<BillSummary> call = retrofit.create(SummaryService.class).getBillSummary(mBillId+"-"+mSession);
        call.enqueue(new Callback<BillSummary>() {
            @Override
            public void onResponse(Call<BillSummary> call, Response<BillSummary> response) {
                if (response.body().getResults().size()>0) {
                    Result summary = response.body().getResults().get(0);
                    if (summary.getSummaryShort() != null) {
                        mBillSummary.setText(summary.getSummaryShort());
                        mLongSummary = summary.getSummary();
                        mBillSummary.setOnClickListener(v -> {
                            //Toggle Long or short summary
                            if (!mIsExpanded) {
                                mBillSummary.setText(mLongSummary);
                                mIsExpanded = !mIsExpanded;
                                mExpandMoreOrLess.setImageResource(R.drawable.ic_expand_less);
                            } else {
                                mBillSummary.setText(summary.getSummaryShort());
                                mIsExpanded = !mIsExpanded;
                                mExpandMoreOrLess.setImageResource(R.drawable.ic_expand_more);
                            }
                        });
                    } else {
                        mBillSummary.setText("No Summary available");
                        mExpandMoreOrLess.setVisibility(View.GONE);
                    }

                    StringBuilder tags = new StringBuilder();
                    tags.append("Tag(s): ");

                    boolean hasTags = false;
                    for (String keyword : summary.getKeywords()) {
                        tags.append("<u>");
                        tags.append(keyword);
                        tags.append("</u>, ");
                        hasTags = true;
                    }
                    if (hasTags) {
                        tags.replace(tags.lastIndexOf(","), tags.lastIndexOf(" "), "");
                        mBillTags.setText(Html.fromHtml(tags.toString()));
                    } else {
                        mBillTags.setVisibility(View.GONE);
                    }
                }else{

                    mBillSummary.setText("No Summary available");
                    mExpandMoreOrLess.setVisibility(View.GONE);
                    mBillTags.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<BillSummary> call, Throwable t) {

            }
        });
    }
}
