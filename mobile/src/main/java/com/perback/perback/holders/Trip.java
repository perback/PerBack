package com.perback.perback.holders;

import android.text.TextUtils;

import com.perback.perback.x_base.BaseSerializable;

import java.util.ArrayList;
import java.util.Random;

public class Trip extends BaseSerializable {

    protected String tripName;

    protected String startLocationLabel;
    protected double startLocationLat;
    protected double startLocationLng;

    protected String endLocationLabel;
    protected double endLocationLat;
    protected double endLocationLng;

    protected long startTime;
    protected long endTime;

    protected String snapshotPath;

    protected long apiTotalDistance;

    public long getApiTotalDistance() {
        return apiTotalDistance;
    }

    public void setApiTotalDistance(long apiTotalDistance) {
        this.apiTotalDistance = apiTotalDistance;
    }

    public String getSnapshotPath() {
        return snapshotPath;
    }

    public void setSnapshotPath(String snapshotPath) {
        this.snapshotPath = snapshotPath;
    }

    public Trip(){
        tripPoints=new ArrayList<>();
    }

    protected ArrayList<TripPoint> tripPoints;

    public ArrayList<TripPoint> getTripPoints() {
        return tripPoints;
    }

    public void setTripPoints(ArrayList<TripPoint> tripPoints) {
        this.tripPoints = tripPoints;
    }

    public void addTripPoint(TripPoint tripPoint) {
        if(tripPoints==null)
            tripPoints = new ArrayList<>();
        tripPoints.add(tripPoint);
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getStartLocationLabel() {
        return startLocationLabel;
    }

    public void setStartLocationLabel(String startLocationLabel) {
        this.startLocationLabel = startLocationLabel;
    }

    public double getStartLocationLat() {
        return startLocationLat;
    }

    public void setStartLocationLat(double startLocationLat) {
        this.startLocationLat = startLocationLat;
    }

    public double getStartLocationLng() {
        return startLocationLng;
    }

    public void setStartLocationLng(double startLocationLng) {
        this.startLocationLng = startLocationLng;
    }

    public String getEndLocationLabel() {
        return endLocationLabel;
    }

    public void setEndLocationLabel(String endLocationLabel) {
        this.endLocationLabel = endLocationLabel;
    }

    public double getEndLocationLat() {
        return endLocationLat;
    }

    public void setEndLocationLat(double endLocationLat) {
        this.endLocationLat = endLocationLat;
    }

    public double getEndLocationLng() {
        return endLocationLng;
    }

    public void setEndLocationLng(double endLocationLng) {
        this.endLocationLng = endLocationLng;
    }

    public boolean validate() {
        if(TextUtils.isEmpty(tripName))
            return false;
        if(TextUtils.isEmpty(startLocationLabel))
            return false;
        if(TextUtils.isEmpty(endLocationLabel))
            return false;
        return true;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        if(endTime>0)
            return endTime-startTime;
        else
            return System.currentTimeMillis() - startTime;
    }

    public static ArrayList<Trip> getMyTripsMockup() {
        ArrayList<Trip> myTrips = new ArrayList<>();
        Trip trip;
        Random r = new Random();
        for(int i=0; i<10; i++) {
            trip = new Trip();
            trip.setTripName("Trip "+(i+1));
            trip.setStartLocationLabel("Start location mockup " + (i + 1));
            trip.setEndLocationLabel("End location mockup " + (i + 1));
            trip.setStartLocationLat(r.nextInt());
            trip.setStartLocationLng(r.nextInt());
            trip.setEndLocationLat(r.nextInt());
            trip.setEndLocationLng(r.nextInt());
            trip.setStartTime(System.currentTimeMillis());
            trip.setEndTime(r.nextLong());
            myTrips.add(trip);
        }
        return myTrips;
    }


    public static Trip getMockup() {
        Trip trip = new Trip();
        trip.setTripName("Iasi - Istanbul");

        //47.173473,27.5753785 infoiasi
        trip.setStartLocationLabel("Iasi");
        trip.setStartLocationLat(47.173473);
        trip.setStartLocationLng(27.5753785);

        //41.005963,28.975868 istanbul
        trip.setEndLocationLabel("Tot iasi"); // 47.161740, 27.595103
        trip.setEndLocationLat(41.005963);
        trip.setEndLocationLng(28.975868);

        trip.addTripPoint(new TripPoint(47.173265, 27.573484));
        trip.addTripPoint(new TripPoint(47.170216, 27.576070));
        trip.addTripPoint(new TripPoint(47.165446, 27.590447));

        return trip;
    }

}
