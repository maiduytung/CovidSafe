package com.android.covidsafe.vo.request;

import com.google.gson.annotations.SerializedName;

public class SendVerificationRequest {
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("device_id")
    private String deviceId;

    public SendVerificationRequest() {
    }

    public SendVerificationRequest(String phoneNumber, String deviceId) {
        this.phoneNumber = phoneNumber;
        this.deviceId = deviceId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
