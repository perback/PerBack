package com.perback.perback.activities;

import android.app.FragmentManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.perback.perback.R;
import com.perback.perback.apis.directions.DirectionsResponse;
import com.perback.perback.apis.directions.DirectionsUtils;
import com.perback.perback.apis.directions.Leg;
import com.perback.perback.apis.directions.Step;
import com.perback.perback.controllers.TripController;
import com.perback.perback.controllers.TripPointAddedListener;
import com.perback.perback.dao.Dao;
import com.perback.perback.holders.Trip;
import com.perback.perback.holders.TripPoint;
import com.perback.perback.utils.RetrofitUtils;
import com.perback.perback.x_base.BaseActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TripProgressActivity extends BaseActivity implements OnMapReadyCallback, TripPointAddedListener {

    protected GoogleMap map;
    protected ArrayList<TripPoint> tripPoints;
    protected boolean firstTime;
    protected boolean cameraUpdated = false;
    protected Pair<Polyline, PolylineOptions> tripPath;
    protected Pair<Polyline, PolylineOptions> progressPath;

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

        final Trip trip = TripController.getInstance().getTrip();
        final TripPoint currentLocation = Dao.getInstance().readLocation();
        TripPoint startLocation = currentLocation;
        if (trip.getTripPoints().size() > 0) {
            startLocation = trip.getTripPoints().get(0);
        }


        LatLng startPoint = new LatLng(startLocation.getLat(), startLocation.getLng());
        LatLng endPoint = new LatLng(trip.getEndLocationLat(), trip.getEndLocationLng());
        drawPath(false, startPoint, endPoint, trip.getTripPoints(), getResources().getColor(R.color.blue_path), new DrawPathCallback() {
            @Override
            public void pathDrawn(LatLng startPoint, LatLng endPoint, ArrayList<TripPoint> waypoints) {
                endPoint = new LatLng(currentLocation.getLat(), currentLocation.getLng());
                drawPath(true, startPoint, endPoint, trip.getTripPoints(), getResources().getColor(R.color.orange_path), new DrawPathCallback() {
                    @Override
                    public void pathDrawn(LatLng startPoint, LatLng endPoint, ArrayList<TripPoint> waypoints) {
                        setStats();
                    }
                });

            }
        });
    }

    public void setStats() {
        double percentage = TripController.getInstance().getProgress();
        float distanceLeft = TripController.getInstance().getDistanceLeft()/1000;
        views.getTextView(R.id.tv_percentage).setText(getString(R.string.percentage_achieved, ""+new DecimalFormat("#.##").format(percentage)+"%"));
        views.getTextView(R.id.tv_distance_left).setText(getString(R.string.kilometers_left, ""+distanceLeft));
        long time = TripController.getInstance().getElapsedTime();

        SimpleDateFormat sdf;
        String unit;
        if(time<60000) {
            sdf = new SimpleDateFormat("ss");
            unit = "seconds";
        }
        else if(time<3600000) {
           sdf = new SimpleDateFormat("mm");
            unit = "minutes";
        } else {
            sdf = new SimpleDateFormat("HH");
            unit = "hours";
        }

        Date elapsedTime = new Date();
        views.getTextView(R.id.tv_time_elapsed).setText(getString(R.string.hours_walking, ""+sdf.format(elapsedTime), unit));

//        Animation fadeIn = new AlphaAnimation(0,1);
//        fadeIn.setDuration(300);
//        fadeIn.setFillAfter(true);
//        views.get(R.id.ll_stats).startAnimation(fadeIn);
        views.get(R.id.ll_stats).setVisibility(View.VISIBLE);
    }

    public void drawPath(final boolean tripProgress, final LatLng startPoint, final LatLng endPoint, final ArrayList<TripPoint> waypoints, final int color, final DrawPathCallback callback) {
        RetrofitUtils.getDirectionsApi().getDirections("" + startPoint.latitude + "," + startPoint.longitude,
                endPoint.latitude + "," + endPoint.longitude, waypoints, new Callback<DirectionsResponse>() {
                    @Override
                    public void success(final DirectionsResponse directionsResponse, Response response) {
                        if (directionsResponse.isSuccess()) {
                            if (directionsResponse.getRoutes().size() > 0 && directionsResponse.getRoutes().get(0).getLegs().size() > 0) {
                                Leg leg = directionsResponse.getRoutes().get(0).getLegs().get(0);
                                if(!tripProgress) {
                                    TripController.getInstance().setApiTotalDistance(leg.getDistance().getValue());
                                } else {
                                    TripController.getInstance().setApiProgressDistance(leg.getDistance().getValue());
                                }

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
                                                if(tripProgress) {
                                                    progressPath = pathPair;
                                                } else {
                                                    tripPath = pathPair;
                                                }
                                                if(callback!=null)
                                                    callback.pathDrawn(startPoint, endPoint, waypoints);
                                            }
                                        });
                                    }
                                }).start();
                            } else {
                                if(callback!=null)
                                    callback.pathDrawn(startPoint, endPoint, waypoints);
                            }
                        } else {
                            if(callback!=null)
                                callback.pathDrawn(startPoint, endPoint, waypoints);
                            showMessage("Directions error", null);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Debug", "Directions failure: " + error.getMessage());
                        if(callback!=null)
                            callback.pathDrawn(startPoint, endPoint, waypoints);
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
    public void onNewTripPoint(TripPoint tripPoint) {
        if(progressPath!=null) {
            List<LatLng> points = progressPath.first.getPoints();
            points.add(new LatLng(tripPoint.getLat(), tripPoint.getLng()));
            progressPath.first.setPoints(points);
        }
    }

    private interface DrawPathCallback {
        void pathDrawn(LatLng startPoint, LatLng endPoint, ArrayList<TripPoint> waypoints);
    }
}
