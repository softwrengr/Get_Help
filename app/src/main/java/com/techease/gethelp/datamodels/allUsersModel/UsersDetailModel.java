package com.techease.gethelp.datamodels.allUsersModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by eapple on 24/10/2018.
 */

public class UsersDetailModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("active_since")
    @Expose
    private String activeSince;
    @SerializedName("is_online")
    @Expose
    private String isOnline;
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
    private List<UserLanguage> languages = null;

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

    public String getActiveSince() {
        return activeSince;
    }

    public void setActiveSince(String activeSince) {
        this.activeSince = activeSince;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
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

    public List<UserLanguage> getLanguages() {
        return languages;
    }

    public void setLanguages(List<UserLanguage> languages) {
        this.languages = languages;
    }
}
