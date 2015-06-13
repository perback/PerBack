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
    private static String USER_AGENT = "";
    private static String IP = "";

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

    public void ping(String echoMessage, Callback<PingResponse> pingResponseCallback) {
        String sig = Utils.md5(SHARED_SECRET+API_KEY+System.currentTimeMillis());
        eanApi.ping(CID, MINOR_REV, API_KEY, sig, USER_AGENT, IP, echoMessage, pingResponseCallback);
    }


}
