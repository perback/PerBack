package com.perback.perback.activities;

import android.content.Intent;
import android.location.Location;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.perback.perback.R;
import com.perback.perback.apis.directions.DirectionsResponse;
import com.perback.perback.apis.directions.DirectionsUtils;
import com.perback.perback.apis.directions.Leg;
import com.perback.perback.apis.directions.Step;
import com.perback.perback.controllers.TripController;
import com.perback.perback.holders.Trip;
import com.perback.perback.holders.TripPoint;
import com.perback.perback.utils.DrawPathCallback;
import com.perback.perback.utils.RetrofitUtils;
import com.perback.perback.x_base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TripDetailsActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TRIP = "Trip";
    protected Trip trip;
    protected GoogleMap map;
    protected boolean cameraUpdated = false;


    @Override
    protected int getLayoutResId() {
        return R.layout.trip_details_activity;
    }

    @Override
    protected void init() {
        super.init();

        trip = (Trip) getIntent().getSerializableExtra(TRIP);
        String tripName = "My Trip";
        if (trip != null && !TextUtils.isEmpty(trip.getTripName()))
            tripName = trip.getTripName();


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(tripName);
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.app_green));

        MapFragment mapFragment = MapFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.map_container, mapFragment, "Trip details map").commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void setData() {
        super.setData();


    }

    public static void launch(BaseActivity activity, Trip trip) {
        Intent intent = new Intent(activity, TripDetailsActivity.class);
        intent.putExtra(TRIP, trip);
        activity.startActivity(intent);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (!cameraUpdated) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                    cameraUpdated = true;
                }
            }
        });

        LatLng startPoint = new LatLng(trip.getStartLocationLat(), trip.getStartLocationLng());
        LatLng endPoint = new LatLng(trip.getEndLocationLat(), trip.getEndLocationLng());
        drawPath(false, startPoint, endPoint, trip.getTripPoints(), getResources().getColor(R.color.blue_path), new DrawPathCallback() {

            @Override
            public void pathDrawn(LatLng startPoint, LatLng endPoint, ArrayList<TripPoint> waypoints) {

            }
        });
    }


    public void drawPath(final boolean tripProgress, final LatLng startPoint, final LatLng endPoint, final ArrayList<TripPoint> waypoints, final int color, final DrawPathCallback callback) {
        RetrofitUtils.getDirectionsApi().getDirections("" + startPoint.latitude + "," + startPoint.longitude,
                endPoint.latitude + "," + endPoint.longitude, null, new Callback<DirectionsResponse>() {
                    @Override
                    public void success(final DirectionsResponse directionsResponse, Response response) {
                        if (directionsResponse.isSuccess()) {
                            if (directionsResponse.getRoutes().size() > 0 && directionsResponse.getRoutes().get(0).getLegs().size() > 0) {
                                Leg leg = directionsResponse.getRoutes().get(0).getLegs().get(0);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ArrayList<Step> steps = directionsResponse.getRoutes().get(0).getLegs().get(0).getSteps();
                                        final PolylineOptions polylineOptions = new PolylineOptions();
                                        List<LatLng> points;
                                        for (Step step : steps) {
                                            points = DirectionsUtils.decodePoly(step.getPolyline().getEncodedPoints());
                                            polylineOptions.addAll(points);
                                        }

                                        polylineOptions.width(10);
                                        polylineOptions.color(color);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Polyline polyline = map.addPolyline(polylineOptions);
                                                Pair<Polyline, PolylineOptions> pathPair = new Pair<Polyline, PolylineOptions>(polyline, polylineOptions);
                                                if (callback != null)
                                                    callback.pathDrawn(startPoint, endPoint, waypoints);
                                            }
                                        });
                                    }
                                }).start();
                            } else {
                                if (callback != null)
                                    callback.pathDrawn(startPoint, endPoint, waypoints);
                            }
                        } else {
                            if (callback != null)
                                callback.pathDrawn(startPoint, endPoint, waypoints);
                            showMessage("Directions error", null);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Debug", "Directions failure: " + error.getMessage());
                        if (callback != null)
                            callback.pathDrawn(startPoint, endPoint, waypoints);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TripDetailsActivity.this, MyTripsActivity.class));
        TripDetailsActivity.this.finish();
    }
}
