package com.perback.perback.apis.places;

import com.perback.perback.dao.Dao;
import com.perback.perback.holders.TripPoint;

import java.util.ArrayList;

import retrofit.Callback;

public class PlacesApiWrapper {

    public static final String API_KEY = "AIzaSyAKwIlqJeix4QKlArC5vkY0rZFmxfNVpHo";

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

    public void nearbySearch(String name, ArrayList<String> types, Callback<BaseResponse<PlacesResponse>> callback) {
        TripPoint locationPoint = Dao.getInstance().readLocation();
        String location = "" + locationPoint.getLat() + "," + locationPoint.getLng();
        Integer radius = Dao.getInstance().readDistanceProximityPosition();
        if (radius == 0)
            radius = 500;
        placesApi.nearbySearch(API_KEY, location, radius, name, getTypesStr(types), callback);
    }

    public String getTypesStr(ArrayList<String> input) {
        String types = null;
        if(input!=null && input.size()>0) {
            types = "";
            for(int i=0; i<input.size(); i++) {
                if(i!=0)
                    types+="|";
                types+=PlacesTypes.getId(input.get(i));
            }
        }
        return types;
    }

}
