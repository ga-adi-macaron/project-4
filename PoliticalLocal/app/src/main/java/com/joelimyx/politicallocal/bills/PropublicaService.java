package com.joelimyx.politicallocal.bills;

import android.support.annotation.Nullable;

import com.joelimyx.politicallocal.bills.gson.RecentBills;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Joe on 12/20/16.
 */

public interface PropublicaService {
    @Headers("X-API-Key: rejxuzeqlf7ghtKW2xiQr1TwSAa3jNTSwGCNU24j")
    @GET("/congress/v1/114/house/bills/passed.json")
    Call<RecentBills> getRecentBills(@Query("offset") int offSet);
//
//    @Headers("X-API-Key: rejxuzeqlf7ghtKW2xiQr1TwSAa3jNTSwGCNU24j")
//    @GET("/congress/v1/house/bills/{bill-id}.json")
//    Call<> getDetailBill(@Path("bill-id") String bill_id);
}
