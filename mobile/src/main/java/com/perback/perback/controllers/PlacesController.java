package com.perback.perback.controllers;

public class PlacesController {

    private static PlacesController placesController;

    public static PlacesController getInstance() {
        if(placesController==null) {
            placesController = new PlacesController();
        }
        return placesController;
    }
    
}
