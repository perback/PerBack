package com.perback.perback.activities;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.perback.perback.R;
import com.perback.perback.x_base.BaseActivity;

/**
 * Created by ralucapostelnicu on 14/06/15.
 */
public class StartTripActivity extends BaseActivity {

    private EditText etTripName;
    private EditText etStartLocation;
    private EditText etEndLocation;
    private Button btnStartTrip;

    @Override
    protected int getLayoutResId() {
        return R.layout.start_trip_activity;
    }

    @Override
    protected void linkUI() {
        super.linkUI();
        etTripName = (EditText) findViewById(R.id.et_trip_name);
        etStartLocation = (EditText) findViewById(R.id.et_start_location);
        etEndLocation = (EditText) findViewById(R.id.et_end_location);
        btnStartTrip = (Button) findViewById(R.id.btn_start_trip);
    }

    @Override
    protected void setActions() {
        super.setActions();
        btnStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StartTripActivity.this, "Start Trip", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
