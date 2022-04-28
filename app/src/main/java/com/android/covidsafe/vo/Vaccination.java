package com.android.covidsafe.vo;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(primaryKeys = "id")
public class Vaccination {

    @NonNull
    @SerializedName("id")
    public final String id;

    @SerializedName("identification")
    public final String identification;

    @SerializedName("vaccine_registration_id")
    public final String vaccineRegistrationId;

    @SerializedName("full_name")
    public final String fullName;

    @SerializedName("date_of_birth")
    public final Date dateOfBirth;

    public final boolean gender;

    public final String address;

    @SerializedName("vaccination_date")
    public final Date vaccinationDate;

    @SerializedName("vaccination_type")
    public final String vaccinationType;

    @SerializedName("vaccination_center")
    public final String vaccinationCenter;

    public Vaccination(@NonNull String id, String identification, String vaccineRegistrationId, String fullName, Date dateOfBirth, boolean gender, String address, Date vaccinationDate, String vaccinationType, String vaccinationCenter) {
        this.id = id;
        this.identification = identification;
        this.vaccineRegistrationId = vaccineRegistrationId;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.vaccinationDate = vaccinationDate;
        this.vaccinationType = vaccinationType;
        this.vaccinationCenter = vaccinationCenter;
    }
}
