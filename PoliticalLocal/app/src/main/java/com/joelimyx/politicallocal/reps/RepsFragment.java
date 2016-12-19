package com.joelimyx.politicallocal.reps;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.database.RepsSQLHelper;
import com.joelimyx.politicallocal.reps.detail.DetailRepsActivity;

public class RepsFragment extends Fragment
        implements RepsAdapter.OnRepsItemSelectedListener{
    private RepsAdapter mAdapter;
    private static final String TAG = "RepsFragment";
    public static final String ARG_STATE = "state";

    public RepsFragment() {
    }

    public static RepsFragment newInstance(String state) {
        RepsFragment fragment = new RepsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STATE,state);
        fragment.setArguments(args);
        return fragment;
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

        RepsSQLHelper db = RepsSQLHelper.getInstance(getContext());

        mAdapter = new RepsAdapter(db.getRepsList(),getArguments().getString(ARG_STATE),this,getContext());
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void OnRepsItemSelected(String id) {
        Intent intent = new Intent(getActivity(), DetailRepsActivity.class);
        intent.putExtra("id",id);
        getActivity().startActivity(intent);
    }
}
