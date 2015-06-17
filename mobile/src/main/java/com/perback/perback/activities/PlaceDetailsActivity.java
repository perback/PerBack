package com.perback.perback.activities;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.perback.perback.R;
import com.perback.perback.apis.places.PlaceDetailsResponse;
import com.perback.perback.utils.PicassoUtils;
import com.perback.perback.x_base.BaseActivity;

public class PlaceDetailsActivity extends BaseActivity {

    private static final String PLACE_DETAILS = "PlaceDetailsActivity.PLACE_DETAILS";

    protected PlaceDetailsResponse placeDetailsResponse;

    @Override
    protected int getLayoutResId() {
        return R.layout.place_details_activity;
    }

    @Override
    protected void init() {
        super.init();

        placeDetailsResponse = (PlaceDetailsResponse) getIntent().getSerializableExtra(PLACE_DETAILS);

        PicassoUtils.load("https://fbcdn-sphotos-a-a.akamaihd.net/hphotos-ak-xta1/v/t1.0-9/11401484_857761780927595_4041315104609016889_n.jpg?oh=16ad505d89333aec2e855c0f3fd6278d&oe=5631D8AE&__gda__=1446068519_beaacac83031d39ebe20272526177f61", views.getImageView(R.id.backdrop));

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(placeDetailsResponse.getName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public static void launch(BaseActivity baseActivity, PlaceDetailsResponse placeDetailsResponse) {
        Intent intent = new Intent(baseActivity, PlaceDetailsActivity.class);
        intent.putExtra(PLACE_DETAILS, placeDetailsResponse);
        baseActivity.startActivity(intent);
    }


}
