package com.perback.perback.apis.places;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface PlacesApi {

    /**
     * @param location must be a string of the following format: lat,lng
     * */
    @GET("/nearbysearch/json")
    void nearbySearch(@Query("key") String apiKey,
                      @Query("location") String location, @Query("radius") Integer radius,
                      @Query("name") String name,
                      @Query("types") String types,
                      Callback<BasePlacesResponse<PlacesResponse>> callback);

}
