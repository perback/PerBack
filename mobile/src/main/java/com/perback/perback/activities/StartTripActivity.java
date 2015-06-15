package com.perback.perback.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.perback.perback.R;
import com.perback.perback.controllers.TripController;
import com.perback.perback.dao.Dao;
import com.perback.perback.holders.Trip;
import com.perback.perback.holders.TripPoint;
import com.perback.perback.x_base.BaseActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class StartTripActivity extends BaseActivity {

    private EditText etTripName;
    private EditText etStartLocation;
    private EditText etEndLocation;
    private Button btnStartTrip;

    @Override
    protected int getLayoutResId() {
        return R.layout.start_trip_activity;
    }

    @Override
    protected void linkUI() {
        super.linkUI();
        etTripName = (EditText) findViewById(R.id.et_trip_name);
        etStartLocation = (EditText) findViewById(R.id.et_start_location);
        etEndLocation = (EditText) findViewById(R.id.et_end_location);
        btnStartTrip = (Button) findViewById(R.id.btn_start_trip);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void setActions() {
        super.setActions();
        btnStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                views.get(R.id.fl_progress).setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Trip trip = new Trip();
                        trip.setTripName(etTripName.getText().toString());

                        Geocoder geocoder = new Geocoder(StartTripActivity.this);
                        try {
                            TripPoint location = Dao.getInstance().readLocation();
                            List<Address> start_addrs = geocoder.getFromLocation(location.getLat(), location.getLng(), 1);
                            if (start_addrs != null && start_addrs.size() > 0) {
                                Address start_location = start_addrs.get(0);
                                trip.setStartLocationLabel(start_location.getLocality() + ", " + start_location.getCountryName());
                            }

                            final String endLocation = etEndLocation.getText().toString();
                            if (!TextUtils.isEmpty(endLocation)) {
                                List<Address> end_addrs = geocoder.getFromLocationName(endLocation, 1);
                                if (end_addrs != null && end_addrs.size() > 0) {
                                    Address address = end_addrs.get(0);
                                    trip.setEndLocationLat(address.getLatitude());
                                    trip.setEndLocationLng(address.getLongitude());
                                    trip.setEndLocationLabel(address.getLocality() + ", " + address.getCountryName());
                                    TripController.getInstance().startTrip(trip);
                                } else {
                                    showErrorDialog();
                                }
                            } else {
                                // todo remove this
                                trip = Trip.getMockup();
                                TripPoint tp = Dao.getInstance().readLocation();
                                trip.setStartLocationLat(tp.getLat());
                                trip.setStartLocationLng(tp.getLng());
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                views.get(R.id.fl_progress).setVisibility(View.GONE);
                            }
                        });

                        TripController.getInstance().startTrip(trip);
                        startActivity(new Intent(StartTripActivity.this, TripProgressActivity.class));
                        StartTripActivity.this.finish();


                    }
                }).start();
            }
        });
    }

    private void showErrorDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                views.get(R.id.fl_progress).setVisibility(View.GONE);
                new AlertDialog.Builder(StartTripActivity.this)
                        .setMessage("Can't find this location!")
                        .setPositiveButton("Continue anyway", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Try again", null)
                        .show();
            }
        });
    }
}
