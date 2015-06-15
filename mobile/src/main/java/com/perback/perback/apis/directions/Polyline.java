package com.perback.perback.apis.directions;

import com.google.gson.annotations.SerializedName;
import com.perback.perback.x_base.BaseSerializable;

public class Polyline extends BaseSerializable {

    @SerializedName("points")
    protected String encodedPoints;

    public String getEncodedPoints() {
        return encodedPoints;
    }

    public void setEncodedPoints(String encodedPoints) {
        this.encodedPoints = encodedPoints;
    }
}
