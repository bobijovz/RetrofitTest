package com.itseasyright.app.retrofittest.api.interfaces;

import com.itseasyright.app.retrofittest.api.models.Feeds;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jovijovs on 02/09/2016.
 */
public interface IFeeds {

    @GET("feeds")
    Call<Feeds> getFeeds();
}
