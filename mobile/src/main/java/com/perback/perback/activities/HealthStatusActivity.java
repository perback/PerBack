package com.perback.perback.activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;

import com.perback.perback.R;
import com.perback.perback.dao.Dao;
import com.perback.perback.x_base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ralucapostelnicu on 14/06/15.
 */
public class HealthStatusActivity extends BaseActivity implements SensorEventListener {

    private TextView tvDate;
    private TextView tvNoOfSteps;

    private SensorManager sensorManager;
    private boolean isActivityRunning;

    private Dao dao;

    @Override
    protected int getLayoutResId() {
        return R.layout.health_status_activity;
    }

    @Override
    protected void init() {
        super.init();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        dao = Dao.getInstance();
    }

    @Override
    protected void linkUI() {
        super.linkUI();
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvNoOfSteps = (TextView) findViewById(R.id.tv_no_of_steps);
    }

    @Override
    protected void setData() {
        super.setData();
        tvDate.setText(getCurrentDate());
        tvNoOfSteps.setText(String.valueOf(dao.readNoOfSteps()));
    }

    private String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d yyyy");
        return sdf.format(cal.getTime());
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (isActivityRunning) {
            tvNoOfSteps.setText(String.valueOf((int)sensorEvent.values[0]));
            dao.writeNoOfSteps((int)sensorEvent.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityRunning = false;
        // if you unregister the last listener, the hardware will stop detecting step events
//        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
