package com.android.covidsafe.vo.request;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.android.covidsafe.BR;
import com.android.covidsafe.vo.Profile;
import com.google.gson.annotations.SerializedName;

public class HealthDeclarationRequest extends BaseObservable {
    @SerializedName("id")
    private String id;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("year_of_birth")
    private String yearOfBirth;
    @SerializedName("identification")
    private String identification;
    @SerializedName("gender")
    private boolean gender;
    @SerializedName("nationality")
    private String nationality;
    @SerializedName("province")
    private String province;
    @SerializedName("district")
    private String district;
    @SerializedName("ward")
    private String ward;
    @SerializedName("address")
    private String address;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("email")
    private String email;
    @SerializedName("visit")
    private boolean visit;
    @SerializedName("visit_detail")
    private String visitDetail;
    @SerializedName("symptoms")
    private boolean symptoms;
    @SerializedName("symptoms_detail")
    private String symptomsDetail;
    @SerializedName("contact_sick_people")
    private boolean contactSickPeople;
    @SerializedName("contact_epidemic_area")
    private boolean contactEpidemicArea;
    @SerializedName("contact_symptoms_people")
    private boolean contactSymptomsPeople;
    @SerializedName("qr_code")
    private String qrCode;

    public HealthDeclarationRequest() {
    }

    public HealthDeclarationRequest(Profile data) {
        this.fullName = data.fullName;

        if (data.dateOfBirth != null) {
            this.yearOfBirth = String.valueOf(1900 + data.dateOfBirth.getYear());
        }

        this.identification = data.identification;
        this.gender = data.gender;
        this.nationality = data.nationality;
        this.province = data.province;
        this.district = data.district;
        this.ward = data.ward;
        this.address = data.address;
        this.phoneNumber = data.phoneNumber;
        this.email = data.email;
    }

    public HealthDeclarationRequest(String id, String fullName, String yearOfBirth, String identification, boolean gender, String nationality, String province, String district, String ward, String address, String phoneNumber, String email, boolean visit, String visitDetail, boolean symptoms, String symptomsDetail, boolean contactSickPeople, boolean contactEpidemicArea, boolean contactSymptomsPeople, String qrCode) {
        this.id = id;
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
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Bindable
    public boolean isVisit() {
        return visit;
    }

    public void setVisit(boolean visit) {
        this.visit = visit;
        notifyPropertyChanged(BR.visit);
    }

    public String getVisitDetail() {
        return visitDetail;
    }

    public void setVisitDetail(String visitDetail) {
        this.visitDetail = visitDetail;
    }

    @Bindable
    public boolean isSymptoms() {
        return symptoms;
    }

    public void setSymptoms(boolean symptoms) {
        this.symptoms = symptoms;
        notifyPropertyChanged(BR.symptoms);
    }

    public String getSymptomsDetail() {
        return symptomsDetail;
    }

    public void setSymptomsDetail(String symptomsDetail) {
        this.symptomsDetail = symptomsDetail;
    }

    public boolean isContactSickPeople() {
        return contactSickPeople;
    }

    public void setContactSickPeople(boolean contactSickPeople) {
        this.contactSickPeople = contactSickPeople;
    }

    public boolean isContactEpidemicArea() {
        return contactEpidemicArea;
    }

    public void setContactEpidemicArea(boolean contactEpidemicArea) {
        this.contactEpidemicArea = contactEpidemicArea;
    }

    public boolean isContactSymptomsPeople() {
        return contactSymptomsPeople;
    }

    public void setContactSymptomsPeople(boolean contactSymptomsPeople) {
        this.contactSymptomsPeople = contactSymptomsPeople;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
