package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Blood_Request_Post_Edit extends AppCompatActivity {
    String[] districts, bloodGroups, bloodQuantitys;
    private ImageButton backButton, homeButton;
    private Button updateButton, deleteButton;
    private TextInputLayout nameEditText, districtEditText, bloodGroupEditText, bloodQuantityEditText, dateEditText, timeEditText, diseaseEditText, hospitalNameEditText, hospitalLocationEditText, mobileEditText, identifyNumberEditText, accountNumberEditText;
    private TextInputEditText date_picker_EditText, time_picker_EditText;
    private AutoCompleteTextView districtDropdown, bloodGroupDropdown, bloodQuantityDropdown;
    DatabaseReference requestDatabase;
    private static final String PREFERENCES = "bloodRequest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request_post_edit);

        requestDatabase = FirebaseDatabase.getInstance().getReference("BloodRequest");

        // Find all input field IDs
        nameEditText = findViewById(R.id.name_input);
        districtEditText = findViewById(R.id.district_input);
        bloodGroupEditText = findViewById(R.id.bloodGroup_input);
        bloodQuantityEditText = findViewById(R.id.blood_quantity_input);
        dateEditText = findViewById(R.id.date_input);
        timeEditText = findViewById(R.id.time_input);
        diseaseEditText = findViewById(R.id.disease_input);
        hospitalNameEditText = findViewById(R.id.hospital_name_input);
        hospitalLocationEditText = findViewById(R.id.hospital_location_input);
        mobileEditText = findViewById(R.id.mobileNumber_input);

        //find all button id
        updateButton = findViewById(R.id.update_post);
        deleteButton = findViewById(R.id.delete_post);
        backButton = findViewById(R.id.back_button);
        homeButton = findViewById(R.id.home);

        // Initialize AutoCompleteTextViews for districts, blood groups, and blood quantities
        districtDropdown = findViewById(R.id.district_dropdown);
        bloodGroupDropdown = findViewById(R.id.bloodGroup_dropdown);
        bloodQuantityDropdown = findViewById(R.id.bloodQuantity_dropdown);

        // To hide
        homeButton.setVisibility(View.GONE);

        //data and time picker id
        date_picker_EditText = findViewById(R.id.date_pickerEditText);
        time_picker_EditText = findViewById(R.id.time_picker_EditText);

        // District List spinner
        districts = getResources().getStringArray(R.array.districtnamestring);
        AutoCompleteTextView districtDropdown = findViewById(R.id.district_dropdown);
        // ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, districts);
        // Set Adapter
        districtDropdown.setAdapter(adapter);
        districtDropdown.setFocusable(false);
        // Show dropdown on click
        districtDropdown.setOnClickListener(v -> districtDropdown.showDropDown());
        // Handle Selection
        districtDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDistrict = (String) parent.getItemAtPosition(position);
        });

        // bloodGroup List spinner
        bloodGroups = getResources().getStringArray(R.array.bloodgroupnamestring);
        AutoCompleteTextView bloodGroupDropdown = findViewById(R.id.bloodGroup_dropdown);
        // ArrayAdapter
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bloodGroups);
        // Set Adapter
        bloodGroupDropdown.setAdapter(adapter1);
        bloodGroupDropdown.setFocusable(false);
        // Show dropdown on click
        bloodGroupDropdown.setOnClickListener(v -> bloodGroupDropdown.showDropDown());
        // Handle Selection
        bloodGroupDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedBloodGroup = (String) parent.getItemAtPosition(position);
        });


        // blood quantity List spinner
        bloodQuantitys = getResources().getStringArray(R.array.bloodQuantitys);
        AutoCompleteTextView bloodQuantityDropdown = findViewById(R.id.bloodQuantity_dropdown);
        // ArrayAdapter
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bloodQuantitys);
        // Set Adapter
        bloodQuantityDropdown.setAdapter(adapter2);
        bloodQuantityDropdown.setFocusable(false);
        // Show dropdown on click
        bloodQuantityDropdown.setOnClickListener(v -> bloodQuantityDropdown.showDropDown());
        // Handle Selection
        bloodQuantityDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedBloodGroup = (String) parent.getItemAtPosition(position);
        });


        // Disable direct input in the EditText
        date_picker_EditText.setFocusable(false);
        // Set up Date Picker Dialog function call
        date_picker_EditText.setOnClickListener(view -> showDatePickerDialog());

        // Disable direct input in the EditText
        time_picker_EditText.setFocusable(false);
        // Set up Time Picker Dialog
        time_picker_EditText.setOnClickListener(view -> showTimePickerDialog());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Blood_Request_List.class);
                startActivity(intent);
            }
        });

        displayRequestPost();

        updateButton.setOnClickListener(v -> updatepost());
        deleteButton.setOnClickListener(v -> deletePost());

    }

    private void deletePost() {
        Intent intent = new Intent(getApplicationContext(), Blood_Request_post_Delete_Dailog.class);
        startActivity(intent);
    }

    private void updatepost() {

        String fullNameInput = nameEditText.getEditText().getText().toString().trim();
        String districtInput = districtEditText.getEditText().getText().toString().trim();
        String bloodGroupInput = bloodGroupEditText.getEditText().getText().toString().trim();
        String bloodQuantityInput = bloodQuantityEditText.getEditText().getText().toString().trim();
        String dateInput = dateEditText.getEditText().getText().toString().trim();
        String timeInput = timeEditText.getEditText().getText().toString().trim();
        String diseaseInput = diseaseEditText.getEditText().getText().toString().trim();
        String hospital_NameInput = hospitalNameEditText.getEditText().getText().toString().trim();
        String hospital_LocationInput = hospitalLocationEditText.getEditText().getText().toString().trim();
        String mobileNumberInput = mobileEditText.getEditText().getText().toString().trim();


        SharedPreferences bloodRequestPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor requestEditor = bloodRequestPreferences.edit();
        String requestId = bloodRequestPreferences.getString("p_requestId", "N/A");

        //validation check
        if (!fullNameInput.isEmpty()) {
            nameEditText.setError(null);
            nameEditText.setErrorEnabled(false);
            if (!districtInput.isEmpty()) {
                districtEditText.setError(null);
                districtEditText.setErrorEnabled(false);
                if (!bloodGroupInput.isEmpty()) {
                    bloodGroupEditText.setError(null);
                    bloodGroupEditText.setErrorEnabled(false);
                    if (!bloodQuantityInput.isEmpty()) {
                        bloodQuantityEditText.setError(null);
                        bloodQuantityEditText.setErrorEnabled(false);
                        if (!dateInput.isEmpty()) {
                            dateEditText.setError(null);
                            dateEditText.setErrorEnabled(false);
                            if (!timeInput.isEmpty()) {
                                timeEditText.setError(null);
                                timeEditText.setErrorEnabled(false);
                                if (!diseaseInput.isEmpty()) {
                                    diseaseEditText.setError(null);
                                    diseaseEditText.setErrorEnabled(false);
                                    if (!hospital_NameInput.isEmpty()) {
                                        hospitalNameEditText.setError(null);
                                        hospitalNameEditText.setErrorEnabled(false);
                                        if (!hospital_LocationInput.isEmpty()) {
                                            hospitalLocationEditText.setError(null);
                                            hospitalLocationEditText.setErrorEnabled(false);
                                            if (!mobileNumberInput.isEmpty() && isValidBangladeshiPhoneNumber(mobileNumberInput)) {
                                                mobileEditText.setError(null);
                                                mobileEditText.setErrorEnabled(false);
                                                if (requestId != null) {

                                                    //stored database function
                                                    requestDatabase = FirebaseDatabase.getInstance().getReference("BloodRequest");

                                                    requestDatabase.child(requestId).child("fullName").setValue(fullNameInput);
                                                    requestDatabase.child(requestId).child("district").setValue(districtInput);
                                                    requestDatabase.child(requestId).child("bloodGroup").setValue(bloodGroupInput);
                                                    requestDatabase.child(requestId).child("bloodQuantity").setValue(bloodQuantityInput);
                                                    requestDatabase.child(requestId).child("date").setValue(dateInput);
                                                    requestDatabase.child(requestId).child("time").setValue(timeInput);
                                                    requestDatabase.child(requestId).child("disease").setValue(diseaseInput);
                                                    requestDatabase.child(requestId).child("hospitalName").setValue(hospital_NameInput);
                                                    requestDatabase.child(requestId).child("hospitalLocation").setValue(hospital_LocationInput);
                                                    requestDatabase.child(requestId).child("requestId").setValue(requestId);
                                                    requestDatabase.child(requestId).child("mobileNumber").setValue(mobileNumberInput);

                                                    requestEditor.putString("p_name", fullNameInput);
                                                    requestEditor.putString("p_district", districtInput);
                                                    requestEditor.putString("p_bloodGroup", bloodGroupInput);
                                                    requestEditor.putString("p_bloodQuantity", bloodQuantityInput);
                                                    requestEditor.putString("p_date", dateInput);
                                                    requestEditor.putString("p_time", timeInput);
                                                    requestEditor.putString("p_disease", diseaseInput);
                                                    requestEditor.putString("p_hospital_Name", hospital_NameInput);
                                                    requestEditor.putString("p_hospital_Location", hospital_LocationInput);
                                                    requestEditor.putString("p_requestId", requestId);
                                                    requestEditor.putString("p_mobileNumber", mobileNumberInput);
                                                    requestEditor.apply();

                                                    Toast.makeText(getApplicationContext(), "Update successfully", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(getApplicationContext(), Blood_Request_List.class);
                                                    startActivity(intent);

                                                } else {
                                                }
                                            } else {
                                                mobileEditText.setError("Please enter your valid mobile number");
                                            }
                                        } else {
                                            hospitalLocationEditText.setError("Please enter hospital address");
                                        }
                                    } else {
                                        hospitalNameEditText.setError("Please enter hospital name");
                                    }
                                } else {
                                    diseaseEditText.setError("Please enter your disease");
                                }
                            } else {
                                timeEditText.setError("Please select time");
                            }
                        } else {
                            dateEditText.setError("Please select date");
                        }
                    } else {
                        bloodGroupEditText.setError("Please select your blood group");
                    }
                } else {
                    bloodQuantityEditText.setError("Please select blood quantity");
                }
            } else {
                districtEditText.setError("Please select your district");
            }
        } else {
            nameEditText.setError("Please enter your full name");
        }
    }

    //bangladeshi mobile pattern
    private boolean isValidBangladeshiPhoneNumber(String phoneNumber) {
        // Check if the phone number is 11 digits long and starts with "01"
        return phoneNumber.matches("^01[3-9]\\d{8}$");
    }
    private void displayRequestPost() {
        // Get data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);

        String name = sharedPreferences.getString("p_name", "N/A");
        String district = sharedPreferences.getString("p_district", "N/A");
        String bloodGroup = sharedPreferences.getString("p_bloodGroup", "N/A");
        String bloodQuantity = sharedPreferences.getString("p_bloodQuantity", "N/A");
        String date = sharedPreferences.getString("p_date", "N/A");
        String time = sharedPreferences.getString("p_time", "N/A");
        String disease = sharedPreferences.getString("p_disease", "N/A");
        String hospitalName = sharedPreferences.getString("p_hospital_Name", "N/A");
        String hospitalLocation = sharedPreferences.getString("p_hospital_Location", "N/A");
        String mobileNumber = sharedPreferences.getString("p_mobileNumber", "N/A");

        // Set values to the EditText and AutoCompleteTextView
        nameEditText.getEditText().setText(name);
        districtDropdown.setText(district, false);
        bloodGroupDropdown.setText(bloodGroup, false);
        bloodQuantityDropdown.setText(bloodQuantity, false);
        dateEditText.getEditText().setText(date);
        timeEditText.getEditText().setText(time);
        diseaseEditText.getEditText().setText(disease);
        hospitalNameEditText.getEditText().setText(hospitalName);
        hospitalLocationEditText.getEditText().setText(hospitalLocation);
        mobileEditText.getEditText().setText(mobileNumber);
    }

    //Date picker function
    private void showDatePickerDialog() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();

        // Create Date Picker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Blood_Request_Post_Edit.this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    // Format the selected date
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    String selectedDate = dateFormat.format(calendar.getTime());

                    // Set the formatted date to the EditText
                    date_picker_EditText.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Show the dialog
        datePickerDialog.show();
    }

    //Time picker function
    private void showTimePickerDialog() {
        // Get current time
        final Calendar calendar = Calendar.getInstance();

        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // Create Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                Blood_Request_Post_Edit.this,
                (TimePicker view, int hourOfDay, int minute) -> {
                    // Format the selected time
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);

                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a", Locale.getDefault());
                    String selectedTime = timeFormat.format(calendar.getTime());

                    // Set the formatted time to the EditText
                    time_picker_EditText.setText(selectedTime);
                },
                currentHour,
                currentMinute,
                true // Use 24-hour format (set false for 12-hour format)
        );

        // Show the dialog
        timePickerDialog.show();
    }

}