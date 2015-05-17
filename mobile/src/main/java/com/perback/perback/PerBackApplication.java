package com.perback.perback;

import android.app.Application;

import com.perback.perback.controllers.TripController;
import com.perback.perback.dao.Dao;
import com.perback.perback.utils.PicassoUtils;

public class PerBackApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PicassoUtils.init(getApplicationContext());
        TripController.init(getApplicationContext());
        Dao.init(getApplicationContext());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Dao.onLowMemory();
    }
}
