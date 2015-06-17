package com.perback.perback.apis.directions;

import android.util.Log;

import com.perback.perback.holders.TripPoint;

import java.util.ArrayList;

import retrofit.Callback;

public class DirectionsApiWrapper {

    private static final String API_KEY = "AIzaSyAKwIlqJeix4QKlArC5vkY0rZFmxfNVpHo";

    protected DirectionsApi directionsApi;

    public DirectionsApiWrapper(DirectionsApi directionsApi) {
        this.directionsApi = directionsApi;
    }

    public void getDirections(String origin, String destination, Callback<DirectionsResponse> callback) {
        directionsApi.getDirections(API_KEY, origin, destination, null, callback);
    }

    public void getDirections(String origin, String destination, ArrayList<TripPoint> waypoints, Callback<DirectionsResponse> callback) {
        String waypointsStr = null;
        if (waypoints != null) {
            ArrayList<TripPoint> waypoints8 = getIntermediate8Points(waypoints);
            waypointsStr = "";
            TripPoint tp;
            for (int i = 0; i < waypoints8.size(); i++) {
                tp = waypoints8.get(i);
                if (i != 0)
                    waypointsStr += "|";
                waypointsStr += "via:" + tp.getLat() + "," + tp.getLng();
            }
        }
        directionsApi.getDirections(API_KEY, origin, destination, waypointsStr, callback);
    }

    private ArrayList<TripPoint> getIntermediate8Points(ArrayList<TripPoint> waypoints) {
        ArrayList<TripPoint> intermediate = new ArrayList<>();
        int step = waypoints.size()/8+1;
        for (int i = 0; i < waypoints.size(); i+=step) {
            if(i+step>=waypoints.size())
                intermediate.add(waypoints.get(waypoints.size()-1));
            else
                intermediate.add(waypoints.get(i));
        }
        return intermediate;
    }
}
