package com.joelimyx.politicallocal.reps.service;

import com.twitter.sdk.android.core.GuestSession;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Joe on 12/19/16.
 */

public class TwitterIdClient extends TwitterApiClient {
    public TwitterIdClient(OkHttpClient client) {
        super(client);
    }

    public IdService getIdService() {
        return getService(IdService.class);
    }
    public interface IdService {
        @GET("/1.1/users/show.json")
        Call<User> getId(@Query("screen_name") String screenName);
    }
}

