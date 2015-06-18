package com.perback.perback.adapters;

import android.content.Context;

import com.perback.perback.R;
import com.perback.perback.holders.Trip;
import com.perback.perback.x_base.BaseAdapter;
import com.perback.perback.x_base.ViewHolder;

import java.util.ArrayList;

public class MyTripsAdapter extends BaseAdapter<Trip> {

    public MyTripsAdapter(Context context, ArrayList<Trip> dataSet) {
        super(context, R.layout.item_my_trips, dataSet);
    }

    @Override
    protected void setData(ViewHolder views, Trip item, int pos) {
        
    }
}
