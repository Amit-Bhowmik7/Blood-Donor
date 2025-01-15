package com.example.myapplication;

public class Organization_Add_Handler {
    private String organizationName, district, upazila, officeAddress, mobileNumber, accountNumber;

    public Organization_Add_Handler(){
    }

    public Organization_Add_Handler(String organizationName, String district, String upazila, String officeAddress, String mobileNumber, String accountNumber) {
        this.organizationName = organizationName;
        this.district = district;
        this.upazila = upazila;
        this.officeAddress = officeAddress;
        this.mobileNumber = mobileNumber;
        this.accountNumber = accountNumber;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
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
