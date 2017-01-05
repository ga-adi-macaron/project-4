package com.joelimyx.politicallocal.bills.detail.sponsors;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.bills.BillFragment;
import com.joelimyx.politicallocal.bills.detail.sponsors.detail.DetailSponsorService;
import com.joelimyx.politicallocal.bills.detail.sponsors.detail.GsonDetailSponsor;
import com.joelimyx.politicallocal.bills.detail.sponsors.detail.Result;
import com.joelimyx.politicallocal.bills.detail.sponsors.detail.Sponsor;
import com.joelimyx.politicallocal.bills.detail.sponsors.gson.Cosponsor;
import com.joelimyx.politicallocal.bills.detail.sponsors.gson.GsonSponsorsList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SponsorsFragment extends Fragment {
    private static final String ARG_BILL_ID = "bill id";
    public static final String congress_Url = "https://congress.api.sunlightfoundation.com/";

    private static final String TAG = "SponsorsFragment";
    private TextView mMainSponsorName,mMainSponsorDistrictRank, mCosponsorPlaceHolder;
    private CircleImageView mMainSponsorPortrait;
    private SponsorsAdapter mAdapter;
    private String mBillId;


    public SponsorsFragment() {
    }

    public static SponsorsFragment newInstance(String billId) {
        SponsorsFragment fragment = new SponsorsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BILL_ID, billId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBillId = getArguments().getString(ARG_BILL_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sponsors, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerview = (RecyclerView) view.findViewById(R.id.sponsor_recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        List<Sponsor> mSponsorList = new ArrayList<>(100);
        mAdapter = new SponsorsAdapter(mSponsorList,getContext());
        recyclerview.setAdapter(mAdapter);

        mMainSponsorName = (TextView) view.findViewById(R.id.reps_name);
        mMainSponsorDistrictRank= (TextView) view.findViewById(R.id.reps_district_rank);
        mCosponsorPlaceHolder= (TextView) view.findViewById(R.id.sponsor_cosponsor_placeholder);
        mMainSponsorPortrait = (CircleImageView) view.findViewById(R.id.reps_portrait);

        //Grab individual sponsor basic info
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BillFragment.propublica_baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<GsonSponsorsList> call = retrofit.create(SponsorsService.class).getSponsors(mBillId);
        call.enqueue(new Callback<GsonSponsorsList>() {
            @Override
            public void onResponse(Call<GsonSponsorsList> call, Response<GsonSponsorsList> response) {

                //Major sponsor
                String majorSponsor = response.body().getResults().get(0).getSponsorId();
                setMainSponsorLayout(majorSponsor);

                //CoSponsors
                List<Cosponsor> cosponsors = response.body().getResults().get(0).getCosponsors();
                if (cosponsors != null && cosponsors.size()!=0) {
                    for (Cosponsor current : cosponsors) {
                        addSponsorToList(current.getCosponsorId());
                    }
                }else{
                    mCosponsorPlaceHolder.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GsonSponsorsList> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to get Sponsor List", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * @param bioId unique bio ID from Congressional Biographical Directory
     */
    private void addSponsorToList(String bioId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(congress_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<GsonDetailSponsor> call = retrofit.create(DetailSponsorService.class).getSponsorDetail(bioId);
        call.enqueue(new Callback<GsonDetailSponsor>() {
            @Override
            public void onResponse(Call<GsonDetailSponsor> call, Response<GsonDetailSponsor> response) {

                if(response.body().getResults().size()!=0) {
                    Result result = response.body().getResults().get(0);

                    //Name append
                    String name = result.getTitle() + ". " + result.getFirstName();
                    if (result.getMiddleName() != null) {
                        name += " " + result.getMiddleName();
                    }
                    name += " "+result.getLastName();

                    //Determine whether it is senator or representatives
                    if (result.getDistrict() != null) {
                        mAdapter.addSponsorToList(new Sponsor(
                                        result.getBioguideId(),
                                        name,
                                        result.getState(),
                                        result.getParty(),
                                        result.getChamber(),
                                        result.getDistrict()));
                    } else {
                        mAdapter.addSponsorToList(new Sponsor(
                                        result.getBioguideId(),
                                        name,
                                        result.getState(),
                                        result.getParty(),
                                        result.getChamber(),
                                        result.getSenateClass()));
                    }
                }
            }

            @Override
            public void onFailure(Call<GsonDetailSponsor> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to get Sponsor Detail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMainSponsorLayout(String major){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(congress_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<GsonDetailSponsor> call = retrofit.create(DetailSponsorService.class).getSponsorDetail(major);
        call.enqueue(new Callback<GsonDetailSponsor>() {
            @Override
            public void onResponse(Call<GsonDetailSponsor> call, Response<GsonDetailSponsor> response) {

                if(response.body().getResults().size()!=0) {
                    Result result = response.body().getResults().get(0);

                    //Name append
                    String name = result.getTitle() + ". " + result.getFirstName();
                    if (result.getMiddleName() != null) {
                        name += " " + result.getMiddleName();
                    }
                    name += " "+result.getLastName();

                    //Determine whether it is senator or representatives
                    if (result.getDistrict() != null) {

                        mMainSponsorName.setText(name+" ("+result.getParty()+"-"+result.getState()+")");

                        Picasso.with(getContext())
                                .load("https://theunitedstates.io/images/congress/original/"+result.getBioguideId()+".jpg")
                                .placeholder(R.drawable.ic_reps)
                                .fit()
                                .into(mMainSponsorPortrait);

                        if (result.getDistrict()==0){
                            mMainSponsorDistrictRank.setText(result.getState()+" At-Large District");
                        }else{
                            mMainSponsorDistrictRank.setText(result.getState()+" District "+result.getDistrict());
                        }

                    } else {
                        mMainSponsorName.setText(name+" ("+result.getParty()+"-"+result.getState()+")");

                        Picasso.with(getContext())
                                .load("https://theunitedstates.io/images/congress/original/"+result.getBioguideId()+".jpg")
                                .placeholder(R.drawable.ic_reps)
                                .fit()
                                .into(mMainSponsorPortrait);

                        mMainSponsorDistrictRank.setText("Senator Class "+result.getSenateClass());

                    }
                }
            }

            @Override
            public void onFailure(Call<GsonDetailSponsor> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to get Sponsor Detail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
