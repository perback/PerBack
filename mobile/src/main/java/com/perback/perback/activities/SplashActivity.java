package com.perback.perback.activities;

import android.app.Activity;
import android.content.DialogInterface;

import com.perback.perback.R;
import com.perback.perback.apis.ean.EANCallback;
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
    protected Class<? extends Activity> getNextActivity() {
        return MainActivity.class;
    }

    @Override
    protected ArrayList<Runnable> getOperations() {
        ArrayList<Runnable> operations = new ArrayList<>();
        operations.add(getTestOperation());
        operations.add(getIpCheckOperation());
        operations.add(getEanPingOperation());
        return operations;
    }

    private Runnable getTestOperation() {
        return new Runnable() {
            @Override
            public void run() {
                operationDone();
            }
        };
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
                            showMessage("Ip: " + ip, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    operationDone();
                                }
                            });

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
                RetrofitUtils.getEanApi(SplashActivity.this).ping("Test message", new EANCallback<PingResponse>() {
                    @Override
                    public void success(PingResponse pingResponse) {
                        if(pingResponse.isSucces()) {
                            showMessage("Ping Success", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    operationDone();
                                }
                            });
                        } else {
                            showMessage("Error: "+pingResponse.getMessage(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    operationDone();
                                }
                            });
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        showMessage("Ping Failure: " + error, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                operationDone();
                            }
                        });
                    }
                });
            }
        };
    }
}
