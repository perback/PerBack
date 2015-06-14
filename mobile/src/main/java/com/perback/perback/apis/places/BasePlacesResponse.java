package com.perback.perback.apis.places;

import com.perback.perback.x_base.BaseSerializable;

import java.util.ArrayList;

public class BasePlacesResponse<T> extends BaseSerializable {

    protected String status;
    protected ArrayList<T> results;

    public ArrayList<T> getResults() {
        return results;
    }

    public void setResults(ArrayList<T> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSucces() {
        return status!=null && status.equals("OK");
    }

}
