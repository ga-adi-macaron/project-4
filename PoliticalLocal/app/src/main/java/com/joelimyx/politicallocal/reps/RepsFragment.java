package com.joelimyx.politicallocal.reps;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.database.RepsSQLHelper;
import com.joelimyx.politicallocal.reps.detail.DetailRepsActivity;
import com.joelimyx.politicallocal.reps.gson.opensecret.ListOfLegislator;
import com.joelimyx.politicallocal.reps.gson.opensecret.Legislator;
import com.joelimyx.politicallocal.reps.service.OpenSecretService;

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
    private RepsAdapter mAdapter;
    private static final String TAG = "RepsFragment";

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

        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.district_file),Context.MODE_PRIVATE);
        RepsSQLHelper db = RepsSQLHelper.getInstance(getContext());

        mAdapter = new RepsAdapter(db.getRepsList(),preferences.getString(getString(R.string.state),null),this);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void OnRepsItemSelected(String id) {
        Intent intent = new Intent(getActivity(), DetailRepsActivity.class);
        intent.putExtra("id",id);
        getActivity().startActivity(intent);
    }
}
