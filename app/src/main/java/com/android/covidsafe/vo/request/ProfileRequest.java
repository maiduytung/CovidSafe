package com.android.covidsafe.vo.request;

import com.android.covidsafe.vo.Profile;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileRequest {

    @SerializedName("id")
    private String id;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("date_of_birth")
    private Date dateOfBirth;

    @SerializedName("gender")
    private boolean gender;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("identification")
    private String identification;

    @SerializedName("email")
    private String email;

    @SerializedName("province")
    private String province;

    @SerializedName("district")
    private String district;

    @SerializedName("ward")
    private String ward;

    @SerializedName("address")
    private String address;

    @SerializedName("health_insurance_number")
    private String healthInsuranceNumber;

    @SerializedName("nationality")
    private String nationality;

    @SerializedName("ethnic")
    private String ethnic;

    @SerializedName("religion")
    private String religion;

    @SerializedName("occupation")
    private String occupation;

    public ProfileRequest() {
    }

    public ProfileRequest(Profile data) {
        this.id = data.id;
        this.avatar = data.avatar;
        this.fullName = data.fullName;
        this.dateOfBirth = data.dateOfBirth;
        this.gender = data.gender;
        this.phoneNumber = data.phoneNumber;
        this.identification = data.identification;
        this.email = data.email;
        this.province = data.province;
        this.district = data.district;
        this.ward = data.ward;
        this.address = data.address;
        this.healthInsuranceNumber = data.healthInsuranceNumber;
        this.nationality = data.nationality;
        this.ethnic = data.ethnic;
        this.religion = data.religion;
        this.occupation = data.occupation;
    }

    public ProfileRequest(String id, String avatar, String fullName, Date dateOfBirth, boolean gender, String phoneNumber, String identification, String email, String province, String district, String ward, String address, String healthInsuranceNumber, String nationality, String ethnic, String religion, String occupation) {
        this.id = id;
        this.avatar = avatar;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        try {
            Date date = new SimpleDateFormat("MMM dd,yyyy").parse(dateOfBirth);
            this.dateOfBirth = date;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getHealthInsuranceNumber() {
        return healthInsuranceNumber;
    }

    public void setHealthInsuranceNumber(String healthInsuranceNumber) {
        this.healthInsuranceNumber = healthInsuranceNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
