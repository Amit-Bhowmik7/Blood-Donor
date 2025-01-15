package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Profile_Page extends AppCompatActivity {
private AppCompatButton profileEditButton, profileVolunteerList, profileBloodRequestList, organizationList, ambulanceList, thalassemiaList;
private ImageButton backButton, homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        profileEditButton = findViewById(R.id.profileEditButton);
        profileEditButton.setOnClickListener(v -> profileEditButton());

        profileBloodRequestList = findViewById(R.id.bloodRequestPostButton);
        profileBloodRequestList.setOnClickListener(v -> profileBloodRequestList() );

        organizationList = findViewById(R.id.organigationPostButton);
        organizationList.setOnClickListener(v -> organizationList());

        ambulanceList = findViewById(R.id.ambulancePostButton);
        ambulanceList.setOnClickListener(v -> ambulanceList());

        thalassemiaList = findViewById(R.id.thalassemiaPostButton);
        thalassemiaList.setOnClickListener(v -> thalassemiaList());

        profileVolunteerList = findViewById(R.id.volunteerPostButton);
        profileVolunteerList.setOnClickListener(v -> volunteerPost());

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> backButton());

        homeButton = findViewById(R.id.home);
        homeButton.setVisibility(View.GONE);

    }

    private void backButton() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void thalassemiaList() {
        Intent intent = new Intent(getApplicationContext(), Profile_Thalassemia_List.class);
        startActivity(intent);
    }

    private void ambulanceList() {
        Intent intent = new Intent(getApplicationContext(), Profile_Ambulance_List.class);
        startActivity(intent);
    }

    private void organizationList() {
        Intent intent = new Intent(getApplicationContext(), Profile_Organization_List.class);
        startActivity(intent);
    }

    private void profileBloodRequestList() {
        Intent intent = new Intent(getApplicationContext(), Profile_Blood_Request_List.class);
        startActivity(intent);
    }

    private void volunteerPost() {
        Intent intent = new Intent(getApplicationContext(), Profile_Volunteer_List.class);
        startActivity(intent);
    }

    private void profileEditButton() {
        Intent intent = new Intent(getApplicationContext(), Profile_Edit.class);
        startActivity(intent);
    }
}