package com.perback.perback.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.perback.perback.controllers.TripController;
import com.perback.perback.dao.Dao;
import com.perback.perback.holders.TripPoint;

public class LocationService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static GoogleApiClient googleApiClient;
    private boolean requestPending;

    public LocationService() {
        super("Perback location service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    public void init() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        if (!googleApiClient.isConnected())
            googleApiClient.connect();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        init();
        if (googleApiClient.isConnected()) {
            sendResponse();
        } else {
            requestPending = true;
        }
    }

    private void sendResponse() {
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        TripPoint tripPoint = new TripPoint(location.getLatitude(), location.getLongitude());
        Dao dao = Dao.getInstance();
        if (dao != null)
            dao.writeLocation(tripPoint);
        Intent response = new Intent(TripController.UPDATE_LOCATION_ACTION);
        response.putExtra(TripController.LOCATION, location);
        sendBroadcast(response);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e("Debug", "Client connected");
        if (requestPending) {
            requestPending = false;
            sendResponse();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("Debug", "Connection suspended " + i);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("Debug", "Connection failed: " + connectionResult.getErrorCode());
    }
}
