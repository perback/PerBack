package com.perback.perback.activities;

import android.content.Intent;

import com.perback.perback.R;
import com.perback.perback.base.BaseActivity;

public class MapActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.map_activity;
    }

    public static void launch(BaseActivity activity) {
        activity.startActivity(new Intent(activity, MapActivity.class));
    }
}
