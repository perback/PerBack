package com.perback.perback.apis.ean;


import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface EANApi {

    @GET("/ping")
    void ping (@Query("cid") String cid, @Query("minorRev") String minorRev,
               @Query("apiKey") String apiKey, @Query("sig") String sig,
               @Query("customerUserAgent") String customerUserAgent,
               @Query("customerIpAddress") String customerIpAddress,
               @Query("echo") String echoMessage, Callback<PingResponse> callback);

}
