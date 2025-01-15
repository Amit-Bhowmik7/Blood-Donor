package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class registration_1st_page extends AppCompatActivity implements View.OnClickListener {
    String[] districts,bloodGroups,genders;
    private TextInputLayout fullNameEditText, ageEditText, genderEditText, bloodGroupEditText, districtEditText, upazilaEditText, locationEditText, phoneNoEditText, passwordEditText;
    private AppCompatCheckBox checkBox;

    private TextInputEditText age_picker_EditText;
    DatabaseReference registration_database;

    //count users start
    private static final String PREFERENCES = "UserPrefs"; // SharedPreferences file
    private static final String TOTAL_USERS_KEY = "totalUsers";
    private static final String TOTAL_DONORS_KEY = "totalDonors";
    //count users end

    //button
    private Button registration_save;
    private ImageButton backButton, homeButton;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_1st_page);

        //Find All Id from the registration field
        fullNameEditText = findViewById(R.id.name_input);
        ageEditText = findViewById(R.id.age_input);
        genderEditText = findViewById(R.id.gender_input);
        bloodGroupEditText = findViewById(R.id.bloodGroup_input);
        districtEditText = findViewById(R.id.district_input);
        upazilaEditText = findViewById(R.id.upazila_input);
        locationEditText = findViewById(R.id.location_input);
        phoneNoEditText = findViewById(R.id.phoneNumber_input);
        passwordEditText = findViewById(R.id.password_input);
        checkBox = findViewById(R.id.checkbox_input);
        age_picker_EditText = findViewById(R.id.age_picker_EditText);

        //Button find
        registration_save = findViewById(R.id.registrationsave);
        backButton = findViewById(R.id.back_button);

        homeButton = findViewById(R.id.home);
        // To hide
        homeButton.setVisibility(View.GONE);

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
        checkBox.setButtonTintList(colorStateList);

        // Disable direct input in the EditText
        age_picker_EditText.setFocusable(false);
        // Set up Date Picker Dialog
        age_picker_EditText.setOnClickListener(view -> showAgePickerDialog());

        // Gender List
        genders = getResources().getStringArray(R.array.genderstring);
        AutoCompleteTextView genderDropdown = findViewById(R.id.gender_dropdown);
        // ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        // Set Adapter
        genderDropdown.setAdapter(adapter);
        genderDropdown.setFocusable(false);
        // Show dropdown on click
        genderDropdown.setOnClickListener(v -> genderDropdown.showDropDown());
        // Handle Selection
        genderDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedGender= (String) parent.getItemAtPosition(position);
        });

        // bloodGroup List
        bloodGroups = getResources().getStringArray(R.array.bloodgroupnamestring);
        AutoCompleteTextView bloodGroupDropdown = findViewById(R.id.bloodGroup_dropdown);
        // ArrayAdapter
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bloodGroups);
        // Set Adapter
        bloodGroupDropdown.setAdapter(adapter2);
        bloodGroupDropdown.setFocusable(false);
        // Show dropdown on click
        bloodGroupDropdown.setOnClickListener(v -> bloodGroupDropdown.showDropDown());
        // Handle Selection
        bloodGroupDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedBloodGroup = (String) parent.getItemAtPosition(position);
        });

        // District List
        districts = getResources().getStringArray(R.array.districtnamestring);
        AutoCompleteTextView districtDropdown = findViewById(R.id.district_dropdown);
        // ArrayAdapter
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, districts);
        // Set Adapter
        districtDropdown.setAdapter(adapter1);
        districtDropdown.setFocusable(false);
        // Show dropdown on click
        districtDropdown.setOnClickListener(v -> districtDropdown.showDropDown());
        // Handle Selection
        districtDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDistrict = (String) parent.getItemAtPosition(position);
        });

        // Function call for user input data
        registration_save.setOnClickListener(this);
        //back button call
        backButton.setOnClickListener(this);

    }


    //age picker function with age calculator into calendar
    private void showAgePickerDialog() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Create Date Picker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                registration_1st_page.this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    // Calculate age
                    int age = calculateAge(year, month, dayOfMonth);

                    // Set the age to the EditText
                    age_picker_EditText.setText(String.valueOf(age));
                },
                currentYear,
                currentMonth,
                currentDay
        );

        // Limit the date to prevent selecting future dates
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        // Show the dialog
        datePickerDialog.show();
    }

    private int calculateAge(int year, int month, int dayOfMonth) {
        final Calendar today = Calendar.getInstance();
        int currentYear = today.get(Calendar.YEAR);
        int currentMonth = today.get(Calendar.MONTH);
        int currentDay = today.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - year;

        // Adjust age if the birthday hasn't occurred this year yet
        if (currentMonth < month || (currentMonth == month && currentDay < dayOfMonth)) {
            age--;
        }

        return age;
    }

    //Bangladeshi number pattern for input number
    private boolean isValidBangladeshiPhoneNumber(String phoneNumber) {
        // Check if the phone number is 11 digits long and starts with "01"
        return phoneNumber.matches("^01[3-9]\\d{8}$");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                Intent intent  = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                break;
            case R.id.registrationsave:

                String fullName = fullNameEditText.getEditText().getText().toString().trim();
                String age = ageEditText.getEditText().getText().toString().trim();
                String gender = genderEditText.getEditText().getText().toString().trim();
                String bloodGroup = bloodGroupEditText.getEditText().getText().toString().trim();
                String district = districtEditText.getEditText().getText().toString().trim();
                String upazila = upazilaEditText.getEditText().getText().toString().trim();
                String location = locationEditText.getEditText().getText().toString().trim();
                String phoneNumber = phoneNoEditText.getEditText().getText().toString().trim();
                String password = passwordEditText.getEditText().getText().toString().trim();

                boolean asADonor = checkBox.isChecked();


                // Validation
                if ((!fullName.isEmpty())) {
                    fullNameEditText.setError(null);
                    fullNameEditText.setErrorEnabled(false);
                    if (!age.isEmpty()) {
                        ageEditText.setError(null);
                        ageEditText.setErrorEnabled(false);
                        if (!gender.isEmpty()) {
                            genderEditText.setError(null);
                            genderEditText.setErrorEnabled(false);
                            if (!bloodGroup.isEmpty()) {
                                bloodGroupEditText.setError(null);
                                bloodGroupEditText.setErrorEnabled(false);
                                if (!district.isEmpty()) {
                                    districtEditText.setError(null);
                                    districtEditText.setErrorEnabled(false);
                                    if (!upazila.isEmpty()) {
                                        upazilaEditText.setError(null);
                                        upazilaEditText.setErrorEnabled(false);
                                        if (!location.isEmpty()) {
                                            locationEditText.setError(null);
                                            locationEditText.setErrorEnabled(false);
                                            if (!phoneNumber.isEmpty() && isValidBangladeshiPhoneNumber(phoneNumber)) {
                                                phoneNoEditText.setError(null);
                                                phoneNoEditText.setErrorEnabled(false);
                                                if (!password.isEmpty() & password.length()>=4) {
                                                    passwordEditText.setError(null);
                                                    passwordEditText.setErrorEnabled(false);
                                                    if(!checkBox.isChecked()){
                                                        boolean as_A_donor=false;
                                                        registration_database = FirebaseDatabase.getInstance().getReference("UsersList");
                                                        registration_database.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if(!snapshot.hasChild(phoneNumber)){
                                                                    phoneNoEditText.setError(null);
                                                                    phoneNoEditText.setErrorEnabled(false);
                                                                    New_User_Registration_Handler newUser = new New_User_Registration_Handler(fullName, age, gender, bloodGroup, district, upazila, location, phoneNumber, password,as_A_donor);
                                                                    registration_database.child(phoneNumber).setValue(newUser);

                                                                    //total users count function call
                                                                    updateUserCounts(asADonor);

                                                                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(registration_1st_page.this, MainActivity.class);

                                                                    SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
                                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                    editor.putString("name", fullName);
                                                                    editor.putString("age", age);
                                                                    editor.putString("gender", gender);
                                                                    editor.putString("bloodGroup", bloodGroup);
                                                                    editor.putString("district", district);
                                                                    editor.putString("upazila", upazila);
                                                                    editor.putString("location", location);
                                                                    editor.putString("mobileNumber", phoneNumber);
                                                                    editor.putString("password", password);
                                                                    editor.apply();

                                                                    startActivity(intent);
                                                                    finish();

                                                                }else {
                                                                    phoneNoEditText.setError("Phone number is already registered");
                                                                }
                                                            }
                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    }else {
                                                        boolean as_A_donor=true;
                                                        registration_database = FirebaseDatabase.getInstance().getReference("UsersList");
                                                        registration_database.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if(!snapshot.hasChild(phoneNumber)){
                                                                    phoneNoEditText.setError(null);
                                                                    phoneNoEditText.setErrorEnabled(false);
                                                                    New_User_Registration_Handler newUser = new New_User_Registration_Handler(fullName, age, gender, bloodGroup, district, upazila, location, phoneNumber, password,as_A_donor);
                                                                    registration_database.child(phoneNumber).setValue(newUser);

                                                                    //total users count function call
                                                                    updateUserCounts(asADonor);

                                                                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(registration_1st_page.this, MainActivity.class);

                                                                    SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
                                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                    editor.putString("name", fullName);
                                                                    editor.putString("age", age);
                                                                    editor.putString("gender", gender);
                                                                    editor.putString("bloodGroup", bloodGroup);
                                                                    editor.putString("district", district);
                                                                    editor.putString("upazila", upazila);
                                                                    editor.putString("location", location);
                                                                    editor.putString("mobileNumber", phoneNumber);
                                                                    editor.putString("password", password);
                                                                    editor.apply();

                                                                    startActivity(intent);
                                                                    finish();

                                                                    }else {
                                                                        phoneNoEditText.setError("Phone number is already registered");
                                                                    }
                                                                }
                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                    }

                                                } else {
                                                    passwordEditText.setError("Please enter minimum 4 digit password");
                                                }
                                            } else {
                                                phoneNoEditText.setError("Please enter a valid Bangladeshi phone number (e.g., 019XXXXXXXX)");
                                            }
                                        } else {
                                            locationEditText.setError("Please enter your location");
                                        }
                                    } else {
                                        upazilaEditText.setError("Please enter your upazila");
                                    }
                                } else {
                                    districtEditText.setError("Please select your district");
                                }
                            } else {
                                bloodGroupEditText.setError("Please select your bloodGroup");
                            }
                        } else {
                            genderEditText.setError("Please select your gender");
                        }
                    } else {
                        ageEditText.setError("Please enter your age");
                    }
                }else {
                    fullNameEditText.setError("Please enter your full name");
                }
                break;
        }
    }

    //total users and donors function
    private void updateUserCounts(boolean isDonor) {
        DatabaseReference countsRef = FirebaseDatabase.getInstance().getReference("UserCounts");

        countsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalUsers = snapshot.child("totalUsers").getValue(Integer.class) != null
                        ? snapshot.child("totalUsers").getValue(Integer.class)
                        : 0;
                int totalDonors = snapshot.child("totalDonors").getValue(Integer.class) != null
                        ? snapshot.child("totalDonors").getValue(Integer.class)
                        : 0;

                totalUsers++;
                if (isDonor) {
                    totalDonors++;
                }

                countsRef.child("totalUsers").setValue(totalUsers);
                countsRef.child("totalDonors").setValue(totalDonors);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error updating counts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput(String fullName, String age, String gender, String bloodGroup, String district, String upazila, String location, String phoneNumber, String password) {
        // Perform field validations as before
        // Return true if all validations pass, otherwise return false
        return false;
    }
}