package com.android.covidsafe.vo;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(primaryKeys = "id")
public class Report {
    @NonNull
    @SerializedName("id")
    public final String id;

    @SerializedName("user_id")
    public final String userId;

    @SerializedName("report")
    public final String report;

    @SerializedName("province")
    public final String province;

    @SerializedName("district")
    public final String district;

    @SerializedName("ward")
    public final String ward;

    @SerializedName("address")
    public final String address;

    @SerializedName("status")
    public final int status;

    @SerializedName("created_date")
    public final Date createdDate;

    public Report(@NonNull String id, String userId, String report, String province, String district, String ward, String address, int status, Date createdDate) {
        this.id = id;
        this.userId = userId;
        this.report = report;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.address = address;
        this.status = status;
        this.createdDate = createdDate;
    }
}
