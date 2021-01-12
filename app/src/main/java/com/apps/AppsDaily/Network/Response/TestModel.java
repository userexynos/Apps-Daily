package com.apps.AppsDaily.Network.Response;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 2/3/17.
 */

public class TestModel {
    @SerializedName("status")
    private Boolean status;

    @SerializedName("msg")
    private String msg;

    public Boolean getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return getMsg() +":"+ getStatus();
    }
}
