package com.perback.perback.activities;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.perback.perback.R;
import com.perback.perback.controllers.TripController;
import com.perback.perback.holders.Trip;
import com.perback.perback.holders.TripPoint;
import com.perback.perback.x_base.BaseActivity;

import java.util.ArrayList;

import icepick.Icicle;

public class TripTestActivity extends BaseActivity {

    @Icicle
    protected int lastPosition;

    protected ViewGroup vg_tripPoints;

    @Override
    protected int getLayoutResId() {
        return R.layout.trip_test_activity;
    }

    @Override
    protected void linkUI() {
        super.linkUI();
        vg_tripPoints = (ViewGroup) views.get(R.id.ll_tripPoints);
    }

    @Override
    protected void setActions() {
        super.setActions();

        views.get(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastPosition = 0;
                Trip trip = Trip.getMockup();

                views.getTextView(R.id.tv_tripName).setText(trip.getTripName());
                views.getTextView(R.id.tv_tripStart).setText(trip.getStartLocationLabel());
                views.getTextView(R.id.tv_tripEnd).setText(trip.getEndLocationLabel());
                views.getTextView(R.id.tv_status).setText("Monitoring");

                TripController.getInstance().startTrip(trip);
            }
        });

        views.get(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TripController.getInstance().endTrip();
                resetUI();
            }
        });

        views.get(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TripController.getInstance().isMonitoring())
                    updateUI(TripController.getInstance().getTrip());
            }
        });
    }

    public void resetUI() {
        views.getTextView(R.id.tv_tripName).setText("");
        views.getTextView(R.id.tv_tripStart).setText("");
        views.getTextView(R.id.tv_tripEnd).setText("");
        views.getTextView(R.id.tv_status).setText("Stopped");
        vg_tripPoints.removeAllViews();
        lastPosition = 0;
    }

    public void updateUI(Trip trip) {
        ArrayList<TripPoint> tripPoints = trip.getTripPoints();
        View dataView;
        TextView tv_label, tv_data;
        if(tripPoints!=null) {
            for (int i = lastPosition; i < tripPoints.size(); i++) {
                dataView = LayoutInflater.from(this).inflate(R.layout.data_item, null);
                tv_label = (TextView) dataView.findViewById(R.id.tv_label);
                tv_data = (TextView) dataView.findViewById(R.id.tv_data);
                tv_label.setText("Trip point " + (i + 1));
                tv_data.setText("(" + tripPoints.get(i).getLat() + "/" + tripPoints.get(i).getLng()+")");
                vg_tripPoints.addView(dataView);
            }
            lastPosition = tripPoints.size();
        }
    }

    public static void launch(BaseActivity activity) {
        activity.startActivity(new Intent(activity, TripTestActivity.class));
    }
}
