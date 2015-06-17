package com.perback.perback.apis.places;

import com.perback.perback.x_base.BaseSerializable;

public class Geometry extends BaseSerializable {

    protected Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
