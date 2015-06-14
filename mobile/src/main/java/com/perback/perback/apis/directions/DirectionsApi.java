package com.perback.perback.apis.directions;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface DirectionsApi {

    @GET("/json")
    void getDirections(@Query("key") String apiKey,
                       @Query("origin") String origin, @Query("destination") String destination,
                       Callback<DirectionsResponse> callback);

}
