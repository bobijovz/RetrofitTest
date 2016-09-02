package com.itseasyright.app.retrofittest.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jovijovs on 01/09/2016.
 */
public class AppClient {
    public static final String API_BASE_URL = "https://api.github.com";
    private static AppClient appClient;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static HttpLoggingInterceptor interceptor;
    private Retrofit retrofit;

    public static AppClient getClient() {
        if (appClient == null)
            appClient = new AppClient();
        return appClient;
    }

    public Retrofit retrofit() {
        return this.retrofit;
    }

    private AppClient() {
        interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
    }

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
