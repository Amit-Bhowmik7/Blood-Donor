package com.example.myapplication;

public class Volunteer_Add_Handler {
    private String volinteerName, district, upazila, presentAddress, mobileNumber, accountNumber;

    public Volunteer_Add_Handler(){

    }

    public Volunteer_Add_Handler(String volinteerName, String district, String upazila, String presentAddress, String mobileNumber, String accountNumber) {
        this.volinteerName = volinteerName;
        this.district = district;
        this.upazila = upazila;
        this.presentAddress = presentAddress;
        this.mobileNumber = mobileNumber;
        this.accountNumber = accountNumber;
    }

    public String getVolinteerName() {
        return volinteerName;
    }

    public void setVolinteerName(String volinteerName) {
        this.volinteerName = volinteerName;
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


