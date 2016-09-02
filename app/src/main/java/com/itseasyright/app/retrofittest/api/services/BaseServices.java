package com.itseasyright.app.retrofittest.api.services;

import android.content.Context;

import com.itseasyright.app.retrofittest.api.AppClient;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by jovijovs on 02/09/2016.
 */
public class BaseServices {
    public Context context;
    public Bus bus;
    public AppClient appClient;

    public BaseServices(Context context, Bus bus, AppClient appClient){
        this.context = context;
        this.bus = bus;
        this.appClient = appClient;
    }

    /*public <S> S parseError(Class<S> response) {
        Converter<ResponseBody, S> converter =
                appClient.retrofit()
                        .responseBodyConverter(response.getClass(), new Annotation[0]);

        S error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new S();
        }

        return error;
    }*/
}
