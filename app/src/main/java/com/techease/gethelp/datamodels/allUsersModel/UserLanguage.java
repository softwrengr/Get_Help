package com.techease.gethelp.datamodels.allUsersModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by eapple on 30/10/2018.
 */

public class UserLanguage {

    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("flag")
    @Expose
    private String flag;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
