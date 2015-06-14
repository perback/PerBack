package com.perback.perback.apis.places;

import com.perback.perback.dao.Dao;
import com.perback.perback.holders.TripPoint;

import retrofit.Callback;

public class PlacesApiWrapper {

    private static final String API_KEY = "AIzaSyAKwIlqJeix4QKlArC5vkY0rZFmxfNVpHo";

    private PlacesApi placesApi;

    public PlacesApiWrapper(PlacesApi placesApi) {
        this.placesApi = placesApi;
    }

    public void getPlaceDetails(String placeId, Callback<BaseResponse<PlaceDetailsResponse>> callback) {
        placesApi.getPlaceDetails(API_KEY, placeId, callback);
    }

    public void nearbySearch(Callback<BaseResponse<PlacesResponse>> callback) {
        nearbySearch(null, null, callback);
    }

    public void nearbySearch(String name, String types, Callback<BaseResponse<PlacesResponse>> callback) {
        TripPoint locationPoint = Dao.getInstance().readLocation();
        String location = "" + locationPoint.getLat() + "," + locationPoint.getLng();
        Integer radius = Dao.getInstance().readPlacesRadius();
        if (radius == null)
            radius = 500;
        placesApi.nearbySearch(API_KEY, location, radius, name, types, callback);
    }

}
