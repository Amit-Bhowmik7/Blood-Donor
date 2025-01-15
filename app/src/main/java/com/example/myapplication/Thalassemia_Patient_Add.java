package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Thalassemia_Patient_Add extends AppCompatActivity {
    String[] districts, bloodGroups;
    private Button thalassemia_patient_registration_save_button;
    private ImageButton back_button;
    private ImageButton home;
    private TextInputLayout nameEditText, districtEditText, bloodGroupEditText, upazilaEditText, presentAddressEditText, mobileEditText;
    DatabaseReference thalassemiaDatabase;
    private static final String thalassemiaRequest = "thalassemiaRequest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thalassemia_patient_add);

        //find all input field id
        nameEditText = findViewById(R.id.thalassemia_patient_name_input);
        bloodGroupEditText = findViewById(R.id.thalassemia_patient_bloodGroup_input);
        districtEditText = findViewById(R.id.thalassemia_patient_district_input);
        upazilaEditText = findViewById(R.id.thalassemia_patient_upazila_input);
        presentAddressEditText = findViewById(R.id.thalassemia_patient_presentAddress_input);
        mobileEditText = findViewById(R.id.thalassemia_patient_mobileNumber_input);

        //button find
        thalassemia_patient_registration_save_button = findViewById(R.id.thalassemia_patient_registration_Save);
        home = findViewById(R.id.home);
        back_button = findViewById(R.id.back_button);


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

        //data stored and get from database
        thalassemia_patient_registration_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = nameEditText.getEditText().getText().toString().trim();
                String bloodGroup = bloodGroupEditText.getEditText().getText().toString().trim();
                String district = districtEditText.getEditText().getText().toString().trim();
                String upazila = upazilaEditText.getEditText().getText().toString().trim();
                String present_address = presentAddressEditText.getEditText().getText().toString().trim();
                String mobileNumber = mobileEditText.getEditText().getText().toString().trim();

                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String accountNumber = sharedPreferences.getString("mobileNumber","N/A");

                //validation check
                if (!fullName.isEmpty()){
                    nameEditText.setError(null);
                    nameEditText.setErrorEnabled(false);
                    if (!bloodGroup.isEmpty()){
                        bloodGroupEditText.setError(null);
                        bloodGroupEditText.setErrorEnabled(false);
                        if (!district.isEmpty()){
                            districtEditText.setError(null);
                            districtEditText.setErrorEnabled(false);
                            if (!upazila.isEmpty()){
                                upazilaEditText.setError(null);
                                upazilaEditText.setErrorEnabled(false);
                                if (!present_address.isEmpty()){
                                    presentAddressEditText.setError(null);
                                    presentAddressEditText.setErrorEnabled(false);
                                    if (!mobileNumber.isEmpty() && isValidBangladeshiPhoneNumber(mobileNumber)){
                                        mobileEditText.setError(null);
                                        mobileEditText.setErrorEnabled(false);

                                        thalassemiaDatabase = FirebaseDatabase.getInstance().getReference("ThalassemiaPatientList");
                                        thalassemiaDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(!snapshot.hasChild(mobileNumber)){
                                                    mobileEditText.setError(null);
                                                    mobileEditText.setErrorEnabled(false);
                                                    Thalassemia_Patient_Handler newDriver = new Thalassemia_Patient_Handler(fullName, bloodGroup, district, upazila, present_address, mobileNumber, accountNumber);
                                                    thalassemiaDatabase.child(mobileNumber).setValue(newDriver);

                                                    SharedPreferences thalassemiaPreferences = getSharedPreferences(thalassemiaRequest, MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = thalassemiaPreferences.edit();

                                                    editor.putString("t_name",fullName);
                                                    editor.putString("t_bloodGroup",bloodGroup);
                                                    editor.putString("t_district",district);
                                                    editor.putString("t_upazila",upazila);
                                                    editor.putString("t_presentAddress",present_address);
                                                    editor.putString("t_mobileNumber",mobileNumber);
                                                    editor.putString("t_accountNumber",accountNumber);
                                                    editor.apply();

                                                    Toast.makeText(getApplicationContext(), "Registration Successfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Thalassemia_Patient_Add.this, Thalassemia_Patient_List.class);
                                                    startActivity(intent);
                                                    finish();

                                                }else {
                                                    mobileEditText.setError("Phone number is already registered");
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }else {
                                        mobileEditText.setError("Please enter your valid mobile number");
                                    }
                                }else {
                                    presentAddressEditText.setError("Please enter your present address");
                                }
                            }else {
                                upazilaEditText.setError("Please enter your upazila / thana / Area");
                            }
                        }else {
                            districtEditText.setError("Please select your district");
                        }
                    }else {
                        bloodGroupEditText.setError("Please select your bloodGroup");
                    }
                }else {
                    nameEditText.setError("Please enter your full name");
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Thalassemia_Patient_Add.this, Thalassemia_Patient_List.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Thalassemia_Patient_Add.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //Bangladeshi number pattern for input number
    private boolean isValidBangladeshiPhoneNumber(String mobileNumber) {
        // Check if the phone number is 11 digits long and starts with "01"
        return mobileNumber.matches("^01[3-9]\\d{8}$");
    }
}