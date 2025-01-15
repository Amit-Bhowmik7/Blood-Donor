package com.example.myapplication;

public class Thalassemia_Patient_Handler {
    private String patientName, bloodGroup, district, upazila, presentAddress, mobileNumber, accountNumber;

    public Thalassemia_Patient_Handler(){

    }

    public Thalassemia_Patient_Handler(String patientName, String bloodGroup, String district, String upazila, String presentAddress, String mobileNumber, String accountNumber) {
        this.patientName = patientName;
        this.bloodGroup = bloodGroup;
        this.district = district;
        this.upazila = upazila;
        this.presentAddress = presentAddress;
        this.mobileNumber = mobileNumber;
        this.accountNumber = accountNumber;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUpazila() {
        return upazila;
    }

    public void setUpazila(String upazila) {
        this.upazila = upazila;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
