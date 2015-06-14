package com.perback.perback.apis.ean;

import com.google.gson.annotations.SerializedName;
import com.perback.perback.x_base.BaseSerializable;

public class EanWsError extends BaseSerializable {

    protected int itineraryId;
    protected String handling;
    protected String category;
    protected int exceptionConditionId;
    protected String presentationMessage;
    protected String verboseMessage;
    @SerializedName("ServerInfo")
    protected ServerInfo serverInfo;

    public int getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(int itineraryId) {
        this.itineraryId = itineraryId;
    }

    public String getHandling() {
        return handling;
    }

    public void setHandling(String handling) {
        this.handling = handling;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getExceptionConditionId() {
        return exceptionConditionId;
    }

    public void setExceptionConditionId(int exceptionConditionId) {
        this.exceptionConditionId = exceptionConditionId;
    }

    public String getPresentationMessage() {
        return presentationMessage;
    }

    public void setPresentationMessage(String presentationMessage) {
        this.presentationMessage = presentationMessage;
    }

    public String getVerboseMessage() {
        return verboseMessage;
    }

    public void setVerboseMessage(String verboseMessage) {
        this.verboseMessage = verboseMessage;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }
}
