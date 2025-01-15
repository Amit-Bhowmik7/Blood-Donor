package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class Thalassemia_Patient_Edit extends AppCompatActivity {
    String[] districts, bloodGroups;
    private AppCompatButton updateButton, deleteButton;
    private ImageButton backButton, homeButton;
    private AutoCompleteTextView districtDropdown, bloodGroupDropdown;
    private TextInputLayout nameEditText, districtEditText, bloodGroupEditText, upazilaEditText, presentAddressEditText, mobileEditText;
    DatabaseReference thalassemiaDatabase;
    private static final String thalassemiaRequest = "thalassemiaRequest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thalassemia_patient_edit);

        //find all input field id
        nameEditText = findViewById(R.id.thalassemia_patient_name_input);
        bloodGroupEditText = findViewById(R.id.thalassemia_patient_bloodGroup_input);
        districtEditText = findViewById(R.id.thalassemia_patient_district_input);
        upazilaEditText = findViewById(R.id.thalassemia_patient_upazila_input);
        presentAddressEditText = findViewById(R.id.thalassemia_patient_presentAddress_input);
        mobileEditText = findViewById(R.id.thalassemia_patient_mobileNumber_input);

        //button find
        homeButton = findViewById(R.id.home);
        backButton = findViewById(R.id.back_button);
        updateButton = findViewById(R.id.update_post);
        deleteButton = findViewById(R.id.delete_post);

        // Initialize AutoCompleteTextViews for districts, blood groups, and blood quantities
        districtDropdown = findViewById(R.id.district_dropdown);
        bloodGroupDropdown = findViewById(R.id.bloodGroup_dropdown);

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

        backButton.setOnClickListener(v -> backButton());
        homeButton.setOnClickListener(v -> homeButton());

        displayRequestPost();

        updateButton.setOnClickListener(v -> updatePost());
        deleteButton.setOnClickListener(v -> deletePost());
    }

    private void updatePost() {

        String fullName = nameEditText.getEditText().getText().toString().trim();
        String bloodGroup = bloodGroupEditText.getEditText().getText().toString().trim();
        String district = districtEditText.getEditText().getText().toString().trim();
        String upazila = upazilaEditText.getEditText().getText().toString().trim();
        String present_address = presentAddressEditText.getEditText().getText().toString().trim();
        String mobileNumber = mobileEditText.getEditText().getText().toString().trim();

        SharedPreferences sharedPreferences = getSharedPreferences(thalassemiaRequest, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String accountNumber = sharedPreferences.getString("t_accountNumber","N/A");
        String originalmobileNumber = sharedPreferences.getString("t_mobileNumber","N/A");

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

                                if (!originalmobileNumber.equals(mobileNumber)) {
                                    thalassemiaDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.hasChild(mobileNumber)) {
                                                mobileEditText.setError("This phone number is already registered");
                                            } else {
                                                thalassemiaDatabase.child(originalmobileNumber).get()
                                                        .addOnSuccessListener(dataSnapshot -> {
                                                            thalassemiaDatabase.child(mobileNumber).setValue(dataSnapshot.getValue());
                                                            thalassemiaDatabase.child(mobileNumber).child("mobileNumber").setValue(mobileNumber);
                                                            thalassemiaDatabase.child(originalmobileNumber).removeValue();

                                                            updatePreferences(editor, fullName, bloodGroup, district, upazila, present_address, accountNumber, mobileNumber);
                                                            Toast.makeText(Thalassemia_Patient_Edit.this, "Post updated successfully!", Toast.LENGTH_SHORT).show();
                                                        });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(Thalassemia_Patient_Edit.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    thalassemiaDatabase.child(mobileNumber).child("patientName").setValue(fullName);
                                    thalassemiaDatabase.child(mobileNumber).child("bloodGroup").setValue(bloodGroup);
                                    thalassemiaDatabase.child(mobileNumber).child("district").setValue(district);
                                    thalassemiaDatabase.child(mobileNumber).child("upazila").setValue(upazila);
                                    thalassemiaDatabase.child(mobileNumber).child("presentAddress").setValue(present_address);
                                    thalassemiaDatabase.child(mobileNumber).child("accountNumber").setValue(accountNumber);

                                    updatePreferences(editor, fullName, bloodGroup, district, upazila, present_address, accountNumber, mobileNumber);
                                    Toast.makeText(Thalassemia_Patient_Edit.this, "Post updated successfully!", Toast.LENGTH_SHORT).show();
                                }

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

    private void updatePreferences(SharedPreferences.Editor editor, String fullname, String bloodGroup, String district, String upazila, String presentAddress,  String accountNumber, String mobileNumber) {
        editor.putString("t_name", fullname);
        editor.putString("t_bloodGroup", bloodGroup);
        editor.putString("t_district", district);
        editor.putString("t_upazila", upazila);
        editor.putString("t_presentAddress", presentAddress);
        editor.putString("t_mobileNumber", mobileNumber);
        editor.putString("t_accountNumber", accountNumber);
        editor.apply();
    }

    //bangladeshi mobile pattern
    private boolean isValidBangladeshiPhoneNumber(String phoneNumber) {
        // Check if the phone number is 11 digits long and starts with "01"
        return phoneNumber.matches("^01[3-9]\\d{8}$");
    }

    private void deletePost() {
        Intent intent = new Intent(getApplicationContext(), Thalassemia_patient_Delete_Dialog.class);
        startActivity(intent);
    }

    private void homeButton() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    private void backButton() {
        Intent intent = new Intent(getApplicationContext(), Thalassemia_Patient_List.class);
        startActivity(intent);
    }

    private void displayRequestPost() {
        SharedPreferences sharedPreferences = getSharedPreferences(thalassemiaRequest, MODE_PRIVATE);

        String fullName = sharedPreferences.getString("t_name", "N/A");
        String bloodGroup = sharedPreferences.getString("t_bloodGroup", "N/A");
        String district = sharedPreferences.getString("t_district", "N/A");
        String upazila = sharedPreferences.getString("t_upazila", "N/A");
        String presentAddress = sharedPreferences.getString("t_presentAddress", "N/A");
        String mobileNumber = sharedPreferences.getString("t_mobileNumber", "N/A");

        nameEditText.getEditText().setText(fullName);
        bloodGroupEditText.getEditText().setText(bloodGroup);
        districtDropdown.setText(district,false);
        bloodGroupDropdown.setText(district,false);
        upazilaEditText.getEditText().setText(upazila);
        presentAddressEditText.getEditText().setText(presentAddress);
        mobileEditText.getEditText().setText(mobileNumber);
    }
}