package com.perback.perback.apis.ean;

import com.google.gson.annotations.SerializedName;
import com.perback.perback.x_base.BaseSerializable;

import java.util.ArrayList;

public class HotelList extends BaseSerializable {

    @SerializedName("@size")
    protected int size;
    @SerializedName("@activePropertyCount")
    protected int activePropertyCount;
    @SerializedName("HotelSummary")
    protected ArrayList<HotelSummary> hotels;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getActivePropertyCount() {
        return activePropertyCount;
    }

    public void setActivePropertyCount(int activePropertyCount) {
        this.activePropertyCount = activePropertyCount;
    }

    public ArrayList<HotelSummary> getHotels() {
        return hotels;
    }

    public void setHotels(ArrayList<HotelSummary> hotels) {
        this.hotels = hotels;
    }
}
