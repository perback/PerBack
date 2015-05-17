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
