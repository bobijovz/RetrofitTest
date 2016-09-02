package com.itseasyright.app.retrofittest.manager;

import android.content.Context;
import android.util.Log;

import com.itseasyright.app.retrofittest.api.AppClient;
import com.itseasyright.app.retrofittest.api.interfaces.IFeeds;
import com.itseasyright.app.retrofittest.api.models.Feeds;
import com.itseasyright.app.retrofittest.api.services.BaseServices;
import com.itseasyright.app.retrofittest.api.services.GitHubFeedServices;
import com.itseasyright.app.retrofittest.event.GetGithubFeeds;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jovijovs on 01/09/2016.
 */
public class AppManager {
    private Context context;
    private Bus bus;
    public AppClient appClient;

    public AppManager(Context context, Bus bus) {
        this.context = context;
        this.bus = bus;
        appClient = AppClient.getClient();

        new BaseServices(context,bus,appClient);
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

    /*@Subscribe
    public void onGetSnapshotEvent(final GetSnapshotsRequest request) {
        appClient.createService(ISnapshots.class)
                .getSnapshots(request.getQ(), request.getToken())
                .enqueue(new Callback<GetSnapshotsResponse>() {
                    @Override
                    public void onResponse(Call<GetSnapshotsResponse> call, Response<GetSnapshotsResponse> response) {
                        bus.post(response.body());
                       /// Log.d("Response", String.valueOf(response.errorBody()));
                    }

                    @Override
                    public void onFailure(Call<GetSnapshotsResponse> call, Throwable t) {
                        Log.e("ErrorSnaps",t.getMessage());
                    }
                });
    }

    @Subscribe
    public void onGetTokenEvent(GetTokenRequest request){
        appClient.createService(ISnapshots.class)
                .getToken()
                .enqueue(new Callback<GetTokenResponse>() {
                    @Override
                    public void onResponse(Call<GetTokenResponse> call, Response<GetTokenResponse> response) {
                        Log.d("", String.valueOf(response));
                    }

                    @Override
                    public void onFailure(Call<GetTokenResponse> call, Throwable t) {
                        Log.d("", String.valueOf(t.getMessage()));

                    }
                });
    }*/
}
