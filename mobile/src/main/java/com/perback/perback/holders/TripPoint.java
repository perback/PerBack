package com.perback.perback.holders;

import com.perback.perback.x_base.BaseSerializable;

public class TripPoint extends BaseSerializable {

    protected double lat;
    protected double lng;

    protected boolean checkpoint;
    protected String description;
    protected String imagePath;

    public TripPoint(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        checkpoint = false;
    }

    public TripPoint(double lat, double lng, boolean checkpoint) {
        this.lat = lat;
        this.lng = lng;
        this.checkpoint = checkpoint;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(boolean checkpoint) {
        this.checkpoint = checkpoint;
    }
}
