package com.android.covidsafe.vo.request;

import com.google.gson.annotations.SerializedName;

public class OTPVerificationRequest {
    @SerializedName("otp")
    private String otp;
    @SerializedName("device_id")
    private String deviceId;

    public OTPVerificationRequest() {
    }

    public OTPVerificationRequest(String otp, String deviceId) {
        this.otp = otp;
        this.deviceId = deviceId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
