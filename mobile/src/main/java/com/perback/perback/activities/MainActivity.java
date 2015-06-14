package com.perback.perback.activities;

import com.perback.perback.R;
import com.perback.perback.apis.ean.EANCallback;
import com.perback.perback.apis.ean.HotelListResponse;
import com.perback.perback.apis.places.BasePlacesResponse;
import com.perback.perback.apis.places.PlacesResponse;
import com.perback.perback.dao.Dao;
import com.perback.perback.holders.TripPoint;
import com.perback.perback.utils.RetrofitUtils;
import com.perback.perback.x_base.BaseActivity;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.main_activity;
    }

    @Override
    protected void setData() {
        super.setData();
//        TripTestActivity.launch(this);
//        finish();
//        testHotelList();
        testPlacesApi();
    }

    private void testPlacesApi() {
        RetrofitUtils.getPlacesApi().testCall(new Callback<BasePlacesResponse<PlacesResponse>>() {
            @Override
            public void success(BasePlacesResponse<PlacesResponse> placesResponseBasePlacesResponse, Response response) {
                if(placesResponseBasePlacesResponse.isSucces()) {
                    ArrayList<PlacesResponse> placesResponse = placesResponseBasePlacesResponse.getResults();
                    showMessage("Found "+placesResponse.size()+" places!", null);
                } else {
                    showMessage("Error", null);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showMessage("Error: "+error.getMessage(), null);
            }
        });
    }

    private void testHotelList() {
        TripPoint location = Dao.getInstance().readLocation();
        if(location!=null) {
            RetrofitUtils.getEanApi(this).getNearbyHotels(location.getLat(), location.getLng(), new EANCallback<HotelListResponse>() {
                @Override
                public void success(HotelListResponse hotelListResponse) {
                    if (hotelListResponse.isSucces()) {
                        showMessage("" + hotelListResponse.getHotelList().getHotels().size() + " hotels found!", null);
                    } else {
                        showMessage(hotelListResponse.getMessage(), null);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    showMessage("" + error.getMessage(), null);
                }
            });
        }
    }
}
