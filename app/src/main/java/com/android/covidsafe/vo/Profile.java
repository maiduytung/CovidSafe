package com.android.covidsafe.vo;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(primaryKeys = "id")
public class Profile {
    @NonNull
    @SerializedName("id")
    public final String id;

    @SerializedName("user_id")
    public final String userId;

    @SerializedName("avatar")
    public final String avatar;

    @SerializedName("qr_code")
    public final String qrCode;

    @SerializedName("full_name")
    public final String fullName;

    @SerializedName("date_of_birth")
    public final Date dateOfBirth;

    @SerializedName("gender")
    public final boolean gender;

    @SerializedName("phone_number")
    public final String phoneNumber;

    @SerializedName("identification")
    public final String identification;

    @SerializedName("email")
    public final String email;

    @SerializedName("province")
    public final String province;

    @SerializedName("district")
    public final String district;

    @SerializedName("ward")
    public final String ward;

    @SerializedName("address")
    public final String address;

    @SerializedName("health_insurance_number")
    public final String healthInsuranceNumber;

    @SerializedName("nationality")
    public final String nationality;

    @SerializedName("ethnic")
    public final String ethnic;

    @SerializedName("religion")
    public final String religion;

    @SerializedName("occupation")
    public final String occupation;

    public Profile(@NonNull String id, String userId, String avatar, String qrCode, String fullName, Date dateOfBirth, boolean gender, String phoneNumber, String identification, String email, String province, String district, String ward, String address, String healthInsuranceNumber, String nationality, String ethnic, String religion, String occupation) {
        this.id = id;
        this.userId = userId;
        this.avatar = avatar;
        this.qrCode = qrCode;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.identification = identification;
        this.email = email;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.address = address;
        this.healthInsuranceNumber = healthInsuranceNumber;
        this.nationality = nationality;
        this.ethnic = ethnic;
        this.religion = religion;
        this.occupation = occupation;
    }
}
