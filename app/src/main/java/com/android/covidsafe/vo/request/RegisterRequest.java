package com.android.covidsafe.vo.request;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("username")
    private String phoneNumber;
    @SerializedName("password")
    private String password;
    @SerializedName("device_id")
    private String deviceId;
    @SerializedName("verification_key")
    private String verificationKey;

    public RegisterRequest() {
    }

    public RegisterRequest(String phoneNumber, String password, String deviceId, String verificationKey) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.deviceId = deviceId;
        this.verificationKey = verificationKey;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getVerificationKey() {
        return verificationKey;
    }

    public void setVerificationKey(String verificationKey) {
        this.verificationKey = verificationKey;
    }
}
