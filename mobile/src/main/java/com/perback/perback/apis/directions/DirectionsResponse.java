package com.perback.perback.apis.directions;

import com.perback.perback.x_base.BaseSerializable;

import java.util.ArrayList;

public class DirectionsResponse extends BaseSerializable {

    protected String status;
    protected ArrayList<Route> routes;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    public boolean isSuccess() {
        return status.equals("OK");
    }
}
