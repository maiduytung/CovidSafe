package com.android.covidsafe.vo.request;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ReportRequest {

    @NonNull
    @SerializedName("report")
    private String report;

    @NonNull
    @SerializedName("province")
    private String province;

    @NonNull
    @SerializedName("district")
    private String district;

    @NonNull
    @SerializedName("ward")
    private String ward;

    @SerializedName("address")
    private String address;

    public ReportRequest() {
    }

    public ReportRequest(@NonNull String report, @NonNull String province, @NonNull String district, @NonNull String ward, String address) {
        this.report = report;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.address = address;
    }

    @NonNull
    public String getReport() {
        return report;
    }

    public void setReport(@NonNull String report) {
        this.report = report;
    }

    @NonNull
    public String getProvince() {
        return province;
    }

    public void setProvince(@NonNull String province) {
        this.province = province;
    }

    @NonNull
    public String getDistrict() {
        return district;
    }

    public void setDistrict(@NonNull String district) {
        this.district = district;
    }

    @NonNull
    public String getWard() {
        return ward;
    }

    public void setWard(@NonNull String ward) {
        this.ward = ward;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
