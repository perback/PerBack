package com.perback.perback.apis.ean;


import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface EANApi {

    @GET("/ping")
    void ping (@Query("cid") String cid, @Query("minorRev") String minorRev,
               @Query("apiKey") String apiKey, @Query("sig") String sig,
               @Query("customerUserAgent") String customerUserAgent,
               @Query("echo") String echoMessage,
               Callback<PingResponse> callback);


    @GET("/list")
    void getNearbyHotels(@Query("cid") String cid, @Query("minorRev") String minorRev,
                         @Query("apiKey") String apiKey,
                         @Query("sig") String sig,
                         @Query("latitude") String latitude, @Query("longitude") String longitude,
                         @Query("searchRadius") Integer searchRadius,
                         @Query("searchRadiusUnit") String searchRadiusUnit,
                         @Query("propertyCategory") String category,
                         @Query("minStarRating") Double minStarRating,
                         @Query("maxStarRating") Double maxStarRating,
                         @Query("sort") String sort,
                         Callback<HotelListResponse> callback);

}
