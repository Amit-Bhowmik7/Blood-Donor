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
import java.util.UUID;

public class Blood_Request_Send extends AppCompatActivity {
    String[] districts ,bloodGroups, bloodQuantitys;
    private ImageButton backButton, homeButton;
    private Button send_request;
    private TextInputLayout nameEditText, districtEditText, bloodGroupEditText, bloodQuantityEditText, dateEditText, timeEditText, diseaseEditText, hospitalNameEditText, hospitalLocationEditText, mobileEditText;
    private TextInputEditText date_picker_EditText, time_picker_EditText;
    DatabaseReference requestDatabase;
    private static final String PREFERENCES = "UserPrefs";
    private static final String bloodRequest = "bloodRequest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request_send);

        //find all input field id
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
        send_request = findViewById(R.id.send_request);
        backButton = findViewById(R.id.back_button);
        homeButton = findViewById(R.id.home);

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
                Intent intent = new Intent(Blood_Request_Send.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //displayProfileAccountNumber();

        //data stored and get from database
        send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = nameEditText.getEditText().getText().toString().trim();
                String district = districtEditText.getEditText().getText().toString().trim();
                String bloodGroup = bloodGroupEditText.getEditText().getText().toString().trim();
                String bloodQuantity = bloodQuantityEditText.getEditText().getText().toString().trim();
                String date = dateEditText.getEditText().getText().toString().trim();
                String time = timeEditText.getEditText().getText().toString().trim();
                String disease = diseaseEditText.getEditText().getText().toString().trim();
                String hospital_Name = hospitalNameEditText.getEditText().getText().toString().trim();
                String hospital_Location = hospitalLocationEditText.getEditText().getText().toString().trim();
                String mobileNumber = mobileEditText.getEditText().getText().toString().trim();
                String requestId = UUID.randomUUID().toString();

                SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
                String accountNumber = sharedPreferences.getString("mobileNumber","N/A");

                //validation check
                if (!fullName.isEmpty()){
                    nameEditText.setError(null);
                    nameEditText.setErrorEnabled(false);
                    if (!district.isEmpty()){
                        districtEditText.setError(null);
                        districtEditText.setErrorEnabled(false);
                        if (!bloodGroup.isEmpty()){
                            bloodGroupEditText.setError(null);
                            bloodGroupEditText.setErrorEnabled(false);
                            if (!bloodQuantity.isEmpty()){
                                bloodQuantityEditText.setError(null);
                                bloodQuantityEditText.setErrorEnabled(false);
                                if (!date.isEmpty()){
                                    dateEditText.setError(null);
                                    dateEditText.setErrorEnabled(false);
                                    if (!time.isEmpty()){
                                        timeEditText.setError(null);
                                        timeEditText.setErrorEnabled(false);
                                        if (!disease.isEmpty()){
                                            diseaseEditText.setError(null);
                                            diseaseEditText.setErrorEnabled(false);
                                            if (!hospital_Name.isEmpty()){
                                                hospitalNameEditText.setError(null);
                                                hospitalNameEditText.setErrorEnabled(false);
                                                if (!hospital_Location.isEmpty()){
                                                    hospitalLocationEditText.setError(null);
                                                    hospitalLocationEditText.setErrorEnabled(false);
                                                    if (!mobileNumber.isEmpty() && isValidBangladeshiPhoneNumber(mobileNumber)){
                                                        mobileEditText.setError(null);
                                                        mobileEditText.setErrorEnabled(false);

                                                            //stored database function
                                                            requestDatabase = FirebaseDatabase.getInstance().getReference("BloodRequest");
                                                            requestDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                        Blood_Request_Handler newRequest = new Blood_Request_Handler(fullName, district, bloodGroup, bloodQuantity, date, time, disease, hospital_Name, hospital_Location, mobileNumber, accountNumber,requestId);
                                                                        requestDatabase.child(requestId).setValue(newRequest);

                                                                        Toast.makeText(getApplicationContext(), "Request Sent Successfully", Toast.LENGTH_SHORT).show();

                                                                        // Save the data into SharedPreferences
                                                                        SharedPreferences bloodPreferences = getSharedPreferences(bloodRequest, MODE_PRIVATE);
                                                                        SharedPreferences.Editor editor = bloodPreferences.edit();

                                                                        editor.putString("p_name", fullName);
                                                                        editor.putString("p_district", district);
                                                                        editor.putString("p_bloodGroup", bloodGroup);
                                                                        editor.putString("p_bloodQuantity", bloodQuantity);
                                                                        editor.putString("p_date", date);
                                                                        editor.putString("p_time", time);
                                                                        editor.putString("p_disease", disease);
                                                                        editor.putString("p_hospital_Name", hospital_Name);
                                                                        editor.putString("p_hospital_Location", hospital_Location);
                                                                        editor.putString("p_mobileNumber", mobileNumber);
                                                                        editor.putString("p_accountNumber", accountNumber);
                                                                        editor.putString("p_requestId", requestId);
                                                                        editor.apply();

                                                                        // Optionally, move to another activity (e.g., Blood_Request_Post_Edit)
                                                                        Intent intent = new Intent(Blood_Request_Send.this, Blood_Request_List.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                    }else {
                                                        mobileEditText.setError("Please enter your valid mobile number");
                                                    }
                                                }else {
                                                    hospitalLocationEditText.setError("Please enter hospital address");
                                                }
                                            }else {
                                                hospitalNameEditText.setError("Please enter hospital name");
                                            }
                                        }else {
                                            diseaseEditText.setError("Please enter your disease");
                                        }
                                    }else {
                                        timeEditText.setError("Please select time");
                                    }
                                }else {
                                    dateEditText.setError("Please select date");
                                }
                            }else {
                                bloodGroupEditText.setError("Please select your blood group");
                            }
                        }else {
                            bloodQuantityEditText.setError("Please select blood quantity");
                        }
                    }else {
                        districtEditText.setError("Please select your district");
                    }
                }else {
                    nameEditText.setError("Please enter your full name");
                }
            }
        });
    }

    private boolean isValidBangladeshiPhoneNumber(String phoneNumber) {
        // Check if the phone number is 11 digits long and starts with "01"
        return phoneNumber.matches("^01[3-9]\\d{8}$");
    }

    //Date picker function
    private void showDatePickerDialog() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();

        // Create Date Picker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Blood_Request_Send.this,
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
                Blood_Request_Send.this,
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