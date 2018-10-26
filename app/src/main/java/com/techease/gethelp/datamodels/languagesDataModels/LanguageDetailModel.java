package com.techease.gethelp.datamodels.languagesDataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by eapple on 25/10/2018.
 */

public class LanguageDetailModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("away")
    @Expose
    private String away;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("history")
    @Expose
    private String history;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("languages")
    @Expose
    private Object languages;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Object getLanguages() {
        return languages;
    }

    public void setLanguages(Object languages) {
        this.languages = languages;
    }
}
