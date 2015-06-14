package com.perback.perback.apis.places;

import com.google.gson.annotations.SerializedName;
import com.perback.perback.x_base.BaseSerializable;

import java.util.ArrayList;

public class OpeningHours extends BaseSerializable {

    @SerializedName("open_now")
    protected boolean openNow;
    protected ArrayList<String> weekday_text;

    public ArrayList<String> getWeekday_text() {
        return weekday_text;
    }

    public void setWeekday_text(ArrayList<String> weekday_text) {
        this.weekday_text = weekday_text;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }
}
