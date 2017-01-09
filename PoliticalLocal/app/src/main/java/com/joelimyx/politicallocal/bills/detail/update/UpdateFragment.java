package com.joelimyx.politicallocal.bills.detail.update;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.bills.BillFragment;
import com.joelimyx.politicallocal.bills.PropublicaService;
import com.joelimyx.politicallocal.bills.detail.gson.propublica.Action;
import com.joelimyx.politicallocal.bills.detail.gson.propublica.DetailBill;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateFragment extends Fragment {
    private static final String ARG_BILL_ID = "bill id";
    private static final String ARG_SESSION = "session";

    private String mBillId, mSession;


    public UpdateFragment() {
    }

    public static UpdateFragment newInstance(String billId, String session) {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BILL_ID, billId);
        args.putString(ARG_SESSION, session);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBillId = getArguments().getString(ARG_BILL_ID);
            mSession = getArguments().getString(ARG_SESSION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerview = (RecyclerView) view.findViewById(R.id.update_recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BillFragment.propublica_baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<DetailBill> call = retrofit.create(PropublicaService.class).getDetailBill(mBillId, Integer.valueOf(mSession));
        call.enqueue(new Callback<DetailBill>() {
            @Override
            public void onResponse(Call<DetailBill> call, Response<DetailBill> response) {
                String latestUpdate = response.body().getResults().get(0).getLatest_major_action();
                List<Action> temp = response.body().getResults().get(0).getActions();
                //Check to see if the latest update matches the top of the list
                if (!latestUpdate.equalsIgnoreCase(temp.get(0).getDescription())){
                    temp.add(0,new Action(response.body().getResults().get(0).getLatest_major_action_date(),latestUpdate));
                }
                recyclerview.setAdapter(new UpdateAdapter(temp));
            }

            @Override
            public void onFailure(Call<DetailBill> call, Throwable t) {

            }
        });
    }
}
