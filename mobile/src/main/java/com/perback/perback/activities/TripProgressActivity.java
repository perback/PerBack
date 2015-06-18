package com.perback.perback.activities;

import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Environment;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.perback.perback.controllers.TripControllerListener;
import com.perback.perback.dao.Dao;
import com.perback.perback.holders.Trip;
import com.perback.perback.holders.TripPoint;
import com.perback.perback.utils.DrawPathCallback;
import com.perback.perback.utils.RetrofitUtils;
import com.perback.perback.x_base.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TripProgressActivity extends BaseActivity implements OnMapReadyCallback, TripControllerListener {

    protected GoogleMap map;
    protected ArrayList<TripPoint> tripPoints;
    protected boolean firstTime;
    protected boolean cameraUpdated = false;
    protected Pair<Polyline, PolylineOptions> tripPath;
    protected Pair<Polyline, PolylineOptions> progressPath;
    protected Bitmap mapBitmap;

    protected Timer timer;
    protected TimerTask timerTask;
    protected final Handler handler = new Handler();

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

        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setStats();
                            }
                        });
                    }
                });
            }
        };

        timer = new Timer();
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
                        timer.schedule(timerTask, 0, 1010);
                    }
                });

            }
        });
    }

    public void setStats() {
        double percentage = TripController.getInstance().getProgress();
        float distanceLeft = TripController.getInstance().getDistanceLeft() / 1000;
        views.getTextView(R.id.tv_percentage).setText(getString(R.string.percentage_achieved, "" + new DecimalFormat("#.##").format(percentage) + "%"));
        views.getTextView(R.id.tv_distance_left).setText(getString(R.string.kilometers_left, "" + distanceLeft));
        long startTime = TripController.getInstance().getTrip().getStartTime();

        Calendar startDateTime = Calendar.getInstance(Locale.ENGLISH);
        startDateTime.setTimeInMillis(startTime);

        Calendar endDateTime = Calendar.getInstance(Locale.ENGLISH);
        endDateTime.setTimeInMillis(System.currentTimeMillis());

        long milliseconds1 = startDateTime.getTimeInMillis();
        long milliseconds2 = endDateTime.getTimeInMillis();
        long diff = milliseconds2 - milliseconds1;

        long hours = diff / (60 * 60 * 1000);
        long minutes = diff / (60 * 1000);
        minutes = minutes - 60 * hours;
        long seconds = diff / (1000);

        String result = "";

        if (hours > 0) {
            result = hours + " hours " + minutes + " minutes";
        } else {
            if (minutes > 0)
                result = minutes + " minutes";
            else {
                result = seconds + " seconds";
            }
        }


//        SimpleDateFormat sdf;
//        String unit;
//        if (time < 60000) {
//            sdf = new SimpleDateFormat("s");
//            unit = "seconds";
//        } else if (time < 3600000) {
//            sdf = new SimpleDateFormat("m");
//            unit = "minutes";
//        } else {
//            sdf = new SimpleDateFormat("H");
//            unit = "hours";
//        }

//        views.getTextView(R.id.tv_time_elapsed).setText(getString(R.string.hours_walking, "" + sdf.format(cal), unit));
        views.getTextView(R.id.tv_time_elapsed).setText(getString(R.string.hours_walking, "" + result, ""));

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
                                if (!tripProgress) {
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
                                                if (tripProgress) {
                                                    progressPath = pathPair;
                                                } else {
                                                    tripPath = pathPair;
                                                }
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
        if (progressPath != null) {
            List<LatLng> points = progressPath.first.getPoints();
            points.add(new LatLng(tripPoint.getLat(), tripPoint.getLng()));
            progressPath.first.setPoints(points);
        }
    }

    @Override
    public void onTripEnd(Trip trip) {
        if (timer != null)
            timer.cancel();
        if (menu != null) {
            MenuItem menuItem = menu.findItem(R.id.action_end_flag);
            if (menuItem != null) {
                menuItem.setVisible(false);
                invalidateOptionsMenu();
            }
        }
        TripDetailsActivity.launch(TripProgressActivity.this, trip);
        TripProgressActivity.this.finish();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId()==R.id.action_end_flag) {
////            captureScreen();
//
//            new MaterialDialog.Builder(this)
//                    .title("End Trip")
//                    .content("Congratulations! You have achieved "+ new DecimalFormat("#.##").format(TripController.getInstance().getProgress())+"%" +" of your trip")
//                    .positiveText("Finish")
//                    .negativeText("Cancel")
//                    .callback(new MaterialDialog.ButtonCallback() {
//                        @Override
//                        public void onPositive(MaterialDialog dialog) {
//                            Dao.getInstance().writeIsTripStarted(false);
//                            TripController.getInstance().endTrip();
////                            TripDetailsActivity.launch();
////                                Intent intent = new Intent(BaseActivity.this, MyTripsActivity.class);
////                                startActivity(intent);
//                        }
//
//                        @Override
//                        public void onNegative(MaterialDialog dialog) {
//                        }
//                    }).show();
//
//            return true;
//        } else {
//            return super.onOptionsItemSelected(item);
//        }
//    }

    public void captureScreen() {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                mapBitmap = snapshot;

                OutputStream fout = null;
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String filePath = "Perback-"+System.currentTimeMillis() + ".jpeg";
                File all = new File(dir, filePath);
                try {
                    all.createNewFile();
                    fout = new FileOutputStream(all);
                    mapBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);

                    TripController.getInstance().endTrip(all.getPath());
                    fout.flush();
                    fout.close();
                } catch (FileNotFoundException e) {
                    Log.d("ImageCapture", "FileNotFoundException");
                    Log.d("ImageCapture", e.getMessage());
                    filePath = "";
                } catch (IOException e) {
                    Log.d("ImageCapture", "IOException");
                    Log.d("ImageCapture", e.getMessage());
                    filePath = "";
                }
            }
        };

        map.snapshot(callback);
    }




}
