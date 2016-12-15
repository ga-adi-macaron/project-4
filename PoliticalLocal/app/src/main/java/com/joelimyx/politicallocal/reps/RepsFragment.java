package com.joelimyx.politicallocal.reps;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.news.gson.Value;
import com.joelimyx.politicallocal.reps.gson.Reps;
import com.joelimyx.politicallocal.reps.gson.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RepsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RepsFragment extends Fragment
        implements RepsAdapter.OnRepsItemSelectedListener{
    public static final String baseUrl = "https://api.propublica.org/";
    private RepsAdapter mAdapter;

    public RepsFragment() {
    }

    public static RepsFragment newInstance() {
        return new RepsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.reps_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mAdapter = new RepsAdapter(new ArrayList<Result>(),this);
        recyclerView.setAdapter(mAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<Reps> call = retrofit.create(ProbulicaService.class).getRepsList("senate","NY");
        call.enqueue(new Callback<Reps>() {
            @Override
            public void onResponse(Call<Reps> call, Response<Reps> response) {
                List<Result> repsList = response.body().getResults();
                mAdapter.swapData(repsList);
            }

            @Override
            public void onFailure(Call<Reps> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to get Representatives", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void OnRepsItemSelected(String id) {

    }
}
