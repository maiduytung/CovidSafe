package com.android.covidsafe.vo.response;

public class BaseResponse {

    public final String data;
    public final Boolean success;
    public final String timestamp;
    public final String cause;
    public final String path;

    public BaseResponse(String data, Boolean success, String timestamp, String cause, String path) {
        this.data = data;
        this.success = success;
        this.timestamp = timestamp;
        this.cause = cause;
        this.path = path;
    }
}
