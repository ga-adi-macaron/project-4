package com.joelimyx.politicallocal.news;

import com.joelimyx.politicallocal.news.Gson.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Joe on 12/14/16.
 */

public interface NewsService {
    @Headers("Ocp-Apim-Subscription-Key: ea44b2c978244c1dbda2c597f3c7edf3")
    @GET("/bing/v5.0/news/search")
    Call<News> getNews(@Query("q") String query, @Query("count") int count);
}
