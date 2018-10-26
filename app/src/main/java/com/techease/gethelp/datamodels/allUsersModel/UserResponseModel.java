package com.techease.gethelp.datamodels.allUsersModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.techease.gethelp.datamodels.languagesDataModels.LanguageDetailModel;

import java.util.List;

/**
 * Created by eapple on 24/10/2018.
 */

public class UserResponseModel {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<UsersDetailModel> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UsersDetailModel> getData() {
        return data;
    }

    public void setData(List<UsersDetailModel> data) {
        this.data = data;
    }

}
