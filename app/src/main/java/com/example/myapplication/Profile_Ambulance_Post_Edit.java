package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile_Ambulance_Post_Edit extends AppCompatActivity {
    String[] districts;
    private AppCompatButton updateButton, deleteButton;
    private AutoCompleteTextView districtDropdown;
    private ImageButton back_button, homeButton;
    private TextInputLayout nameEditText, hospitalNameEditText, districtEditText, upazilaEditText, presentAddressEditText, mobileEditText;
    DatabaseReference ambulanceDatabase;
    private static final String ambulanceRequest = "ambulanceRequest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_ambulance_post_edit);

        //find all input field id
        nameEditText = findViewById(R.id.name_input);
        hospitalNameEditText = findViewById(R.id.hospitalName_input);
        districtEditText = findViewById(R.id.district_input);
        upazilaEditText = findViewById(R.id.upazila_input);
        presentAddressEditText = findViewById(R.id.presentAddress_input);
        mobileEditText = findViewById(R.id.mobileNumber_input);

        //button find
        homeButton = findViewById(R.id.home);
        back_button = findViewById(R.id.back_button);
        updateButton = findViewById(R.id.update_post);
        deleteButton = findViewById(R.id.delete_post);

        // Initialize AutoCompleteTextViews for districts
        districtDropdown = findViewById(R.id.district_dropdown);

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

        back_button.setOnClickListener(v -> backButton());
        homeButton.setOnClickListener(v -> homeButton());

        displayRequestPost();

        updateButton.setOnClickListener(v -> updatePost());
        deleteButton.setOnClickListener(v -> deletePost());
    }

    private void updatePost() {

        String fullName = nameEditText.getEditText().getText().toString().trim();
        String hospital_name = hospitalNameEditText.getEditText().getText().toString().trim();
        String district = districtEditText.getEditText().getText().toString().trim();
        String upazila = upazilaEditText.getEditText().getText().toString().trim();
        String present_address = presentAddressEditText.getEditText().getText().toString().trim();
        String mobileNumber = mobileEditText.getEditText().getText().toString().trim();

        SharedPreferences sharedPreferences = getSharedPreferences(ambulanceRequest, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String accountNumber = sharedPreferences.getString("a_accountNumber","N/A");
        String originalmobileNumber = sharedPreferences.getString("a_mobileNumber","N/A");

        //validation check
        if (!fullName.isEmpty()){
            nameEditText.setError(null);
            nameEditText.setErrorEnabled(false);
            if(!hospital_name.isEmpty()){
                hospitalNameEditText.setError(null);
                hospitalNameEditText.setErrorEnabled(false);
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

                                ambulanceDatabase = FirebaseDatabase.getInstance().getReference("AmbulanceList");

                                if (!originalmobileNumber.equals(mobileNumber)) {
                                    ambulanceDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.hasChild(mobileNumber)) {
                                                mobileEditText.setError("This phone number is already registered");
                                            } else {
                                                ambulanceDatabase.child(originalmobileNumber).get()
                                                        .addOnSuccessListener(dataSnapshot -> {
                                                            ambulanceDatabase.child(mobileNumber).setValue(dataSnapshot.getValue());
                                                            ambulanceDatabase.child(mobileNumber).child("mobileNumber").setValue(mobileNumber);
                                                            ambulanceDatabase.child(originalmobileNumber).removeValue();

                                                            updatePreferences(editor, fullName, hospital_name, district, upazila, present_address, accountNumber, mobileNumber);
                                                            Toast.makeText(Profile_Ambulance_Post_Edit.this, "Post updated successfully!", Toast.LENGTH_SHORT).show();
                                                        });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(Profile_Ambulance_Post_Edit.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    ambulanceDatabase.child(mobileNumber).child("name").setValue(fullName);
                                    ambulanceDatabase.child(mobileNumber).child("hospitalName").setValue(hospital_name);
                                    ambulanceDatabase.child(mobileNumber).child("district").setValue(district);
                                    ambulanceDatabase.child(mobileNumber).child("upazila").setValue(upazila);
                                    ambulanceDatabase.child(mobileNumber).child("presentAddress").setValue(present_address);
                                    ambulanceDatabase.child(mobileNumber).child("accountNumber").setValue(accountNumber);

                                    updatePreferences(editor, fullName, hospital_name, district, upazila, present_address, accountNumber, mobileNumber);
                                    Toast.makeText(Profile_Ambulance_Post_Edit.this, "Post updated successfully!", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                mobileEditText.setError("Please enter your valid mobile number");
                            }
                        }else {
                            presentAddressEditText.setError("Please enter your present address");
                        }
                    }else {
                        upazilaEditText.setError("Please enter your upazila/thana");
                    }
                }else {
                    districtEditText.setError("Please select your district");
                }
            }else {
                hospitalNameEditText.setError("Please enter hospital name");
            }
        }else {
            nameEditText.setError("Please enter your full name");
        }
    }

    private void updatePreferences(SharedPreferences.Editor editor, String fullname, String hospital_name, String district, String upazila, String presentAddress,  String accountNumber, String mobileNumber) {
        editor.putString("a_name", fullname);
        editor.putString("a_hospitalName", hospital_name);
        editor.putString("a_district", district);
        editor.putString("a_upazila", upazila);
        editor.putString("a_presentAddress", presentAddress);
        editor.putString("a_mobileNumber", mobileNumber);
        editor.putString("a_accountNumber", accountNumber);
        editor.apply();
    }

    //bangladeshi mobile pattern
    private boolean isValidBangladeshiPhoneNumber(String phoneNumber) {
        // Check if the phone number is 11 digits long and starts with "01"
        return phoneNumber.matches("^01[3-9]\\d{8}$");
    }

    private void deletePost() {
        Intent intent = new Intent(getApplicationContext(), Profile_Ambulance_Post_Delete_Dialog.class);
        startActivity(intent);
    }

    private void homeButton() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    private void backButton() {
        Intent intent = new Intent(getApplicationContext(), Profile_Ambulance_List.class);
        startActivity(intent);
    }

    private void displayRequestPost() {
        SharedPreferences sharedPreferences = getSharedPreferences(ambulanceRequest, MODE_PRIVATE);

        String fullName = sharedPreferences.getString("a_name", "N/A");
        String hospitalName = sharedPreferences.getString("a_hospitalName", "N/A");
        String district = sharedPreferences.getString("a_district", "N/A");
        String upazila = sharedPreferences.getString("a_upazila", "N/A");
        String presentAddress = sharedPreferences.getString("a_presentAddress", "N/A");
        String mobileNumber = sharedPreferences.getString("a_mobileNumber", "N/A");

        nameEditText.getEditText().setText(fullName);
        hospitalNameEditText.getEditText().setText(hospitalName);
        districtDropdown.setText(district,false);
        upazilaEditText.getEditText().setText(upazila);
        presentAddressEditText.getEditText().setText(presentAddress);
        mobileEditText.getEditText().setText(mobileNumber);
    }
}