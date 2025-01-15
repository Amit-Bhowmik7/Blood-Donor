package com.example.myapplication;

public class New_User_Registration_Handler {
    private String Name, Age, Gender, BloodGroup, District, Upazila, Location, PhoneNumber, Password;
    private boolean as_A_Donor;
    public New_User_Registration_Handler() {
    }
    public New_User_Registration_Handler(String name, String age, String gender, String bloodGroup, String district, String upazila, String location, String phoneNumber, String password, boolean as_A_Donor) {
        this.Name = name;
        this.Age = age;
        this.Gender = gender;
        this.BloodGroup = bloodGroup;
        this.District = district;
        this.Upazila = upazila;
        this.Location = location;
        this.PhoneNumber = phoneNumber;
        this.Password = password;
        this.as_A_Donor = as_A_Donor;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getUpazila() {
        return Upazila;
    }

    public void setUpazila(String upazila) {
        Upazila = upazila;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public boolean isAs_A_Donor() {
        return as_A_Donor;
    }

    public void setAs_A_Donor(boolean as_A_Donor) {
        this.as_A_Donor = as_A_Donor;
    }
}
