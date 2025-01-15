package com.example.myapplication;

public class Ambulance_Add_Handler {
    private String name, hospitalName, district, upazila, presentAddress, mobileNumber, accountNumber;

    // Default constructor for Firebase
    public Ambulance_Add_Handler() {
    }

    public Ambulance_Add_Handler(String name, String hospitalName, String district, String upazila, String presentAddress, String mobileNumber, String accountNumber) {
        this.name = name;
        this.hospitalName = hospitalName;
        this.district = district;
        this.upazila = upazila;
        this.presentAddress = presentAddress;
        this.mobileNumber = mobileNumber;
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
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
