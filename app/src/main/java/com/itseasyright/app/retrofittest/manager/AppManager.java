package com.itseasyright.app.retrofittest.manager;

import android.content.Context;
import android.util.Log;

import com.itseasyright.app.retrofittest.api.AppClient;
import com.itseasyright.app.retrofittest.api.interfaces.ISnapshot;
import com.itseasyright.app.retrofittest.api.models.Snapshots;
import com.itseasyright.app.retrofittest.event.GetSnapshots;
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
        appClient = AppClient.getClient(context);

        //new WebServices(context,bus,appClient);
    }


    @Subscribe
    public void getSnapshots(GetSnapshots request) {
        appClient.createService(ISnapshot.class).getSnaps(request.getQ(), request.getToken())
                .enqueue(new Callback<Snapshots>() {
                    @Override
                    public void onResponse(Call<Snapshots> call, Response<Snapshots> response) {
                        bus.post(response.body());
                    }

                    @Override
                    public void onFailure(Call<Snapshots> call, Throwable t) {
                        Log.d("Error",t.getMessage());
                    }
                });
    }

    /*@Subscribe
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
