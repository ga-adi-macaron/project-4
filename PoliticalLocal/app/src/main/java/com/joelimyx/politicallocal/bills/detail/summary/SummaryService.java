package com.joelimyx.politicallocal.bills.detail.summary;

import com.joelimyx.politicallocal.bills.detail.gson.summary.BillSummary;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Joe on 12/20/16.
 */

public interface SummaryService {
    @GET("/bills?fields=summary_short,summary,keywords")
    Call<BillSummary> getBillSummary(@Query("bill_id") String bill_id);
}
