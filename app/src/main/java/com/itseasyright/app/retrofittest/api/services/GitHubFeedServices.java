package com.itseasyright.app.retrofittest.api.services;

import android.content.Context;
import android.util.Log;

import com.itseasyright.app.retrofittest.api.AppClient;
import com.itseasyright.app.retrofittest.api.interfaces.IFeeds;
import com.itseasyright.app.retrofittest.api.models.Feeds;
import com.itseasyright.app.retrofittest.event.GetGithubFeeds;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jovijovs on 02/09/2016.
 */
public class GitHubFeedServices extends BaseServices {

    public GitHubFeedServices(Context context, Bus bus, AppClient appClient) {
        super(context, bus, appClient);

    }


    @Subscribe
    public void getGithubFeeds(GetGithubFeeds request){
        appClient.createService(IFeeds.class).getFeeds()
                .enqueue(new Callback<Feeds>() {
                    @Override
                    public void onResponse(Call<Feeds> call, Response<Feeds> response) {
                        bus.post(response.body());
                    }

                    @Override
                    public void onFailure(Call<Feeds> call, Throwable t) {
                        Log.d("Error",t.getMessage());
                    }
                });
    }


}
