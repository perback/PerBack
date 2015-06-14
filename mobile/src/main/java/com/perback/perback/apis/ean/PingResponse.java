package com.perback.perback.apis.ean;

import com.google.gson.annotations.SerializedName;
import com.perback.perback.x_base.BaseSerializable;

public class PingResponse extends BaseEANResponse {

    protected String echo;

    @SerializedName("ServerInfo")
    protected ServerInfo serverInfo;

    public String getEcho() {
        return echo;
    }

    public void setEcho(String echo) {
        this.echo = echo;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }
}
