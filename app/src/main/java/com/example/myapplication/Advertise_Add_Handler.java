package com.example.myapplication;

public class Advertise_Add_Handler {
    String advertiseTitle, imageUrl;
    public Advertise_Add_Handler(){

    }

    public Advertise_Add_Handler(String advertiseTitle, String imageUrl) {
        this.advertiseTitle = advertiseTitle;
        this.imageUrl = imageUrl;
    }

    public String getAdvertiseTitle() {
        return advertiseTitle;
    }

    public void setAdvertiseTitle(String advertiseTitle) {
        this.advertiseTitle = advertiseTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
