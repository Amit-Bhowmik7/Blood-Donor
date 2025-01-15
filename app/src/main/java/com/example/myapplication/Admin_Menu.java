package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Admin_Menu extends AppCompatActivity implements View.OnClickListener {
private AppCompatButton usersList, adsAddButton, factsAddBUtton, helpLine, contributorPublishButton,bloodRequestListButton,organizationListButton,ambulanceListButton,thalassemiaListButton,  volunteerListButton;
private ImageButton backButton, homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        //Find id
        factsAddBUtton = findViewById(R.id.factsAddButton);
        helpLine = findViewById(R.id.helpLineAddButton);
        contributorPublishButton = findViewById(R.id.contributorPublishedButton);
        adsAddButton = findViewById(R.id.adsAddButton);
        backButton = findViewById(R.id.back_button);
        bloodRequestListButton = findViewById(R.id.bloodRequestListButton);
        organizationListButton = findViewById(R.id.organizationListButton);
        ambulanceListButton = findViewById(R.id.ambulanceListButton);
        thalassemiaListButton = findViewById(R.id.thalassemiaListButton);
        volunteerListButton = findViewById(R.id.volunteerListButton);
        usersList = findViewById(R.id.alluserButton);
        homeButton = findViewById(R.id.home);

        adsAddButton.setVisibility(View.GONE);


        backButton.setOnClickListener(v -> backButton());
        homeButton.setOnClickListener(v -> homeButton());
        adsAddButton.setOnClickListener(this);
        factsAddBUtton.setOnClickListener(this);
        helpLine.setOnClickListener(this);
        contributorPublishButton.setOnClickListener(this);
        bloodRequestListButton.setOnClickListener(this);
        organizationListButton.setOnClickListener(this);
        ambulanceListButton.setOnClickListener(this);
        thalassemiaListButton.setOnClickListener(this);
        volunteerListButton.setOnClickListener(this);
        usersList.setOnClickListener(this);

    }

    private void homeButton() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void backButton() {
        Intent intent = new Intent(getApplicationContext(), Admin_SignIn.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.adsAddButton:
                Intent intent1 = new Intent(getApplicationContext(), Ads_Add.class);
                startActivity(intent1);
                break;

            case R.id.factsAddButton:
                Intent intent2 = new Intent(getApplicationContext(), Admin_Facts_Menu.class);
                startActivity(intent2);
                break;

            case R.id.helpLineAddButton:
                Intent intent3 = new Intent(getApplicationContext(), Admin_HelpLine_Menu.class);
                startActivity(intent3);
                break;

            case R.id.contributorPublishedButton:
                Intent intent4 = new Intent(getApplicationContext(), Donation_Person_Entry_List.class);
                startActivity(intent4);
                break;

            case R.id.bloodRequestListButton:
                Intent intent5 = new Intent(getApplicationContext(), Admin_Blood_Request_List.class);
                startActivity(intent5);
                break;

            case R.id.organizationListButton:
                Intent intent6 = new Intent(getApplicationContext(), Admin_Organization_List.class);
                startActivity(intent6);
                break;

            case R.id.ambulanceListButton:
                Intent intent7 = new Intent(getApplicationContext(), Admin_Ambulance_List.class);
                startActivity(intent7);
                break;

            case R.id.thalassemiaListButton:
                Intent intent8 = new Intent(getApplicationContext(), Admin_Thalassemia_Patient_List.class);
                startActivity(intent8);
                break;

            case R.id.volunteerListButton:
                Intent intent9 = new Intent(getApplicationContext(), Admin_Volunteer_List.class);
                startActivity(intent9);
                break;

            case R.id.alluserButton:
                Intent intent10 = new Intent(getApplicationContext(), Admin_Users_List.class);
                startActivity(intent10);
                break;
        }
    }
}