package com.joelimyx.politicallocal.reps.service;

import com.joelimyx.politicallocal.reps.gson.probulica.Reps;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by Joe on 12/15/16.
 */

public interface ProbulicaService {
    @Headers("X-API-Key: rejxuzeqlf7ghtKW2xiQr1TwSAa3jNTSwGCNU24j")
    @GET("/congress/v1/members/{chamber}/{state}/current.json")
    Call<Reps> getRepsList(@Path("chamber") String chamber, @Path("state") String State);
}
