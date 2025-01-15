package com.example.myapplication;

public class Help_Line_Add_Handler {
    private String title, contactNumber, requestId;
    public Help_Line_Add_Handler(){

    }

    public Help_Line_Add_Handler(String title, String contactNumber, String requestId) {
        this.title = title;
        this.contactNumber = contactNumber;
        this.requestId = requestId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
