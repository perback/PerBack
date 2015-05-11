package com.perback.perback.activities;

import com.perback.perback.R;
import com.perback.perback.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.main_activity;
    }

    @Override
    protected void setData() {
        super.setData();
        MapActivity.launch(this);
    }
}
