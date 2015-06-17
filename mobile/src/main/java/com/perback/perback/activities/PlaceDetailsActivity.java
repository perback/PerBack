package com.perback.perback.activities;

import android.content.Intent;

import com.perback.perback.R;
import com.perback.perback.apis.places.PlaceDetailsResponse;
import com.perback.perback.x_base.BaseActivity;

public class PlaceDetailsActivity extends BaseActivity {

    private static final String PLACE_DETAILS = "PlaceDetailsActivity.PLACE_DETAILS";

    protected PlaceDetailsResponse placeDetailsResponse;

    @Override
    protected int getLayoutResId() {
        return R.layout.place_details_activity;
    }

    public static void launch(BaseActivity baseActivity, PlaceDetailsResponse placeDetailsResponse) {
        Intent intent = new Intent(baseActivity, PlaceDetailsActivity.class);
        intent.putExtra(PLACE_DETAILS, placeDetailsResponse);
        baseActivity.startActivity(intent);
    }


}
