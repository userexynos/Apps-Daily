package com.apps.AppsDaily.Network.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserProfiles {
    @SerializedName("user")
    @Expose
    private GetUserProfilesPrivacy user;

    public GetUserProfilesPrivacy getUser() {
        return user;
    }

    public void setUser(GetUserProfilesPrivacy user) {
        this.user = user;
    }
}
