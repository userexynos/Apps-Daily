package com.apps.AppsDaily.Network.Response;

import com.google.gson.annotations.SerializedName;

public class SendData {
    @SerializedName("status")
    public Boolean status;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
