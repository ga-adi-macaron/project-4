package com.joelimyx.politicallocal.bills.detail;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.bills.BillFragment;
import com.joelimyx.politicallocal.bills.PropublicaService;
import com.joelimyx.politicallocal.bills.detail.gson.DetailBill;
import com.joelimyx.politicallocal.bills.detail.gson.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailBillActivity extends AppCompatActivity {

    private Result mDetailBill;
    private TextView mDetailBillNumber,mDetailBillTitle,mDetailBillSponsor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDetailBillNumber = (TextView) findViewById(R.id.detail_bill_number);
        mDetailBillTitle= (TextView) findViewById(R.id.detail_bill_title);
        mDetailBillSponsor= (TextView) findViewById(R.id.detail_bill_sponsor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BillFragment.propublica_baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<DetailBill> call = retrofit.create(PropublicaService.class).getDetailBill(getIntent().getStringExtra("id"));
        call.enqueue(new Callback<DetailBill>() {
            @Override
            public void onResponse(Call<DetailBill> call, Response<DetailBill> response) {
                if (call.isExecuted()) {
                    mDetailBill = response.body().getResults().get(0);
                    mDetailBillNumber.setText(mDetailBill.getBill());
                    mDetailBillTitle.setText(mDetailBill.getTitle());
                    mDetailBillSponsor.setText(mDetailBill.getSponsor());
                }
            }

            @Override
            public void onFailure(Call<DetailBill> call, Throwable t) {

            }
        });
    }

}
