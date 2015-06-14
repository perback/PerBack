package com.perback.perback.apis.ean;

import com.google.gson.annotations.SerializedName;
import com.perback.perback.x_base.BaseSerializable;

public class HotelSummary extends BaseSerializable {

    @SerializedName("@order")
    protected int order;
    protected int hotelId;
    protected String name;
    protected String address1;
    protected String city;
    protected String countryCode;
    protected String airportCode;
    protected int propertyCategory;
    protected double hotelRating;
    protected double confidenceRating;
    protected long amenityMask;
    protected double tripAdvisorRating;
    protected long tripAdvisorReviewCount;
    protected String tripAdvisorRatingUrl;
    protected String locationDescription;
    protected String shortDescription;
    protected double highRate;
    protected double lowRate;
    protected String rateCurrencyCode;
    protected double latitude;
    protected double longitude;
    protected double proximityDistance;
    protected String thumbNailUrl;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public int getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(int propertyCategory) {
        this.propertyCategory = propertyCategory;
    }

    public double getHotelRating() {
        return hotelRating;
    }

    public void setHotelRating(double hotelRating) {
        this.hotelRating = hotelRating;
    }

    public double getConfidenceRating() {
        return confidenceRating;
    }

    public void setConfidenceRating(double confidenceRating) {
        this.confidenceRating = confidenceRating;
    }

    public long getAmenityMask() {
        return amenityMask;
    }

    public void setAmenityMask(long amenityMask) {
        this.amenityMask = amenityMask;
    }

    public double getTripAdvisorRating() {
        return tripAdvisorRating;
    }

    public void setTripAdvisorRating(double tripAdvisorRating) {
        this.tripAdvisorRating = tripAdvisorRating;
    }

    public long getTripAdvisorReviewCount() {
        return tripAdvisorReviewCount;
    }

    public void setTripAdvisorReviewCount(long tripAdvisorReviewCount) {
        this.tripAdvisorReviewCount = tripAdvisorReviewCount;
    }

    public String getTripAdvisorRatingUrl() {
        return tripAdvisorRatingUrl;
    }

    public void setTripAdvisorRatingUrl(String tripAdvisorRatingUrl) {
        this.tripAdvisorRatingUrl = tripAdvisorRatingUrl;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public double getHighRate() {
        return highRate;
    }

    public void setHighRate(double highRate) {
        this.highRate = highRate;
    }

    public double getLowRate() {
        return lowRate;
    }

    public void setLowRate(double lowRate) {
        this.lowRate = lowRate;
    }

    public String getRateCurrencyCode() {
        return rateCurrencyCode;
    }

    public void setRateCurrencyCode(String rateCurrencyCode) {
        this.rateCurrencyCode = rateCurrencyCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getProximityDistance() {
        return proximityDistance;
    }

    public void setProximityDistance(double proximityDistance) {
        this.proximityDistance = proximityDistance;
    }

    public String getThumbNailUrl() {
        return thumbNailUrl;
    }

    public void setThumbNailUrl(String thumbNailUrl) {
        this.thumbNailUrl = thumbNailUrl;
    }
}
