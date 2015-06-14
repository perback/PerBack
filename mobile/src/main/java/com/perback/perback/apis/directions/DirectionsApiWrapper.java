package com.perback.perback.apis.directions;

import retrofit.Callback;

public class DirectionsApiWrapper {

    private static final String API_KEY = "AIzaSyAKwIlqJeix4QKlArC5vkY0rZFmxfNVpHo";

    protected DirectionsApi directionsApi;

    public DirectionsApiWrapper(DirectionsApi directionsApi) {
        this.directionsApi = directionsApi;
    }

    public void getDirections(String origin, String destination, Callback<DirectionsResponse> callback) {
        directionsApi.getDirections(API_KEY, origin, destination, callback);
    }




}
