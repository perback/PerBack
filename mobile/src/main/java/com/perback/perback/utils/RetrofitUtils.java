package com.perback.perback.utils;

import android.content.Context;

import com.perback.perback.apis.ean.EANApi;
import com.perback.perback.apis.ean.EANApiWrapper;
import com.perback.perback.apis.ip.IpifyApi;

import retrofit.RestAdapter;

public class RetrofitUtils {


    public static final String EAN_URL = "http://api.ean.com/ean-services/rs/hotel/v3";
    public static final String IPIFY_URL = "https://api.ipify.org";

    private static RestAdapter eanAdapter;

    private static RestAdapter getEanAdapter() {
        if(eanAdapter==null) {
            eanAdapter = new RestAdapter.Builder()
                    .setEndpoint(EAN_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
        }
        return eanAdapter;
    }

    public static IpifyApi getIpifyApi() {
        return new RestAdapter.Builder()
                .setEndpoint(IPIFY_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build().create(IpifyApi.class);
    }

    public static EANApiWrapper getEanApi(Context context) {
        return new EANApiWrapper(getEanAdapter().create(EANApi.class), context);
    }

}
