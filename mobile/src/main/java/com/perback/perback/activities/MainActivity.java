package com.perback.perback.activities;

import com.perback.perback.R;
import com.perback.perback.x_base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.main_activity;
    }

    @Override
    protected void setData() {
        super.setData();
        TripTestActivity.launch(this);
        finish();
    }
}
