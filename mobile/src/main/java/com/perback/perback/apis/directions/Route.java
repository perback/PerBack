package com.perback.perback.apis.directions;

import com.perback.perback.x_base.BaseSerializable;

import java.util.ArrayList;

public class Route extends BaseSerializable {

    protected Bounds bounds;
    protected ArrayList<Leg> legs;
    protected Polyline overview_polyline;

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public ArrayList<Leg> getLegs() {
        return legs;
    }

    public void setLegs(ArrayList<Leg> legs) {
        this.legs = legs;
    }

    public Polyline getOverview_polyline() {
        return overview_polyline;
    }

    public void setOverview_polyline(Polyline overview_polyline) {
        this.overview_polyline = overview_polyline;
    }
}
