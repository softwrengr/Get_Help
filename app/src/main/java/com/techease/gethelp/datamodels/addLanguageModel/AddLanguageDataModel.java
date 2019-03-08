package com.techease.gethelp.datamodels.addLanguageModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddLanguageDataModel {

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
