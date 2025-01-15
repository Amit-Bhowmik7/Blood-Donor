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

public class Organization_Add extends AppCompatActivity {
    String[] districts;
    private Button oorganization_add_button;
    private ImageButton back_button;
    private ImageButton home;
    private TextInputLayout organizationNameEditText, districtEditText, upazilaEditText, officeAddressEditText, mobileEditText;
    DatabaseReference organizationDatabase;
    private static final String organizationRequest = "organizationRequest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_add);

        //find all input field id
        organizationNameEditText = findViewById(R.id.organizationName_input);
        districtEditText = findViewById(R.id.district_input);
        upazilaEditText = findViewById(R.id.upazila_input);
        officeAddressEditText = findViewById(R.id.officeAddress_input);
        mobileEditText = findViewById(R.id.mobileNumber_input);

        //button find
        oorganization_add_button = findViewById(R.id.registration_Save);
        home = findViewById(R.id.home);
        back_button = findViewById(R.id.back_button);

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
        oorganization_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String organizationName = organizationNameEditText.getEditText().getText().toString().trim();
                String district = districtEditText.getEditText().getText().toString().trim();
                String upazila = upazilaEditText.getEditText().getText().toString().trim();
                String officeAddress = officeAddressEditText.getEditText().getText().toString().trim();
                String mobileNumber = mobileEditText.getEditText().getText().toString().trim();

                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String accountNumber = sharedPreferences.getString("mobileNumber","N/A");

                //validation check
                if (!organizationName.isEmpty()){
                    organizationNameEditText.setError(null);
                    organizationNameEditText.setErrorEnabled(false);
                    if (!district.isEmpty()){
                        districtEditText.setError(null);
                        districtEditText.setErrorEnabled(false);
                        if (!upazila.isEmpty()){
                            upazilaEditText.setError(null);
                            upazilaEditText.setErrorEnabled(false);
                            if (!officeAddress.isEmpty()){
                                officeAddressEditText.setError(null);
                                officeAddressEditText.setErrorEnabled(false);
                                if (!mobileNumber.isEmpty() && isValidBangladeshiPhoneNumber(mobileNumber)){
                                    mobileEditText.setError(null);
                                    mobileEditText.setErrorEnabled(false);

                                    organizationDatabase = FirebaseDatabase.getInstance().getReference("OrganizationList");
                                    organizationDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(!snapshot.hasChild(mobileNumber)){
                                                mobileEditText.setError(null);
                                                mobileEditText.setErrorEnabled(false);
                                                Organization_Add_Handler newOrganization = new Organization_Add_Handler(organizationName, district, upazila, officeAddress, mobileNumber, accountNumber);
                                                organizationDatabase.child(mobileNumber).setValue(newOrganization);

                                                SharedPreferences volunteerPreferences = getSharedPreferences(organizationRequest, MODE_PRIVATE);
                                                SharedPreferences.Editor editor = volunteerPreferences.edit();

                                                editor.putString("o_name",organizationName);
                                                editor.putString("o_district",district);
                                                editor.putString("o_upazila",upazila);
                                                editor.putString("o_officeAddress",officeAddress);
                                                editor.putString("o_mobileNumber",mobileNumber);
                                                editor.putString("o_accountNumber",accountNumber);
                                                editor.apply();

                                                Toast.makeText(getApplicationContext(), "Registration Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Organization_Add.this, Organization_List.class);
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
                                officeAddressEditText.setError("Please enter your office address");
                            }
                        }else {
                            upazilaEditText.setError("Please enter your upazila/thana");
                        }
                    }else {
                        districtEditText.setError("Please select your district");
                    }
                }else {
                    organizationNameEditText.setError("Please enter your organization name");
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Organization_Add.this, Organization_List.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Organization_Add.this, MainActivity.class);
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