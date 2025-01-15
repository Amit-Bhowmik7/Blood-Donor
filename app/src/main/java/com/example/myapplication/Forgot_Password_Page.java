package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Forgot_Password_Page extends AppCompatActivity {
    private TextInputLayout fullName, mobileNo, newPassword;
    private AppCompatButton continueButton;
    private ImageButton backButton, homeButton;
    private DatabaseReference registration_database;
    private static final String TAG = "ForgotPasswordPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_page);

        // Initialize views
        fullName = findViewById(R.id.fullName);
        mobileNo = findViewById(R.id.mobileNumber_input);
        newPassword = findViewById(R.id.newPassword_input);
        continueButton = findViewById(R.id.change_password);
        backButton = findViewById(R.id.back_button);
        homeButton = findViewById(com.hbb20.R.id.home);
        homeButton.setVisibility(View.GONE);

        registration_database = FirebaseDatabase.getInstance().getReference("UsersList");

        // Back button functionality
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LogIn.class);
            startActivity(intent);
        });

        // Continue button to initiate password change
        continueButton.setOnClickListener(v -> passwordChange());
    }

    private void passwordChange() {
        String fullNameInput = fullName.getEditText().getText().toString().trim();
        String phoneNumberInput = mobileNo.getEditText().getText().toString().trim();
        String newPasswordInput = newPassword.getEditText().getText().toString().trim();

        if (!phoneNumberInput.isEmpty()) {
            mobileNo.setError(null);
            mobileNo.setErrorEnabled(false);
            if (!fullNameInput.isEmpty()) {
                fullName.setError(null);
                fullName.setErrorEnabled(false);
                if (!newPasswordInput.isEmpty()) {
                    newPassword.setError(null);
                    newPassword.setErrorEnabled(false);
                    // Check if phone number exists in the database
                    registration_database.child(phoneNumberInput).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            //Log.d(TAG, "DataSnapshot exists: " + snapshot.exists());
                            if (snapshot.exists()) {
                                String storedFullName = snapshot.child("name").getValue(String.class);
                                //Log.d(TAG, "Stored full name: " + storedFullName);
                                if (storedFullName != null && storedFullName.equals(fullNameInput)) {
                                    // Reset password in the database
                                    registration_database.child(phoneNumberInput).child("password").setValue(newPasswordInput);

                                    Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), LogIn.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    fullName.setError("Name does not match");
                                }
                            } else {
                                mobileNo.setError("Mobile number not found please create new account");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    newPassword.setError("Enter new password");
                }
            } else {
                fullName.setError("Enter full name");
            }
        }else {
            mobileNo.setError("Enter mobile number");
        }
    }
}
