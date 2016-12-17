package com.joelimyx.politicallocal.reps.service;

import com.joelimyx.politicallocal.reps.detail.gson.DetailInfo;
import com.joelimyx.politicallocal.reps.gson.opensecret.ListOfLegislator;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Joe on 12/16/16.
 */

public interface OpenSecretService {

    @GET("/api/?method=getLegislators&apikey=a8aca8a470037691afbc0a3f20e21299&output=json")
    Call<ListOfLegislator> getLegislators(@Query("id") String district);

    @GET("/api/?method=getLegislators&apikey=a8aca8a470037691afbc0a3f20e21299&output=json")
    Call<DetailInfo> getDetail(@Query("id") String id);
}
