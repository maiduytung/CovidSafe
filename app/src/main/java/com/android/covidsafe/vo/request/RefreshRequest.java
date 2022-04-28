package com.android.covidsafe.vo.request;

import com.google.gson.annotations.SerializedName;

public class RefreshRequest {

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("device_id")
    private String deviceId;

    public RefreshRequest() {
    }

    public RefreshRequest(String refreshToken, String deviceId) {
        this.refreshToken = refreshToken;
        this.deviceId = deviceId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}
