package com.joelimyx.politicallocal.bills.detail.sponsors.detail;

import com.joelimyx.politicallocal.reps.gson.congress.RepsList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Joe on 12/26/16.
 */

public interface DetailSponsorService {
    @GET("/legislators")
    Call<GsonDetailSponsor> getSponsorDetail(@Query("bioguide_id") String bio_id);
}
