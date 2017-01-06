package com.joelimyx.politicallocal.reps.service;

import android.support.annotation.Nullable;

import com.joelimyx.politicallocal.reps.gson.congress.RepsList;
import com.joelimyx.politicallocal.search.gson.BillSearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Joe on 12/17/16.
 */

public interface SunlightService {
    @GET("/legislators/locate")
    Call<RepsList> getLegislatures(@Query("latitude") double latitude, @Query("longitude") double longitude);

    @GET("/legislators?in_office=true")
    Call<RepsList> searchLegislatures(@Query("query") String query);

    @GET("/bills/search?order=last_action_at")
    Call<BillSearch> searchBill(@Query("query") String query, @Nullable @Query("congres") String session);
}
