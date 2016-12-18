package com.joelimyx.politicallocal.reps.service;

import com.joelimyx.politicallocal.reps.gson.congress.RepsList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Joe on 12/17/16.
 */

public interface CongressService {
    @GET("/legislators/locate")
    Call<RepsList> getLegislatures(@Query("latitude") double latitude, @Query("longitude") double longitude);
}
