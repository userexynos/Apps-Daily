package com.apps.AppsDaily.Network.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatestStore {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private LatestStoreData data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LatestStoreData getData() {
        return data;
    }

    public void setData(LatestStoreData data) {
        this.data = data;
    }

}
