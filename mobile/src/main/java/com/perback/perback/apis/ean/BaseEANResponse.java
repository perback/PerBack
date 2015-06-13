package com.perback.perback.apis.ean;

import com.google.gson.annotations.SerializedName;
import com.perback.perback.x_base.BaseSerializable;

public class BaseEANResponse extends BaseSerializable {

    protected String customerSessionId;
    @SerializedName("EanWsError")
    protected EanWsError eanWsError;

    public String getCustomerSessionId() {
        return customerSessionId;
    }

    public void setCustomerSessionId(String customerSessionId) {
        this.customerSessionId = customerSessionId;
    }

    public EanWsError getEanWsError() {
        return eanWsError;
    }

    public void setEanWsError(EanWsError eanWsError) {
        this.eanWsError = eanWsError;
    }

    public boolean isSucces() {
        return eanWsError == null;
    }

    public String getMessage() {
        return eanWsError.getVerboseMessage();
    }

}
