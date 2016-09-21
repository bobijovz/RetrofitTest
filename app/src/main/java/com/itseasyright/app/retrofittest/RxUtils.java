package com.itseasyright.app.retrofittest;

import rx.Subscription;

/**
 * Created by jovijovs on 21/09/2016.
 */

public class RxUtils {

    public static void unsubscribeIfNotNull(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
