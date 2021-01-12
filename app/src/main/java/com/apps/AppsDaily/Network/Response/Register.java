package com.apps.AppsDaily.Network.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Register {
    @Expose
    @SerializedName("status")
    public Boolean status;

    @Expose
    @SerializedName("messages")
    public List<String> messages;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
    @Override
    public String toString() {
        return getStatus()+" "+getMessages();
    }
}
