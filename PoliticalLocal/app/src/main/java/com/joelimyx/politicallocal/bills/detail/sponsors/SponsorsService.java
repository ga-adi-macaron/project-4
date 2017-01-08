package com.joelimyx.politicallocal.bills.detail.sponsors;

import com.joelimyx.politicallocal.bills.detail.sponsors.gson.GsonSponsorsList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by Joe on 12/26/16.
 */

public interface SponsorsService {

    @Headers("X-API-Key: rejxuzeqlf7ghtKW2xiQr1TwSAa3jNTSwGCNU24j")
    @GET("/congress/v1/{session}/bills/{bill-id}/cosponsors.json")
    Call<GsonSponsorsList> getSponsors(@Path("bill-id") String bill_id, @Path("session") String session);
}
