package com.joelimyx.politicallocal.bills;

import com.joelimyx.politicallocal.bills.detail.gson.propublica.DetailBill;
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
    /**
     *
     * @param offSet in multiple of 20
     * @return list of recent bill
     */
    @Headers("X-API-Key: rejxuzeqlf7ghtKW2xiQr1TwSAa3jNTSwGCNU24j")
    @GET("/congress/v1/115/{chamber}/bills/{filter}.json")
    Call<RecentBills> getRecentBills(@Path("chamber") String chamber, @Path("filter") String filter, @Query("offset") int offSet);

    @Headers("X-API-Key: rejxuzeqlf7ghtKW2xiQr1TwSAa3jNTSwGCNU24j")
    @GET("/congress/v1/{session}/bills/{bill-id}.json")
    Call<DetailBill> getDetailBill(@Path("bill-id") String bill_id, @Path("session") int session);
}
