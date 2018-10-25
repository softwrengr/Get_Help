package com.techease.gethelp.datamodels.socialModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by eapple on 23/10/2018.
 */

public class SocialResponseModel {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("user")
    @Expose
    private SocialUserDetail user;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public SocialUserDetail getUser() {
        return user;
    }

    public void setUser(SocialUserDetail user) {
        this.user = user;
    }
}
