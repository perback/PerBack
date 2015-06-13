package com.perback.perback.apis.ean;

import android.content.Context;
import android.webkit.WebView;

import com.perback.perback.dao.Dao;
import com.perback.perback.utils.Utils;

import retrofit.Callback;

public class EANApiWrapper {

    private static final String CID = "490284";
    private static final String API_KEY = "1e4i9oqg6vmhmm7qr7e5o0r7u9";
    private static final String SHARED_SECRET = " a0090tmu976g4";
    private static final String MINOR_REV = "29";
    private static final String API_EXPERIENCE = "PARTNER_MOBILE_APP";
    private static String USER_AGENT = "";
    private static String IP = "";

    public static final String KM_UNIT = "KM";
    public static final String MI_UNIT = "MI";

    private EANApi eanApi;

    public EANApiWrapper(EANApi eanApi, Context context) {
        this.eanApi = eanApi;
        if(USER_AGENT.length()==0) {
            USER_AGENT = new WebView(context).getSettings().getUserAgentString();
        }
        if(IP.length()==0) {
            IP = Dao.getInstance().readIp();
        }
    }

    public void ping(String echoMessage, EANCallback<PingResponse> pingResponseCallback) {
        String sig = getSig();
        eanApi.ping(CID, MINOR_REV, API_KEY, sig, USER_AGENT, echoMessage, pingResponseCallback);
    }

    public void getNearbyHotels(double latitude, double longitude, EANCallback<HotelListResponse> callback) {
        getNearbyHotels(latitude, longitude, null, null, null, callback);

    }

    public void getNearbyHotels(double latitude, double longitude, Integer searchRadius,
                                String searchRadiusUnit, EANCallback<HotelListResponse> callback) {
        getNearbyHotels(latitude, longitude, searchRadius, searchRadiusUnit, null, callback);

    }

    public void getNearbyHotels(double latitude, double longitude, Integer searchRadius,
                                String searchRadiusUnit, String sort, EANCallback<HotelListResponse> callback) {
        String sig = getSig();
        String sessionId = Dao.getInstance().readEanSession();
        eanApi.getNearbyHotels(sessionId, IP, CID, MINOR_REV, API_KEY, sig, API_EXPERIENCE, USER_AGENT,
                String.valueOf(latitude), String.valueOf(longitude), searchRadius,
                searchRadiusUnit, sort, callback);
    }

    private String getSig() {
        return Utils.md5(API_KEY+SHARED_SECRET+System.currentTimeMillis());
    }

}
