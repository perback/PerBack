package com.perback.perback.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perback.perback.apis.ean.EANApi;
import com.perback.perback.apis.ean.EANApiWrapper;
import com.perback.perback.apis.ean.EANDeserializer;
import com.perback.perback.apis.ean.PingResponse;
import com.perback.perback.apis.ip.IpifyApi;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RetrofitUtils {


    public static final String EAN_URL = "http://api.ean.com/ean-services/rs/hotel/v3";
    public static final String IPIFY_URL = "https://api.ipify.org";

    private static RestAdapter eanAdapter;

    private static RestAdapter getEanAdapter() {
        if(eanAdapter==null) {
            Gson gson = new GsonBuilder()
                            .registerTypeAdapter(PingResponse.class, new EANDeserializer<PingResponse>())
                            .create();
            eanAdapter = new RestAdapter.Builder()
                    .setConverter(new GsonConverter(gson))
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
