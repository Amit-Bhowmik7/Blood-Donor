package com.example.myapplication;

public class User {
    private String bloodGroup,name,upazila,district,phoneNumber;
    public User() {}

    public User(String bloodGroup, String name, String upazila, String district, String phoneNumber) {
        this.bloodGroup = bloodGroup;
        this.name = name;
        this.upazila = upazila;
        this.district = district;
        this.phoneNumber = phoneNumber;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getName() {

        return name;
    }

    public String getUpazila() {

        return upazila;
    }

    public String getDistrict() {
        return district;
    }

    public String getPhoneNumber() {

        return phoneNumber;
    }
}
