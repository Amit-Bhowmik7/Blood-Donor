package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Organization_Post_Delete_Dialog extends AppCompatActivity {
    private AppCompatButton cancelButton, deleteButton;
    private ImageButton backButton, homeButton;
    private DatabaseReference organizationRequestPostDatabase;
    private static final String PREFERENCES = "organizationRequest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_post_delete_dialog);

        // Initialize Firebase Database
        organizationRequestPostDatabase = FirebaseDatabase.getInstance().getReference("OrganizationList");

        // Initialize buttons
        cancelButton = findViewById(R.id.cancel_delete);
        deleteButton = findViewById(R.id.delete_post);
        backButton = findViewById(R.id.back_button);
        homeButton = findViewById(R.id.home);


        // Set button listeners
        cancelButton.setOnClickListener(v -> cancelButton());
        deleteButton.setOnClickListener(v -> deletePost());
        backButton.setOnClickListener(v -> backButton());
        homeButton.setOnClickListener(v -> homeButton());
    }

    private void homeButton() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void backButton() {
        Intent intent = new Intent(getApplicationContext(), Organization_Post_Edit.class);
        startActivity(intent);
    }

    private void deletePost() {
        // Retrieve user phone number from SharedPreferences
        SharedPreferences volunteerRequestPost = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        String mobileNumber = volunteerRequestPost.getString("o_mobileNumber", null);

        if (mobileNumber != null) {
            // Remove user data from Firebase
            organizationRequestPostDatabase.child(mobileNumber).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Clear local data from SharedPreferences
                    SharedPreferences.Editor editor = volunteerRequestPost.edit();
                    editor.clear();
                    editor.apply();

                    Toast.makeText(getApplicationContext(), "Post deleted successfully", Toast.LENGTH_SHORT).show();

                    // Redirect to the main activity or login page
                    Intent intent = new Intent(getApplicationContext(), Organization_List.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to delete post. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No post found to delete.", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelButton() {
        Intent intent = new Intent(getApplicationContext(), Organization_Post_Edit.class);
        startActivity(intent);
        finish();
    }
}