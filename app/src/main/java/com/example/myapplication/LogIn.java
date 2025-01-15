package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout phoneEditText, passwordEditText;
    private Button registration, signinbutton,forgotPasswordButton;
    //private AppCompatButton registration, signinbutton;
    private static final String PREFERENCES = "UserPrefs";
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER_ME = "remember_me";

    private CheckBox rememberMeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //field find
        phoneEditText = findViewById(R.id.number_input);
        passwordEditText = findViewById(R.id.password_input);
        signinbutton = findViewById(R.id.signinbutton);
        registration = findViewById(R.id.registrationbuttton);
        rememberMeCheckBox = findViewById(R.id.remember_me_checkbox); // Add this to your layout

        // Create a ColorStateList for the button tint
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked}, // Checked state
                        new int[]{} // Default state
                },
                new int[]{
                        ContextCompat.getColor(this, R.color.green), // Checked color
                        ContextCompat.getColor(this, R.color.ash)    // Default color
                }
        );
        // Apply the ColorStateList to the CheckBox
        rememberMeCheckBox.setButtonTintList(colorStateList);

        // Load saved credentials if "Remember Me checkbox" was previously checked
        loadSavedCredentials();

        //set listener
        signinbutton.setOnClickListener(this);
        registration.setOnClickListener(this);

        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        forgotPasswordButton.setOnClickListener(v -> forgotPassword());
    }

    private void forgotPassword() {
        Intent intent = new Intent(getApplicationContext(), Forgot_Password_Page.class);
        startActivity(intent);
    }

    private void loadSavedCredentials() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isRememberMeChecked = preferences.getBoolean(KEY_REMEMBER_ME, false);

        if (isRememberMeChecked) {
            String savedPhone = preferences.getString(KEY_PHONE, "");
            String savedPassword = preferences.getString(KEY_PASSWORD, "");

            phoneEditText.getEditText().setText(savedPhone);
            passwordEditText.getEditText().setText(savedPassword);
            rememberMeCheckBox.setChecked(true);
        }
    }

    private void saveCredentials(boolean rememberMe) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (rememberMe) {
            editor.putBoolean(KEY_REMEMBER_ME, true);
            editor.putString(KEY_PHONE, phoneEditText.getEditText().getText().toString());
            editor.putString(KEY_PASSWORD, passwordEditText.getEditText().getText().toString());
        } else {
            editor.clear(); // Clear saved credentials
        }
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signinbutton:

                String phoneNo = phoneEditText.getEditText().getText().toString().trim();
                String passwordNo = passwordEditText.getEditText().getText().toString().trim();

                if ((!phoneNo.isEmpty() & phoneNo.length() == 11)) {
                    phoneEditText.setError(null);
                    phoneEditText.setErrorEnabled(false);
                    if (!passwordNo.isEmpty()) {
                        passwordEditText.setError(null);
                        passwordEditText.setErrorEnabled(false);

                        final String phoneNoData = phoneEditText.getEditText().getText().toString().trim();
                        final String passwordData = passwordEditText.getEditText().getText().toString().trim();

                        DatabaseReference database = FirebaseDatabase.getInstance().getReference("UsersList");

                        Query checkPhoneNo = database.orderByChild("phoneNumber").equalTo(phoneNoData);

                        checkPhoneNo.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    phoneEditText.setError(null);
                                    phoneEditText.setErrorEnabled(false);
                                    String checkPassword = snapshot.child(phoneNoData).child("password").getValue(String.class);
                                    if (checkPassword.equals(passwordData)) {
                                        passwordEditText.setError(null);
                                        passwordEditText.setErrorEnabled(false);

                                        // Save credentials if "Remember Me" is checked
                                        saveCredentials(rememberMeCheckBox.isChecked());

                                        Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                                        //get data from firebase data storage
                                        String name = snapshot.child(phoneNoData).child("name").getValue(String.class);
                                        String age = snapshot.child(phoneNoData).child("age").getValue(String.class);
                                        String gender = snapshot.child(phoneNoData).child("gender").getValue(String.class);
                                        String bloodGroup = snapshot.child(phoneNoData).child("bloodGroup").getValue(String.class);
                                        String district = snapshot.child(phoneNoData).child("district").getValue(String.class);
                                        String upazila = snapshot.child(phoneNoData).child("upazila").getValue(String.class);
                                        String location = snapshot.child(phoneNoData).child("location").getValue(String.class);
                                        String mobileNumber = snapshot.child(phoneNoData).child("phoneNumber").getValue(String.class);
                                        String password = snapshot.child(phoneNoData).child("password").getValue(String.class);

                                        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("name", name);
                                        editor.putString("age", age);
                                        editor.putString("gender", gender);
                                        editor.putString("bloodGroup", bloodGroup);
                                        editor.putString("district", district);
                                        editor.putString("upazila", upazila);
                                        editor.putString("location", location);
                                        editor.putString("mobileNumber", mobileNumber);
                                        editor.putString("password", password);
                                        editor.apply();

                                        startActivity(intent);
                                        finish();
                                    } else {
                                        passwordEditText.setError("Wrong password");
                                    }
                                } else {
                                    phoneEditText.setError("User not registered");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        passwordEditText.setError("Enter your registered password");
                    }
                } else {
                    phoneEditText.setError("Enter your registered phone number");
                }
                break;

            case R.id.registrationbuttton:
                Intent intent = new Intent(LogIn.this, registration_1st_page.class);
                startActivity(intent);
                break;
        }
    }
}