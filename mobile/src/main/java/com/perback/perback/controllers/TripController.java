package com.perback.perback.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Handler;
import android.util.Log;

import com.perback.perback.dao.Dao;
import com.perback.perback.holders.Trip;
import com.perback.perback.holders.TripPoint;
import com.perback.perback.services.LocationService;

import java.util.ArrayList;
import java.util.Random;

public class TripController {

    public static TripController instance;

    public static final String UPDATE_LOCATION_ACTION = "com.perback.perback.UPDATE_LOCATION";
    public static final String LOCATION = "TripController.LOCATION_PARCEL";

    private static final long DEFAULT_UPDATE_INTERVAL = 3000;
    private static final float DEFAULT_UPDATE_DISTANCE = 50f;

    private Context context;
    private Trip trip;
    private boolean monitoring;
    private long updateInterval;
    private Handler handler;
    private Runnable updateLocationRunnable;
    private BroadcastReceiver locationBroadcastReceiver;
    private Random r;
    private TripPoint lastLocation;
    private TripPointAddedListener listener;
    private long apiTotalDistance = -1;
    private long apiProgressDistance = -1;

    private TripController(final Context context) {
        this.context = context;
        updateInterval = DEFAULT_UPDATE_INTERVAL;
        monitoring = false;
        handler = new Handler();
        r = new Random();

        updateLocationRunnable = new Runnable() {
            @Override
            public void run() {
                context.startService(new Intent(context, LocationService.class));
                handler.postDelayed(this, updateInterval);
            }
        };

        locationBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Location location = intent.getParcelableExtra(LOCATION);
                String msg = "";
                if (location != null) {
                    msg += location.getLatitude();
                    msg += " ";
                    msg += location.getLongitude();

                    if(lastLocation!=null ) {
                        float[] result = new float[1];
                        Location.distanceBetween(lastLocation.getLat(), lastLocation.getLng(), location.getLatitude(), location.getLongitude(), result);
                        if (result[0] > DEFAULT_UPDATE_DISTANCE) {
                            addTripPoint(location);
                        } else {
//                            Log.d("Debug", "Location almost the same, diff: "+result[0]);
                        }
                    } else {
                        addTripPoint(location);
                    }
                } else {
                    msg += "null";
                }
//                Log.d("Debug", "Location received: " + msg);
            }
        };
    }

    public double getProgress() {
        long totalDistance, progressDistance;
        if(apiTotalDistance!=-1 && apiProgressDistance!=-1) {
            totalDistance = apiTotalDistance;
            progressDistance = apiProgressDistance;
        } else {
            totalDistance = (long)getTotalDistance();
            progressDistance = (long) getDistanceTraveled();
        }
        return (double)(progressDistance*100)/totalDistance;
    }

    public long getApiTotalDistance() {
        return apiTotalDistance;
    }

    public void setApiTotalDistance(long apiTotalDistance) {
        this.apiTotalDistance = apiTotalDistance;
    }

    public long getApiProgressDistance() {
        return apiProgressDistance;
    }

    public void setApiProgressDistance(long apiProgressDistance) {
        this.apiProgressDistance = apiProgressDistance;
    }

    private void addTripPoint(Location location) {
        TripPoint tripPoint = new TripPoint(location.getLatitude(), location.getLongitude());
        lastLocation = tripPoint;
        if (listener != null)
            listener.onNewTripPoint(tripPoint);
        trip.addTripPoint(tripPoint);
        Log.d("Debug", "Trip point added");
    }

    public static void init(Context context) {
        instance = new TripController(context);
    }


    public static TripController getInstance() {
        return instance;
    }

    public void addCheckPoint(TripPoint tripPoint) {
        tripPoint.setCheckpoint(true);
        trip.addTripPoint(tripPoint);
    }

    public ArrayList<TripPoint> getCheckpoints() {
        return getCheckpoints(trip);
    }

    public void unregisterListener() {
        listener = null;
    }

    public void registerListener(TripPointAddedListener listener) {
        this.listener = listener;
    }

    public ArrayList<TripPoint> getCheckpoints(Trip trip) {
        ArrayList<TripPoint> checkpoints = new ArrayList<>();
        if (isMonitoring()) {
            ArrayList<TripPoint> tripPoints = getTrip().getTripPoints();
            for (int i = 0; i < tripPoints.size(); i++) {
                if (tripPoints.get(i).isCheckpoint())
                    checkpoints.add(tripPoints.get(i));
            }
        }
        return checkpoints;
    }

    public void startTrip(Trip trip) {
        this.trip = trip;
        trip.setStartTime(System.currentTimeMillis());
        monitoring = true;
        handler.postDelayed(updateLocationRunnable, updateInterval);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TripController.UPDATE_LOCATION_ACTION);
        context.registerReceiver(locationBroadcastReceiver, intentFilter);
    }

    public void endTrip() {
        monitoring = false;
        trip.setEndTime(System.currentTimeMillis());
        Dao.getInstance().saveTrip(trip);
        handler.removeCallbacks(updateLocationRunnable);
        context.unregisterReceiver(locationBroadcastReceiver);
    }

    public float getTotalDistance() {
        float[] totalDistance = new float[1];
        Location.distanceBetween(trip.getStartLocationLat(), trip.getStartLocationLng(), trip.getEndLocationLat(), trip.getEndLocationLng(), totalDistance);
        return totalDistance[0];
    }

    public float getDistanceTraveled() {
        float[] traveledDistance = new float[1];
        Location.distanceBetween(trip.getStartLocationLat(), trip.getStartLocationLng(), lastLocation.getLat(), lastLocation.getLng(), traveledDistance);
        return traveledDistance[0];
    }

    public float getDistanceLeft() {
        float totalDistance = getTotalDistance();
        float distanceTraveled = getDistanceTraveled();
        if(apiTotalDistance!=-1)
            totalDistance = apiTotalDistance;
        if(apiProgressDistance!=-1)
            distanceTraveled = apiProgressDistance;
        return totalDistance - distanceTraveled;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - trip.getStartTime();
    }

    public TripPoint getLastLocation() {
        return lastLocation;
    }

    public Trip getTrip() {
        return trip;
    }

    public boolean isMonitoring() {
        return monitoring;
    }
}
