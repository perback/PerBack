package com.perback.perback.dao;

import android.content.Context;

import com.perback.perback.holders.Trip;
import com.perback.perback.holders.TripPoint;
import com.perback.perback.x_base.BaseDao;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class Dao extends BaseDao {

    private static final String LOCATION = "Dao.UserLocation";
    private static final String TRIPS = "Dao.UserTrips";
    private static final String DISPLAY_LOCATION = "Dao.DisplayLocation";
    private static final String UPDATE_DISTANCE = "Dao.UpdateDistance";
    private static final String DISTANCE_PROXIMITY = "Dao.DistanceProximity";
    private static final String STEPS = "Dao.Steps";
    private static final String IP = "Dao.UserIp";
    private static final String EAN_SESSION = "Dao.EanSession";
    private static final String PLACES_RADIUS = "Dao.PlacesRadius";
    private static final String TRIP_STARTED = "trip_started";

    private static Dao instance;
    private static ArrayList<Trip> trips;

    private Dao(Context context) {
        super(context);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new Dao(context);
        }
    }

    public static void onLowMemory() {
        instance.writeTrips(trips);
        trips = null;
    }

    public static Dao getInstance() {
        return instance;
    }

    public void writePlacesRadius(Integer radius) {
        write(StorageMethod.InternalFiles, radius, PLACES_RADIUS);
    }

    public Integer readPlacesRadius() {
        return (Integer) read(StorageMethod.InternalFiles, PLACES_RADIUS);
    }

    public void writeEanSession(String session) {
        write(StorageMethod.InternalFiles, session, EAN_SESSION);
    }

    public String readEanSession() {
        return (String) read(StorageMethod.InternalFiles, EAN_SESSION);
    }

    public void writeIp(String ip) {
        write(StorageMethod.InternalFiles, ip, IP);
    }

    public String readIp() {
        return (String) read(StorageMethod.InternalFiles, IP);
    }

    public void writeLocation(TripPoint tripPoint) {
        write(StorageMethod.InternalFiles, tripPoint, LOCATION);
    }

    public TripPoint readLocation() {
        return (TripPoint) read(StorageMethod.InternalFiles, LOCATION);
    }

    public ArrayList<Trip> getTrips() {
        if (trips == null) {
            trips = readTrips();
        }
        return trips;
    }

    public void saveTrip(Trip trip) {
        if (trips == null) {
            trips = readTrips();
            if (trips == null)
                trips = new ArrayList<>();
        }
        trips.add(trip);
        writeTrips(trips);
    }

    private void writeTrips(ArrayList<Trip> trips) {
        write(StorageMethod.InternalFiles, trips, TRIPS);
    }

    private ArrayList<Trip> readTrips() {
        return (ArrayList<Trip>) read(StorageMethod.InternalFiles, TRIPS);
    }

    public void writeDisplayLocation(Boolean displayLocation) {
        write(StorageMethod.InternalFiles, displayLocation, DISPLAY_LOCATION);
    }

    public boolean readDisplayLocation() {
        if (read(StorageMethod.InternalFiles, DISPLAY_LOCATION) == null)
            return false;
        return (boolean) read(StorageMethod.InternalFiles, DISPLAY_LOCATION);
    }

    public void writeUpdateDistancePosition(int updateDistanceTime) {
        write(StorageMethod.InternalFiles, updateDistanceTime, UPDATE_DISTANCE);
    }

    public int readUpdateDistancePosition() {
        if (read(StorageMethod.InternalFiles, UPDATE_DISTANCE) == null)
            return 0;
        return (int) read(StorageMethod.InternalFiles, UPDATE_DISTANCE);
    }

    public void writeDistanceProximityPosition(int distanceProximityPosition) {
        write(StorageMethod.InternalFiles, distanceProximityPosition, DISTANCE_PROXIMITY);
    }

    public Integer readDistanceProximityPosition() {
        if (read(StorageMethod.InternalFiles, DISTANCE_PROXIMITY) == null)
            return 0;
        return (Integer) read(StorageMethod.InternalFiles, DISTANCE_PROXIMITY);
    }

    public void writeNoOfSteps(int noOfSteps) {
        write(StorageMethod.InternalFiles, noOfSteps, STEPS);
    }

    public int readNoOfSteps() {
        if (read(StorageMethod.InternalFiles, STEPS) == null)
            return 0;
        return (int) read(StorageMethod.InternalFiles, STEPS);
    }

    public void writeIsTripStarted(boolean isTripStarted) {
        write(StorageMethod.InternalFiles, isTripStarted, TRIP_STARTED);
    }

    public boolean isTripStarted() {
        if (read(StorageMethod.InternalFiles, TRIP_STARTED) == null)
            return false;
        return (boolean) read(StorageMethod.InternalFiles, TRIP_STARTED);
    }

}
