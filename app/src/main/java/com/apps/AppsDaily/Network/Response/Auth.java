package com.apps.AppsDaily.Network.Response;

import com.google.gson.annotations.SerializedName;

public class Auth {
    @SerializedName("status")
    private Boolean status;

    @SerializedName("token")
    private String token;

    public Boolean getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return getToken() +":"+ getStatus();
    }
}
