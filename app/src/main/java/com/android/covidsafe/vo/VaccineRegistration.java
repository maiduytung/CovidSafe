package com.android.covidsafe.vo;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(primaryKeys = "id")
public class VaccineRegistration {
    @NonNull
    @SerializedName("id")
    public final String id;
    @SerializedName("qr_code")
    public final String qrCode;
    @SerializedName("user_id")
    public final String userId;
    @SerializedName("full_name")
    public final String fullName;
    @SerializedName("date_of_birth")
    public final Date dateOfBirth;
    @SerializedName("gender")
    public final boolean gender;
    @SerializedName("identification")
    public final String identification;
    @SerializedName("health_insurance_number")
    public final String healthInsuranceNumber;
    @SerializedName("preferred_vaccination_date")
    public final Date preferredVaccinationDate;
    @SerializedName("occupation")
    public final String occupation;
    @SerializedName("priority")
    public final String priority;
    @SerializedName("province")
    public final String province;
    @SerializedName("district")
    public final String district;
    @SerializedName("ward")
    public final String ward;
    @SerializedName("address")
    public final String address;
    @SerializedName("ethnic")
    public final String ethnic;
    @SerializedName("nationality")
    public final String nationality;
    @SerializedName("anaphylaxis")
    public final int anaphylaxis;
    @SerializedName("covid19")
    public final int covid19;
    @SerializedName("other_vaccinations")
    public final int otherVaccinations;
    @SerializedName("cancer")
    public final int cancer;
    @SerializedName("taking_medicine")
    public final int takingMedicine;
    @SerializedName("acute_illness")
    public final int acuteIllness;
    @SerializedName("chronic_progressive_disease")
    public final int chronicProgressiveDisease;
    @SerializedName("chronic_treated_well")
    public final int chronicTreatedWell;
    @SerializedName("pregnant")
    public final int pregnant;
    @SerializedName("more_than_65")
    public final int moreThan65;
    @SerializedName("coagulation")
    public final int coagulation;
    @SerializedName("allergy")
    public final int allergy;
    @SerializedName("status")
    public final int status;
    @SerializedName("created_date")
    public final Date createdDate;

    public VaccineRegistration(@NonNull String id, String qrCode, String userId, String fullName, Date dateOfBirth, boolean gender, String identification, String healthInsuranceNumber, Date preferredVaccinationDate, String occupation, String priority, String province, String district, String ward, String address, String ethnic, String nationality, int anaphylaxis, int covid19, int otherVaccinations, int cancer, int takingMedicine, int acuteIllness, int chronicProgressiveDisease, int chronicTreatedWell, int pregnant, int moreThan65, int coagulation, int allergy, int status, Date createdDate) {
        this.id = id;
        this.qrCode = qrCode;
        this.userId = userId;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.identification = identification;
        this.healthInsuranceNumber = healthInsuranceNumber;
        this.preferredVaccinationDate = preferredVaccinationDate;
        this.occupation = occupation;
        this.priority = priority;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.address = address;
        this.ethnic = ethnic;
        this.nationality = nationality;
        this.anaphylaxis = anaphylaxis;
        this.covid19 = covid19;
        this.otherVaccinations = otherVaccinations;
        this.cancer = cancer;
        this.takingMedicine = takingMedicine;
        this.acuteIllness = acuteIllness;
        this.chronicProgressiveDisease = chronicProgressiveDisease;
        this.chronicTreatedWell = chronicTreatedWell;
        this.pregnant = pregnant;
        this.moreThan65 = moreThan65;
        this.coagulation = coagulation;
        this.allergy = allergy;
        this.status = status;
        this.createdDate = createdDate;
    }
}
