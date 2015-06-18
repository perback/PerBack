package com.perback.perback.activities;

import android.view.View;
import android.widget.ListView;

import com.perback.perback.R;
import com.perback.perback.adapters.MyTripsAdapter;
import com.perback.perback.controllers.TripController;
import com.perback.perback.holders.Trip;
import com.perback.perback.x_base.BaseActivity;

public class MyTripsActivity extends BaseActivity {

    protected ListView lv_my_trips;

    @Override
    protected int getLayoutResId() {
        return R.layout.my_trips_activity;
    }

    @Override
    protected void linkUI() {
        super.linkUI();
        lv_my_trips = (ListView) views.get(R.id.lv_my_trips);
    }

    @Override
    protected void setData() {
        super.setData();

        if(!TripController.getInstance().isMonitoring()) {
            views.get(R.id.fab_add_trip).setVisibility(View.VISIBLE);
        }

        lv_my_trips.setAdapter(new MyTripsAdapter(this, Trip.getMyTripsMockup()));
    }

    @Override
    protected void setActions() {
        super.setActions();

        views.get(R.id.fab_add_trip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
