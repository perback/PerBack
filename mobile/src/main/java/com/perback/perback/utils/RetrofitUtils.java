package com.perback.perback.utils;

import android.content.Context;

import com.google.android.gms.analytics.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perback.perback.apis.directions.DirectionsApi;
import com.perback.perback.apis.directions.DirectionsApiWrapper;
import com.perback.perback.apis.ean.EANApi;
import com.perback.perback.apis.ean.EANApiWrapper;
import com.perback.perback.apis.ean.EANDeserializer;
import com.perback.perback.apis.ean.HotelListResponse;
import com.perback.perback.apis.ean.PingResponse;
import com.perback.perback.apis.ip.IpifyApi;
import com.perback.perback.apis.places.PlacesApi;
import com.perback.perback.apis.places.PlacesApiWrapper;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RetrofitUtils {

    public static final String PLACES_URL = "https://maps.googleapis.com/maps/api/place";
    public static final String DIRECTIONS_URL = "https://maps.googleapis.com/maps/api/directions";
    public static final String EAN_URL = "http://api.ean.com/ean-services/rs/hotel/v3";
    public static final String IPIFY_URL = "https://api.ipify.org";

    private static RestAdapter eanAdapter;
    private static RestAdapter placesAdapter;
    private static RestAdapter directionsAdapter;

    private static RestAdapter getDirectionsAdapter() {
        if(directionsAdapter == null ) {
            directionsAdapter = new RestAdapter.Builder()
                    .setEndpoint(DIRECTIONS_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
        }
        return directionsAdapter;
    }

    private static RestAdapter getPlacesAdapter() {
        if(placesAdapter == null ) {
            placesAdapter = new RestAdapter.Builder()
                    .setEndpoint(PLACES_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
        }
        return placesAdapter;
    }

    private static RestAdapter getEanAdapter() {
        if(eanAdapter==null) {
            Gson gson = new GsonBuilder()
                            .registerTypeAdapter(PingResponse.class, new EANDeserializer<PingResponse>())
                            .registerTypeAdapter(HotelListResponse.class, new EANDeserializer<HotelListResponse>())
                            .create();
            eanAdapter = new RestAdapter.Builder()
                    .setConverter(new GsonConverter(gson))
                    .setEndpoint(EAN_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
        }
        return eanAdapter;
    }

    public static DirectionsApiWrapper getDirectionsApi() {
        return new DirectionsApiWrapper(getDirectionsAdapter().create(DirectionsApi.class));
    }


    public static PlacesApiWrapper getPlacesApi() {
        return new PlacesApiWrapper(getPlacesAdapter().create(PlacesApi.class));
    }

    public static IpifyApi getIpifyApi() {
        return new RestAdapter.Builder()
                .setEndpoint(IPIFY_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build().create(IpifyApi.class);
    }

    public static EANApiWrapper getEanApi(Context context) {
        return new EANApiWrapper(getEanAdapter().create(EANApi.class), context);
    }

}
