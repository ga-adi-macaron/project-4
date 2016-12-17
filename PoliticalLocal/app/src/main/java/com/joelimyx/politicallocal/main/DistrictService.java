package com.joelimyx.politicallocal.main;

import com.joelimyx.politicallocal.main.gson.District;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Joe on 12/16/16.
 */

public interface DistrictService {
    @GET("/districts/locate")
    Call<District> getDistrict(@Query("latitude") String latitude, @Query("longitude") String longtitude);
}
