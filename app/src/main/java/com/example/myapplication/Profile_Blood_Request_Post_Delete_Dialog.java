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

public class Profile_Blood_Request_Post_Delete_Dialog extends AppCompatActivity {
    private AppCompatButton cancelButton, deleteButton;
    private ImageButton backButton, homeButton;
    private DatabaseReference bloodRequestPostDatabase;
    private static final String PREFERENCES = "bloodRequest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_blood_request_post_delete_dialog);

        // Initialize Firebase Database
        bloodRequestPostDatabase = FirebaseDatabase.getInstance().getReference("BloodRequest");

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
        Intent intent = new Intent(getApplicationContext(), Profile_Blood_Request_Post_Edit.class);
        startActivity(intent);
    }

    private void deletePost() {
        // Retrieve user phone number from SharedPreferences
        SharedPreferences bloodRequestPost = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        String p_requestId = bloodRequestPost.getString("p_requestId", null);

        if (p_requestId != null) {
            // Remove user data from Firebase
            bloodRequestPostDatabase.child(p_requestId).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Clear local data from SharedPreferences
                    SharedPreferences.Editor editor = bloodRequestPost.edit();
                    editor.clear();
                    editor.apply();

                    Toast.makeText(getApplicationContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();

                    // Redirect to the main activity or login page
                    Intent intent = new Intent(getApplicationContext(), Profile_Blood_Request_List.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to delete account. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No account found to delete.", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelButton() {
        Intent intent = new Intent(getApplicationContext(), Profile_Blood_Request_Post_Edit.class);
        startActivity(intent);
        finish();
    }
}