package com.itseasyright.app.retrofittest.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by jovijovs on 01/09/2016.
 */
public class BusProvider {
    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);
    public static Bus getInstance() {
        return BUS;
    }
    private BusProvider() {
    }
}
