package com.perback.perback.apis.places;

import com.perback.perback.x_base.BaseSerializable;

import java.util.ArrayList;

public class AddressComponent extends BaseSerializable {

    protected String long_name;
    protected String short_name;
    protected ArrayList<String> types;

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }
}
