package com.techease.gethelp.datamodels.clientRequestsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientRequestDataModel {

    @SerializedName("request_id")
    @Expose
    private String requestId;
    @SerializedName("requested")
    @Expose
    private String requested;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("current_location")
    @Expose
    private String currentLocation;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("driver")
    @Expose
    private String driver;
    @SerializedName("status")
    @Expose
    private String status;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequested() {
        return requested;
    }

    public void setRequested(String requested) {
        this.requested = requested;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
