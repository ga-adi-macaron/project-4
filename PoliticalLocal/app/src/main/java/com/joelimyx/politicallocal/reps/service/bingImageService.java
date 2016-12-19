package com.joelimyx.politicallocal.reps.service;

import com.joelimyx.politicallocal.news.gson.News;
import com.joelimyx.politicallocal.reps.gson.bingsearch.Portrait;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Joe on 12/18/16.
 */

public interface BingImageService {
    @Headers("Ocp-Apim-Subscription-Key: ea44b2c978244c1dbda2c597f3c7edf3")
    @GET("/bing/v5.0/images/search")
    Call<Portrait> getImage(@Query("q") String query, @Query("count") int count);

}
