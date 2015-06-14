package com.perback.perback;

import android.app.Application;
import android.content.Intent;

import com.perback.perback.controllers.TripController;
import com.perback.perback.dao.Dao;
import com.perback.perback.services.LocationService;
import com.perback.perback.utils.PicassoUtils;
import com.perback.perback.utils.RetrofitUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class PerBackApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PicassoUtils.init(getApplicationContext());
        TripController.init(getApplicationContext());
        Dao.init(getApplicationContext());
        if(Dao.getInstance().readPlacesRadius()==null){
            Dao.getInstance().writePlacesRadius(500);
        }
        startService(new Intent(getApplicationContext(), LocationService.class));
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Dao.onLowMemory();
    }
}
