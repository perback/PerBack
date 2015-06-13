package com.perback.perback.apis.ean;

import com.perback.perback.dao.Dao;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class EANCallback<T> implements Callback<T> {

    public abstract void success(T t);


    @Override
    public void success(T t, Response response) {
        if(t instanceof BaseEANResponse) {
            BaseEANResponse temp = (BaseEANResponse) t;
            if(temp.getCustomerSessionId()!=null) {
                Dao.getInstance().writeEanSession(temp.getCustomerSessionId());
            }
        }
        success(t);
    }

}
