package com.example.myapplication;

public class Blood_Request_Handler {
    private String FullName, District, BloodGroup, BloodQuantity, Date, Time, Disease, HospitalName, HospitalLocation, MobileNumber, accountNumber, requestId;

    public Blood_Request_Handler() {
    }

    public Blood_Request_Handler(String fullName, String district, String bloodGroup, String bloodQuantity, String date, String time, String disease, String hospitalName, String hospitalLocation, String mobileNumber, String accountNumber, String requestId) {
        this.FullName = fullName;
        this.District = district;
        this.BloodGroup = bloodGroup;
        this.BloodQuantity = bloodQuantity;
        this.Date = date;
        this.Time = time;
        this.Disease = disease;
        this.HospitalName = hospitalName;
        this.HospitalLocation = hospitalLocation;
        this.MobileNumber = mobileNumber;
        this.accountNumber = accountNumber;
        this.requestId = requestId;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public String getBloodQuantity() {
        return BloodQuantity;
    }

    public void setBloodQuantity(String bloodQuantity) {
        BloodQuantity = bloodQuantity;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDisease() {
        return Disease;
    }

    public void setDisease(String disease) {
        Disease = disease;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getHospitalLocation() {
        return HospitalLocation;
    }

    public void setHospitalLocation(String hospitalLocation) {
        HospitalLocation = hospitalLocation;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
