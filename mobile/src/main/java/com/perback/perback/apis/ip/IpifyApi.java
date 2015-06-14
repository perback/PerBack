package com.perback.perback.apis.ip;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;

public interface IpifyApi {

    @GET("/")
    void getIp(Callback<Response> responseCallback);

}
