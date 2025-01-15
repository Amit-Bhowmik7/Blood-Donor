package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Profile_Edit extends AppCompatActivity {
    String[] districts, bloodGroups, genders;
    private TextInputLayout fullNameEditText, ageEditText, genderEditText, bloodGroupEditText, districtEditText, upazilaEditText, locationEditText, mobileNoEditText, passwordEditText;
    private TextView userName;
    private AppCompatButton update_profile, delete_profile;
    private ImageButton backButton, homeButton;
    DatabaseReference registration_database;
    private static final String PREFERENCES = "UserPrefs";
    private TextInputEditText age_picker_EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        // Connect to database
        registration_database = FirebaseDatabase.getInstance().getReference("UsersList");

        // Find all views
        userName = findViewById(R.id.user_name);
        fullNameEditText = findViewById(R.id.name_input);
        ageEditText = findViewById(R.id.age_input);
        genderEditText = findViewById(R.id.gender_input);
        bloodGroupEditText = findViewById(R.id.bloodGroup_input);
        districtEditText = findViewById(R.id.district_input);
        upazilaEditText = findViewById(R.id.upazila_input);
        locationEditText = findViewById(R.id.location_input);
        mobileNoEditText = findViewById(R.id.mobileNumber_input);
        passwordEditText = findViewById(R.id.password_input);
        age_picker_EditText = findViewById(R.id.age_picker_EditText);


        //backButton = findViewById(R.id.back_button);
        update_profile = findViewById(R.id.update_profile);
        delete_profile = findViewById(R.id.delete_account);
        backButton = findViewById(R.id.back_button);

        homeButton = findViewById(R.id.home);
        homeButton.setOnClickListener(v -> homeButton());

        // Disable direct input in the EditText
        age_picker_EditText.setFocusable(false);
        age_picker_EditText.setOnClickListener(view -> showAgePickerDialog());

        // Populate dropdowns
        setupDropdowns();

        // Back button functionality
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Profile_Page.class);
            startActivity(intent);
        });

        // Retrieve data from SharedPreferences and display
        displayUserProfile();

        // Update profile on button click
        update_profile.setOnClickListener(v -> updateProfile());

        // Delete profile on button click
        delete_profile.setOnClickListener(v -> delete_profile());
    }

    private void homeButton() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void delete_profile() {
        Intent intent = new Intent(getApplicationContext(), Delete_Account_Dailog.class);
        startActivity(intent);
    }

    private void setupDropdowns() {
        // Gender List
        genders = getResources().getStringArray(R.array.genderstring);
        setupDropdown(R.id.gender_dropdown, genders);

        // BloodGroup List
        bloodGroups = getResources().getStringArray(R.array.bloodgroupnamestring);
        setupDropdown(R.id.bloodGroup_dropdown, bloodGroups);

        // District List
        districts = getResources().getStringArray(R.array.districtnamestring);
        setupDropdown(R.id.district_dropdown, districts);
    }

    private void setupDropdown(int viewId, String[] items) {
        AutoCompleteTextView dropdown = findViewById(viewId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setFocusable(false);
        dropdown.setOnClickListener(v -> dropdown.showDropDown());
    }

    private void displayUserProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);


        String name = sharedPreferences.getString("name", "N/A");
        String age = sharedPreferences.getString("age", "N/A");
        String gender = sharedPreferences.getString("gender", "N/A");
        String bloodGroup = sharedPreferences.getString("bloodGroup", "N/A");
        String district = sharedPreferences.getString("district", "N/A");
        String upazila = sharedPreferences.getString("upazila", "N/A");
        String location = sharedPreferences.getString("location", "N/A");
        String phoneNumber = sharedPreferences.getString("mobileNumber","N/A");
        String password = sharedPreferences.getString("password", "N/A");

        userName.setText(name);
        fullNameEditText.getEditText().setText(name);
        ((AutoCompleteTextView) findViewById(R.id.gender_dropdown)).setText(gender, false);
        ((AutoCompleteTextView) findViewById(R.id.bloodGroup_dropdown)).setText(bloodGroup, false);
        ((AutoCompleteTextView) findViewById(R.id.district_dropdown)).setText(district, false);
        ageEditText.getEditText().setText(age);
        upazilaEditText.getEditText().setText(upazila);
        locationEditText.getEditText().setText(location);
        mobileNoEditText.getEditText().setText(phoneNumber);
        passwordEditText.getEditText().setText(password);
    }

    private void updateProfile() {
        String fullNameInput = fullNameEditText.getEditText().getText().toString().trim();
        String ageInput = ageEditText.getEditText().getText().toString().trim();
        String genderInput = genderEditText.getEditText().getText().toString().trim();
        String bloodGroupInput = bloodGroupEditText.getEditText().getText().toString().trim();
        String districtInput = districtEditText.getEditText().getText().toString().trim();
        String upazilaInput = upazilaEditText.getEditText().getText().toString().trim();
        String locationInput = locationEditText.getEditText().getText().toString().trim();
        String phoneNumberInput = mobileNoEditText.getEditText().getText().toString().trim();
        String passwordInput = passwordEditText.getEditText().getText().toString().trim();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String originalPhoneNumber = sharedPreferences.getString("mobileNumber", "N/A");

        // Validation
        if (fullNameInput.isEmpty()) {
            fullNameEditText.setError("Please enter your full name");
            return;
        }else {
            fullNameEditText.setError(null);
            fullNameEditText.setErrorEnabled(false);
        }
        if (ageInput.isEmpty()) {
            ageEditText.setError("Please select your age");
            return;
        }
        if (genderInput.isEmpty()) {
            genderEditText.setError("Please select your gender");
            return;
        }
        if (bloodGroupInput.isEmpty()) {
            bloodGroupEditText.setError("Please select your blood group");
            return;
        }
        if (districtInput.isEmpty()) {
            districtEditText.setError("Please select your district");
            return;
        }
        if (upazilaInput.isEmpty()) {
            upazilaEditText.setError("Please enter your upazila");
            return;
        }else {
            upazilaEditText.setError(null);
            upazilaEditText.setErrorEnabled(false);
        }
        if (locationInput.isEmpty()) {
            locationEditText.setError("Please enter your location");
            return;
        }else {
            locationEditText.setError(null);
            locationEditText.setErrorEnabled(false);
        }
        if (phoneNumberInput.isEmpty()) {
            mobileNoEditText.setError("Please enter your mobile number");
            return;
        }else {
            mobileNoEditText.setError(null);
            mobileNoEditText.setErrorEnabled(false);
        }
        if (passwordInput.isEmpty()) {
            passwordEditText.setError("Please enter your password");
            return;
        }else {
            passwordEditText.setError(null);
            passwordEditText.setErrorEnabled(false);
        }

        if (!originalPhoneNumber.equals(phoneNumberInput)) {
            registration_database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(phoneNumberInput)) {
                        mobileNoEditText.setError("This phone number is already registered");
                    } else {
                        mobileNoEditText.setError(null);
                        mobileNoEditText.setErrorEnabled(false);
                        registration_database.child(originalPhoneNumber).get()
                                .addOnSuccessListener(dataSnapshot -> {
                                    registration_database.child(phoneNumberInput).setValue(dataSnapshot.getValue());
                                    registration_database.child(phoneNumberInput).child("phoneNumber").setValue(phoneNumberInput);
                                    registration_database.child(originalPhoneNumber).removeValue();
                                    updatePreferences(editor, fullNameInput, ageInput, genderInput, bloodGroupInput, districtInput, upazilaInput, locationInput, phoneNumberInput, passwordInput);
                                    Toast.makeText(Profile_Edit.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Profile_Edit.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            registration_database.child(phoneNumberInput).child("name").setValue(fullNameInput);
            registration_database.child(phoneNumberInput).child("age").setValue(ageInput);
            registration_database.child(phoneNumberInput).child("gender").setValue(genderInput);
            registration_database.child(phoneNumberInput).child("bloodGroup").setValue(bloodGroupInput);
            registration_database.child(phoneNumberInput).child("district").setValue(districtInput);
            registration_database.child(phoneNumberInput).child("upazila").setValue(upazilaInput);
            registration_database.child(phoneNumberInput).child("location").setValue(locationInput);
            registration_database.child(phoneNumberInput).child("password").setValue(passwordInput);
            updatePreferences(editor, fullNameInput, ageInput, genderInput, bloodGroupInput, districtInput, upazilaInput, locationInput, phoneNumberInput, passwordInput);
            Toast.makeText(Profile_Edit.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePreferences(SharedPreferences.Editor editor, String name, String age, String gender, String bloodGroup, String district, String upazila, String location, String phoneNumber, String password) {
        editor.putString("name", name);
        editor.putString("age", age);
        editor.putString("gender", gender);
        editor.putString("bloodGroup", bloodGroup);
        editor.putString("district", district);
        editor.putString("upazila", upazila);
        editor.putString("location", location);
        editor.putString("mobileNumber", phoneNumber);
        editor.putString("password", password);
        editor.apply();
        userName.setText(name);
    }

    private void showAgePickerDialog() {
        final Calendar calendar = Calendar.getInstance();

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Profile_Edit.this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    int age = calculateAge(year, month, dayOfMonth);
                    age_picker_EditText.setText(String.valueOf(age));
                },
                currentYear,
                currentMonth,
                currentDay
        );

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private int calculateAge(int year, int month, int dayOfMonth) {
        final Calendar today = Calendar.getInstance();
        int currentYear = today.get(Calendar.YEAR);
        int currentMonth = today.get(Calendar.MONTH);
        int currentDay = today.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - year;

        if (currentMonth < month || (currentMonth == month && currentDay < dayOfMonth)) {
            age--;
        }

        return age;
    }
}
