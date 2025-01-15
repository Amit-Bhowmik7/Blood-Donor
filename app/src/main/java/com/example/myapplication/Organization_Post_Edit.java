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

public class Organization_Post_Edit extends AppCompatActivity {
    String[] districts;
    private ImageButton back_button;
    private ImageButton homeButton;
    private TextInputLayout nameEditText, districtEditText, upazilaEditText, officeAddressEditText, mobileEditText;
    private AppCompatButton updateButton, deleteButton;
    private AutoCompleteTextView districtDropdown;
    DatabaseReference organizationDatabase;
    private static final String organizationRequest = "organizationRequest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_post_edit);

        //find all input field id
        nameEditText = findViewById(R.id.organizationName_input);
        districtEditText = findViewById(R.id.district_input);
        upazilaEditText = findViewById(R.id.upazila_input);
        officeAddressEditText = findViewById(R.id.officeAddress_input);
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

    private void deletePost() {
        Intent intent = new Intent(getApplicationContext(), Organization_Post_Delete_Dialog.class);
        startActivity(intent);
    }

    private void updatePost() {

        String fullNameInput = nameEditText.getEditText().getText().toString().trim();
        String districtInput = districtEditText.getEditText().getText().toString().trim();
        String upazilaInput = upazilaEditText.getEditText().getText().toString().trim();
        String present_addressInput = officeAddressEditText.getEditText().getText().toString().trim();
        String mobileNumberInput = mobileEditText.getEditText().getText().toString().trim();

        SharedPreferences sharedPreferences = getSharedPreferences(organizationRequest, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String accountNumber = sharedPreferences.getString("o_accountNumber","N/A");
        String originalmobileNumber = sharedPreferences.getString("o_mobileNumber","N/A");

        //validation check
        if (!fullNameInput.isEmpty()){
            nameEditText.setError(null);
            nameEditText.setErrorEnabled(false);
            if (!districtInput.isEmpty()){
                districtEditText.setError(null);
                districtEditText.setErrorEnabled(false);
                if (!upazilaInput.isEmpty()){
                    upazilaEditText.setError(null);
                    upazilaEditText.setErrorEnabled(false);
                    if (!present_addressInput.isEmpty()){
                        officeAddressEditText.setError(null);
                        officeAddressEditText.setErrorEnabled(false);
                        if (!mobileNumberInput.isEmpty() && isValidBangladeshiPhoneNumber(mobileNumberInput)){
                            mobileEditText.setError(null);
                            mobileEditText.setErrorEnabled(false);

                            organizationDatabase = FirebaseDatabase.getInstance().getReference("OrganizationList");

                            if (!originalmobileNumber.equals(mobileNumberInput)) {
                                organizationDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild(mobileNumberInput)) {
                                            mobileEditText.setError("This phone number is already registered");
                                        } else {
                                            organizationDatabase.child(originalmobileNumber).get()
                                                    .addOnSuccessListener(dataSnapshot -> {
                                                        organizationDatabase.child(mobileNumberInput).setValue(dataSnapshot.getValue());
                                                        organizationDatabase.child(mobileNumberInput).child("mobileNumber").setValue(mobileNumberInput);
                                                        organizationDatabase.child(originalmobileNumber).removeValue();
                                                        updatePreferences(editor, fullNameInput, districtInput, upazilaInput, present_addressInput, accountNumber, mobileNumberInput);
                                                        Toast.makeText(Organization_Post_Edit.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(Organization_Post_Edit.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                organizationDatabase.child(mobileNumberInput).child("organizationName").setValue(fullNameInput);
                                organizationDatabase.child(mobileNumberInput).child("district").setValue(districtInput);
                                organizationDatabase.child(mobileNumberInput).child("upazila").setValue(upazilaInput);
                                organizationDatabase.child(mobileNumberInput).child("officeAddress").setValue(present_addressInput);
                                organizationDatabase.child(mobileNumberInput).child("accountNumber").setValue(accountNumber);

                                updatePreferences(editor, fullNameInput, districtInput, upazilaInput, present_addressInput, accountNumber, mobileNumberInput);
                                Toast.makeText(Organization_Post_Edit.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            mobileEditText.setError("Please enter your valid mobile number");
                        }
                    }else {
                        officeAddressEditText.setError("Please enter your office address");
                    }
                }else {
                    upazilaEditText.setError("Please enter your upazila / thana / Area");
                }
            }else {
                districtEditText.setError("Please select your district");
            }
        }else {
            nameEditText.setError("Please enter your full name");
        }
    }
    private void updatePreferences(SharedPreferences.Editor editor, String organizationname, String district, String upazila, String officeAddress,  String accountNumber, String mobileNumber) {
        editor.putString("o_name", organizationname);
        editor.putString("o_district", district);
        editor.putString("o_upazila", upazila);
        editor.putString("o_officeAddress", officeAddress);
        editor.putString("o_mobileNumber", mobileNumber);
        editor.putString("o_accountNumber", accountNumber);
        editor.apply();
    }

    //bangladeshi mobile pattern
    private boolean isValidBangladeshiPhoneNumber(String phoneNumber) {
        // Check if the phone number is 11 digits long and starts with "01"
        return phoneNumber.matches("^01[3-9]\\d{8}$");
    }
    private void homeButton() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void backButton() {
        Intent intent = new Intent(getApplicationContext(), Organization_List.class);
        startActivity(intent);
    }

    private void displayRequestPost() {
        SharedPreferences sharedPreferences = getSharedPreferences(organizationRequest, MODE_PRIVATE);

        String organizationName = sharedPreferences.getString("o_name", "N/A");
        String district = sharedPreferences.getString("o_district", "N/A");
        String upazila = sharedPreferences.getString("o_upazila", "N/A");
        String officeAddress = sharedPreferences.getString("o_officeAddress", "N/A");
        String mobileNumber = sharedPreferences.getString("o_mobileNumber", "N/A");

        nameEditText.getEditText().setText(organizationName);
        districtDropdown.setText(district, false);
        upazilaEditText.getEditText().setText(upazila);
        officeAddressEditText.getEditText().setText(officeAddress);
        mobileEditText.getEditText().setText(mobileNumber);
    }
}