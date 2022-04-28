package com.android.covidsafe.vo;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(primaryKeys = "id")
public class HealthDeclaration {
    @NonNull
    @SerializedName("id")
    public final String id;
    @SerializedName("user_id")
    public final String userId;
    @SerializedName("full_name")
    public final String fullName;
    @SerializedName("year_of_birth")
    public final String yearOfBirth;
    @SerializedName("identification")
    public final String identification;
    @SerializedName("gender")
    public final boolean gender;
    @SerializedName("nationality")
    public final String nationality;
    @SerializedName("province")
    public final String province;
    @SerializedName("district")
    public final String district;
    @SerializedName("ward")
    public final String ward;
    @SerializedName("address")
    public final String address;
    @SerializedName("phone_number")
    public final String phoneNumber;
    @SerializedName("email")
    public final String email;
    @SerializedName("visit")
    public final boolean visit;
    @SerializedName("visit_detail")
    public final String visitDetail;
    @SerializedName("symptoms")
    public final boolean symptoms;
    @SerializedName("symptoms_detail")
    public final String symptomsDetail;
    @SerializedName("contact_sick_people")
    public final boolean contactSickPeople;
    @SerializedName("contact_epidemic_area")
    public final boolean contactEpidemicArea;
    @SerializedName("contact_symptoms_people")
    public final boolean contactSymptomsPeople;
    @SerializedName("qr_code")
    public final String qrCode;
    @SerializedName("created_date")
    public final Date createdDate;

    public HealthDeclaration(@NonNull String id, String userId, String fullName, String yearOfBirth, String identification, boolean gender, String nationality, String province, String district, String ward, String address, String phoneNumber, String email, boolean visit, String visitDetail, boolean symptoms, String symptomsDetail, boolean contactSickPeople, boolean contactEpidemicArea, boolean contactSymptomsPeople, String qrCode, Date createdDate) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.yearOfBirth = yearOfBirth;
        this.identification = identification;
        this.gender = gender;
        this.nationality = nationality;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.visit = visit;
        this.visitDetail = visitDetail;
        this.symptoms = symptoms;
        this.symptomsDetail = symptomsDetail;
        this.contactSickPeople = contactSickPeople;
        this.contactEpidemicArea = contactEpidemicArea;
        this.contactSymptomsPeople = contactSymptomsPeople;
        this.qrCode = qrCode;
        this.createdDate = createdDate;
    }
}
