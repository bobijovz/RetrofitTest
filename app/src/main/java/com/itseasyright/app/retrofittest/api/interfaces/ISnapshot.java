package com.itseasyright.app.retrofittest.api.interfaces;

import com.itseasyright.app.retrofittest.api.models.Snapshots;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jovijovs on 20/09/2016.
 */
public interface ISnapshot {

    @GET("snapshot")//q=41&_token=
    Call<Snapshots> getSnaps(@Query("q") String q, @Query("_token") String token);
}
