package com.perback.perback.utils;

import com.google.android.gms.maps.model.LatLng;
import com.perback.perback.holders.TripPoint;

import java.util.ArrayList;

public interface DrawPathCallback {
    void pathDrawn(LatLng startPoint, LatLng endPoint, ArrayList<TripPoint> waypoints);
}
