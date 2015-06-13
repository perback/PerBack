package com.perback.perback;

import android.app.Application;

import com.perback.perback.controllers.TripController;
import com.perback.perback.dao.Dao;
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
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Dao.onLowMemory();
    }
}
