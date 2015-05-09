package com.perback.perback;

import android.app.Application;

import com.perback.perback.utils.PicassoUtils;

public class PerBackApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PicassoUtils.init(getApplicationContext());
    }
}
