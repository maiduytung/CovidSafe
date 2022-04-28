package com.android.covidsafe.vo.request;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.android.covidsafe.BR;
import com.android.covidsafe.vo.Profile;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VaccineRegistrationRequest extends BaseObservable {

    @NonNull
    @SerializedName("full_name")
    private String fullName;

    @NonNull
    @SerializedName("date_of_birth")
    private Date dateOfBirth;

    @NonNull
    @SerializedName("gender")
    private boolean gender;

    @NonNull
    @SerializedName("identification")
    private String identification;

    @SerializedName("health_insurance_number")
    private String healthInsuranceNumber;

    @SerializedName("preferred_vaccination_date")
    private Date preferredVaccinationDate;

    @NonNull
    @SerializedName("occupation")
    private String occupation;

    @NonNull
    @SerializedName("priority")
    private String priority;

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

    @SerializedName("ethnic")
    private String ethnic;

    @SerializedName("nationality")
    private String nationality;

    @SerializedName("anaphylaxis")
    private int anaphylaxis;

    @SerializedName("covid19")
    private int covid19;

    @SerializedName("other_vaccinations")
    private int otherVaccinations;

    @SerializedName("cancer")
    private int cancer;

    @SerializedName("taking_medicine")
    private int takingMedicine;

    @SerializedName("acute_illness")
    private int acuteIllness;

    @SerializedName("chronic_progressive_disease")
    private int chronicProgressiveDisease;

    @SerializedName("chronic_treated_well")
    private int chronicTreatedWell;

    @SerializedName("pregnant")
    private int pregnant;

    @SerializedName("more_than_65")
    private int moreThan65;

    @SerializedName("coagulation")
    private int coagulation;

    @SerializedName("allergy")
    private int allergy;

    public VaccineRegistrationRequest() {
    }

    public VaccineRegistrationRequest(Profile data) {
        this.fullName = data.fullName;
        this.dateOfBirth = data.dateOfBirth;
        this.identification = data.identification;
        this.province = data.province;
        this.district = data.district;
        this.ward = data.ward;
        this.address = data.address;
        this.healthInsuranceNumber = data.healthInsuranceNumber;
        this.nationality = data.nationality;
        this.ethnic = data.ethnic;
        this.occupation = data.occupation;
        this.anaphylaxis = 0;
        this.covid19 = 0;
        this.otherVaccinations = 0;
        this.cancer = 0;
        this.takingMedicine = 0;
        this.acuteIllness = 0;
        this.chronicProgressiveDisease = 0;
        this.chronicTreatedWell = 0;
        this.pregnant = 0;
        this.moreThan65 = 0;
        this.coagulation = 0;
        this.allergy = 0;
    }

    public VaccineRegistrationRequest(@NonNull String fullName, @NonNull Date dateOfBirth, boolean gender, @NonNull String identification, String healthInsuranceNumber, Date preferredVaccinationDate, @NonNull String occupation, @NonNull String priority, @NonNull String province, @NonNull String district, @NonNull String ward, String address, String ethnic, String nationality, int anaphylaxis, int covid19, int otherVaccinations, int cancer, int takingMedicine, int acuteIllness, int chronicProgressiveDisease, int chronicTreatedWell, int pregnant, int moreThan65, int coagulation, int allergy) {
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
    }

    @NonNull
    public String getFullName() {
        return fullName;
    }

    public void setFullName(@NonNull String fullName) {
        this.fullName = fullName;
    }

    @NonNull
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@NonNull Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setDateOfBirth(String dateString) {
        try {
            @SuppressLint("SimpleDateFormat") Date dateOfBirth = new SimpleDateFormat("MMM dd, yyyy").parse(dateString);
            this.dateOfBirth = dateOfBirth;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    @NonNull
    public String getIdentification() {
        return identification;
    }

    public void setIdentification(@NonNull String identification) {
        this.identification = identification;
    }

    public String getHealthInsuranceNumber() {
        return healthInsuranceNumber;
    }

    public void setHealthInsuranceNumber(String healthInsuranceNumber) {
        this.healthInsuranceNumber = healthInsuranceNumber;
    }

    public Date getPreferredVaccinationDate() {
        return preferredVaccinationDate;
    }

    public void setPreferredVaccinationDate(Date preferredVaccinationDate) {
        this.preferredVaccinationDate = preferredVaccinationDate;
    }

    public void setPreferredVaccinationDate(String dateString) {
        try {
            @SuppressLint("SimpleDateFormat") Date preferredVaccinationDate = new SimpleDateFormat("MMM dd, yyyy").parse(dateString);
            this.preferredVaccinationDate = preferredVaccinationDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(@NonNull String occupation) {
        this.occupation = occupation;
    }

    @NonNull
    public String getPriority() {
        return priority;
    }

    public void setPriority(@NonNull String priority) {
        this.priority = priority;
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

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Bindable
    public int getAnaphylaxis() {
        return anaphylaxis;
    }

    public void setAnaphylaxis(int anaphylaxis) {
        this.anaphylaxis = anaphylaxis;
        notifyPropertyChanged(BR.anaphylaxis);
    }

    public int getCovid19() {
        return covid19;
    }

    public void setCovid19(int covid19) {
        this.covid19 = covid19;
    }

    @Bindable
    public int getOtherVaccinations() {
        return otherVaccinations;
    }

    public void setOtherVaccinations(int otherVaccinations) {
        this.otherVaccinations = otherVaccinations;
        notifyPropertyChanged(BR.otherVaccinations);
    }

    public int getCancer() {
        return cancer;
    }

    public void setCancer(int cancer) {
        this.cancer = cancer;
    }

    public int getTakingMedicine() {
        return takingMedicine;
    }

    public void setTakingMedicine(int takingMedicine) {
        this.takingMedicine = takingMedicine;
    }

    @Bindable
    public int getAcuteIllness() {
        return acuteIllness;
    }

    public void setAcuteIllness(int acuteIllness) {
        this.acuteIllness = acuteIllness;
        notifyPropertyChanged(BR.acuteIllness);
    }

    @Bindable
    public int getChronicProgressiveDisease() {
        return chronicProgressiveDisease;
    }

    public void setChronicProgressiveDisease(int chronicProgressiveDisease) {
        this.chronicProgressiveDisease = chronicProgressiveDisease;
        notifyPropertyChanged(BR.chronicProgressiveDisease);
    }

    public int getChronicTreatedWell() {
        return chronicTreatedWell;
    }

    public void setChronicTreatedWell(int chronicTreatedWell) {
        this.chronicTreatedWell = chronicTreatedWell;
    }

    public int getPregnant() {
        return pregnant;
    }

    public void setPregnant(int pregnant) {
        this.pregnant = pregnant;
    }

    public int getMoreThan65() {
        return moreThan65;
    }

    public void setMoreThan65(int moreThan65) {
        this.moreThan65 = moreThan65;
    }

    public int getCoagulation() {
        return coagulation;
    }

    public void setCoagulation(int coagulation) {
        this.coagulation = coagulation;
    }

    @Bindable
    public int getAllergy() {
        return allergy;
    }

    public void setAllergy(int allergy) {
        this.allergy = allergy;
        notifyPropertyChanged(BR.allergy);
    }
}
