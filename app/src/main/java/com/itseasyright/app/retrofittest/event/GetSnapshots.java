package com.itseasyright.app.retrofittest.event;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jovijovs on 20/09/2016.
 */
public class GetSnapshots {
    private String q;
    @SerializedName("_token")
    private String token;

    public GetSnapshots(String q,String token){
        this.q = q;
        this.token = token;
    }

    public String getQ() {
        return q;
    }

    public String getToken() {
        return token;
    }
}
