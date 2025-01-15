package com.example.myapplication;

public class Donation_Person_Entry_Form_Handler {

    private String Name, district, mobileNumber, amount, tnx_id, contributorId;

    public Donation_Person_Entry_Form_Handler(){
    }

    public Donation_Person_Entry_Form_Handler(String name, String district, String mobileNumber, String amount, String tnx_id, String contributorId) {
        this.Name = name;
        this.district = district;
        this.mobileNumber = mobileNumber;
        this.amount = amount;
        this.tnx_id = tnx_id;
        this.contributorId = contributorId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTnx_id() {
        return tnx_id;
    }

    public void setTnx_id(String tnx_id) {
        this.tnx_id = tnx_id;
    }

    public String getContributorId() {
        return contributorId;
    }

    public void setContributorId(String contributorId) {
        this.contributorId = contributorId;
    }
}
