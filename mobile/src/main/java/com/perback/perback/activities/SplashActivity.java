package com.perback.perback.activities;

import com.perback.perback.R;
import com.perback.perback.apis.ean.PingResponse;
import com.perback.perback.dao.Dao;
import com.perback.perback.utils.RetrofitUtils;
import com.perback.perback.x_base.BaseActivitySplash;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SplashActivity extends BaseActivitySplash {

    @Override
    protected int getLayoutResId() {
        return R.layout.splash_activity;
    }

    @Override
    protected ArrayList<Runnable> getOperations() {
        ArrayList<Runnable> operations = new ArrayList<>();
        operations.add(getIpCheckOperation());
        operations.add(getEanPingOperation());
        return operations;
    }

    private Runnable getIpCheckOperation() {
        return new Runnable() {
            @Override
            public void run() {
                if(Dao.getInstance().readIp()==null) {
                    RetrofitUtils.getIpifyApi().getIp(new Callback<Response>() {

                        @Override
                        public void success(Response response, Response response2) {
                            String ip = new String(((TypedByteArray)response.getBody()).getBytes());
                            Dao.getInstance().writeIp(ip);
                            operationDone();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            operationDone();
                        }
                    });
                } else {
                    operationDone();
                }
            }
        };
    }

    private Runnable getEanPingOperation() {
        return new Runnable() {
            @Override
            public void run() {
                RetrofitUtils.getEanApi(SplashActivity.this).ping("Test message", new Callback<PingResponse>() {
                    @Override
                    public void success(PingResponse pingResponse, Response response) {
                        operationDone();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        operationDone();
                    }
                });
            }
        };
    }
}
