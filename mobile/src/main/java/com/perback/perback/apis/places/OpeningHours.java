package com.perback.perback.apis.places;

import com.google.gson.annotations.SerializedName;
import com.perback.perback.x_base.BaseSerializable;

public class OpeningHours extends BaseSerializable {

    @SerializedName("open_now")
    protected boolean openNow;

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }
}
