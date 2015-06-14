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
    private static final String IP = "Dao.UserIp";
    private static final String EAN_SESSION = "Dao.EanSession";
    private static final String PLACES_RADIUS = "Dao.PlacesRadius";

    private static Dao instance;
    private static ArrayList<Trip> trips;

    private Dao(Context context) {
        super(context);
    }

    public static void init(Context context) {
        if(instance == null) {
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
        if(trips==null) {
            trips = readTrips();
        }
        return trips;
    }

    public void saveTrip(Trip trip) {
        if(trips==null) {
            trips = readTrips();
            if(trips==null)
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

}
