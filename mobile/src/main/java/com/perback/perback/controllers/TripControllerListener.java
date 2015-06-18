package com.perback.perback.controllers;

import com.perback.perback.holders.Trip;
import com.perback.perback.holders.TripPoint;

public interface TripControllerListener {

    void onNewTripPoint(TripPoint tripPoint);
    void onTripEnd(Trip trip);

}
