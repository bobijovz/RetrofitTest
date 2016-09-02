package com.itseasyright.app.retrofittest.application;

import android.app.Application;

import com.itseasyright.app.retrofittest.bus.BusProvider;
import com.itseasyright.app.retrofittest.manager.AppManager;
import com.squareup.otto.Bus;

/**
 * Created by jovijovs on 01/09/2016.
 */
public class MainApplication extends Application {
    private AppManager appManager;
    public Bus bus = BusProvider.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        appManager = new AppManager(this, bus);
        bus.register(appManager);
        bus.register(this);
    }
}
