package com.perback.perback.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.perback.perback.R;
import com.perback.perback.adapters.PlaceAutocompleteAdapter;
import com.perback.perback.controllers.TripController;
import com.perback.perback.dao.Dao;
import com.perback.perback.holders.Trip;
import com.perback.perback.holders.TripPoint;
import com.perback.perback.x_base.BaseActivity;

import java.io.IOException;
import java.util.List;


public class StartTripActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText etTripName;
    private EditText etStartLocation;
    private AutoCompleteTextView etAutocompleteEndLocation;
    private Button btnStartTrip;

    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(43.996578, 28.295629), new LatLng( 47.682693, 26.300057));

    @Override
    protected int getLayoutResId() {
        return R.layout.start_trip_activity;
    }

    @Override
    protected void linkUI() {
        super.linkUI();
        etTripName = (EditText) findViewById(R.id.et_trip_name);
        etStartLocation = (EditText) findViewById(R.id.et_start_location);
        etAutocompleteEndLocation = (AutoCompleteTextView) findViewById(R.id.et_end_location);
        btnStartTrip = (Button) findViewById(R.id.btn_start_trip);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void setData() {
        super.setData();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        etAutocompleteEndLocation.setOnItemClickListener(mAutocompleteClickListener);
        etAutocompleteEndLocation.setDropDownBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.grey_with_opacity)));
        mAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, LAT_LNG_BOUNDS, null);
        etAutocompleteEndLocation.setAdapter(mAdapter);
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
             read the place ID.
              */
            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Toast.makeText(getApplicationContext(), "Clicked: " + item.description,
                    Toast.LENGTH_SHORT).show();
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            // Format details of the place for display and show it in a TextView.
            /*mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                    place.getId(), place.getAddress(), place.getPhoneNumber(),
                    place.getWebsiteUri()));*/

            // Display the third party attributions if set.
            final CharSequence thirdPartyAttribution = places.getAttributions();
            if (thirdPartyAttribution == null) {
//                mPlaceDetailsAttribution.setVisibility(View.GONE);
            } else {
//                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
//                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
            }


            places.release();
        }
    };

    @Override
    protected void setActions() {
        super.setActions();
        btnStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                views.get(R.id.fl_progress).setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Trip trip = new Trip();
                        trip.setTripName(etTripName.getText().toString());

                        Geocoder geocoder = new Geocoder(StartTripActivity.this);
                        try {
                            TripPoint location = Dao.getInstance().readLocation();
                            List<Address> start_addrs = geocoder.getFromLocation(location.getLat(), location.getLng(), 1);
                            if (start_addrs != null && start_addrs.size() > 0) {
                                Address start_location = start_addrs.get(0);
                                trip.setStartLocationLabel(start_location.getLocality() + ", " + start_location.getCountryName());
                            }

                            final String endLocation = etAutocompleteEndLocation.getText().toString();
                            if (!TextUtils.isEmpty(endLocation)) {
                                List<Address> end_addrs = geocoder.getFromLocationName(endLocation, 1);
                                if (end_addrs != null && end_addrs.size() > 0) {
                                    Address address = end_addrs.get(0);
                                    trip.setEndLocationLat(address.getLatitude());
                                    trip.setEndLocationLng(address.getLongitude());
                                    trip.setEndLocationLabel(address.getLocality() + ", " + address.getCountryName());
                                    TripController.getInstance().startTrip(trip);
                                } else {
                                    showErrorDialog();
                                }
                            } else {
                                // todo remove this
                                trip = Trip.getMockup();
                                TripPoint tp = Dao.getInstance().readLocation();
                                trip.setStartLocationLat(tp.getLat());
                                trip.setStartLocationLng(tp.getLng());
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                views.get(R.id.fl_progress).setVisibility(View.GONE);
                            }
                        });

                        Dao.getInstance().writeIsTripStarted(true);

                        TripController.getInstance().startTrip(trip);
                        startActivity(new Intent(StartTripActivity.this, TripProgressActivity.class));
                        StartTripActivity.this.finish();


                    }
                }).start();
            }
        });
    }

    private void showErrorDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                views.get(R.id.fl_progress).setVisibility(View.GONE);
                new AlertDialog.Builder(StartTripActivity.this)
                        .setMessage("Can't find this location!")
                        .setPositiveButton("Continue anyway", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Try again", null)
                        .show();
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
}
