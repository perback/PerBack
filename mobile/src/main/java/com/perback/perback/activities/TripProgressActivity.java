package com.perback.perback.activities;

import android.app.FragmentManager;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.perback.perback.R;
import com.perback.perback.apis.directions.DirectionsResponse;
import com.perback.perback.apis.directions.DirectionsUtils;
import com.perback.perback.apis.directions.Step;
import com.perback.perback.controllers.TripController;
import com.perback.perback.controllers.TripPointAddedListener;
import com.perback.perback.dao.Dao;
import com.perback.perback.holders.Trip;
import com.perback.perback.holders.TripPoint;
import com.perback.perback.utils.RetrofitUtils;
import com.perback.perback.x_base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TripProgressActivity extends BaseActivity implements OnMapReadyCallback, TripPointAddedListener {

    protected GoogleMap map;
    protected ArrayList<TripPoint> tripPoints;
    protected boolean firstTime;
    protected boolean cameraUpdated = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.trip_progress_activity;
    }

    @Override
    protected void init() {
        super.init();
        firstTime = true;
        MapFragment mapFragment = MapFragment.newInstance();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.map_container, mapFragment, "Progress Map").commit();
        mapFragment.getMapAsync(this);
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

        Trip trip = TripController.getInstance().getTrip();
        TripPoint startLocation = Dao.getInstance().readLocation();
        RetrofitUtils.getDirectionsApi().getDirections("" + startLocation.getLat() + "," + startLocation.getLng(),
                trip.getEndLocationLat() + "," + trip.getEndLocationLng(), new Callback<DirectionsResponse>() {
                    @Override
                    public void success(final DirectionsResponse directionsResponse, Response response) {
                        if (directionsResponse.isSuccess()) {
                            if (directionsResponse.getRoutes().size() > 0 && directionsResponse.getRoutes().get(0).getLegs().size() > 0) {
                                MarkerOptions options = new MarkerOptions();
                                com.perback.perback.apis.places.Location startLocation = directionsResponse.getRoutes().get(0).getLegs().get(0).getStart_location();
                                com.perback.perback.apis.places.Location endLocation = directionsResponse.getRoutes().get(0).getLegs().get(0).getEnd_location();
                                options.position(new LatLng(startLocation.getLat(), startLocation.getLng()));
                                options.title("Start location");
                                map.addMarker(options);
                                options = new MarkerOptions();
                                options.position(new LatLng(endLocation.getLat(), endLocation.getLng()));
                                options.title("End location");
                                map.addMarker(options);

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
                                        polylineOptions.color(getResources().getColor(R.color.blue_path));

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                map.addPolyline(polylineOptions);
                                            }
                                        });
                                    }
                                }).start();
                            }
                        } else {
                            showMessage("Directions error", null);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Debug", "Directions failure: " + error.getMessage());
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        TripController.getInstance().unregisterListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TripController.getInstance().registerListener(this);
    }

    @Override
    public void onTripPointAdded(TripPoint tripPoint) {
        if (firstTime) {

            firstTime = false;

            tripPoints = TripController.getInstance().getTrip().getTripPoints();

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < tripPoints.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                LatLng position = new LatLng(tripPoints.get(i).getLat(), tripPoints.get(i).getLng());
                points.add(position);

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);


        }

    }
}
