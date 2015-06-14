package com.perback.perback.apis.places;

import com.perback.perback.dao.Dao;
import com.perback.perback.holders.TripPoint;

import retrofit.Callback;
import retrofit.client.Response;

public class PlacesApiWrapper {

    private static final String API_KEY = "AIzaSyAKwIlqJeix4QKlArC5vkY0rZFmxfNVpHo";

    private PlacesApi placesApi;

    public PlacesApiWrapper(PlacesApi placesApi) {
        this.placesApi = placesApi;
    }

    public void testCall(Callback<BasePlacesResponse<PlacesResponse>> callback) {
        TripPoint locationPoint = Dao.getInstance().readLocation();
        String location=""+locationPoint.getLat()+","+locationPoint.getLng();
        placesApi.nearbySearch(API_KEY, location, 500, null, "bank", callback);
    }
}
