package com.perback.perback.apis.ean;

import com.google.gson.annotations.SerializedName;

public class HotelListResponse extends BaseEANResponse {

    protected int numberOfRoomsRequested;
    protected boolean moreResultsAvailable;
    @SerializedName("HotelList")
    protected HotelList hotelList;

    public int getNumberOfRoomsRequested() {
        return numberOfRoomsRequested;
    }

    public void setNumberOfRoomsRequested(int numberOfRoomsRequested) {
        this.numberOfRoomsRequested = numberOfRoomsRequested;
    }

    public boolean isMoreResultsAvailable() {
        return moreResultsAvailable;
    }

    public void setMoreResultsAvailable(boolean moreResultsAvailable) {
        this.moreResultsAvailable = moreResultsAvailable;
    }

    public HotelList getHotelList() {
        return hotelList;
    }

    public void setHotelList(HotelList hotelList) {
        this.hotelList = hotelList;
    }
}
