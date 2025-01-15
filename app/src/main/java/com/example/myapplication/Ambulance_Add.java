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

public class Ambulance_Add extends AppCompatActivity {
    String[] districts;
    private Button ambulance_registration_button;
    private ImageButton back_button;
    private ImageButton home;
    private TextInputLayout nameEditText, hospitalNameEditText, districtEditText, upazilaEditText, presentAddressEditText, mobileEditText;
    DatabaseReference ambulanceDatabase;
    private static final String ambulanceRequest = "ambulanceRequest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_add);

        //find all input field id
        nameEditText = findViewById(R.id.name_input);
        hospitalNameEditText = findViewById(R.id.hospitalName_input);
        districtEditText = findViewById(R.id.district_input);
        upazilaEditText = findViewById(R.id.upazila_input);
        presentAddressEditText = findViewById(R.id.presentAddress_input);
        mobileEditText = findViewById(R.id.mobileNumber_input);

        //button find
        ambulance_registration_button = findViewById(R.id.registration_Save);
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
        ambulance_registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = nameEditText.getEditText().getText().toString().trim();
                String hospital_name = hospitalNameEditText.getEditText().getText().toString().trim();
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
                                        ambulanceDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(!snapshot.hasChild(mobileNumber)){
                                                    mobileEditText.setError(null);
                                                    mobileEditText.setErrorEnabled(false);
                                                    Ambulance_Add_Handler newDriver = new Ambulance_Add_Handler(fullName, hospital_name, district, upazila, present_address, mobileNumber, accountNumber);
                                                    ambulanceDatabase.child(mobileNumber).setValue(newDriver);

                                                    SharedPreferences volunteerPreferences = getSharedPreferences(ambulanceRequest, MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = volunteerPreferences.edit();

                                                    editor.putString("a_name",fullName);
                                                    editor.putString("a_hospitalName",hospital_name);
                                                    editor.putString("a_district",district);
                                                    editor.putString("a_upazila",upazila);
                                                    editor.putString("a_presentAddress",present_address);
                                                    editor.putString("a_mobileNumber",mobileNumber);
                                                    editor.putString("a_accountNumber",accountNumber);
                                                    editor.apply();

                                                    Toast.makeText(getApplicationContext(), "Registration Successfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Ambulance_Add.this, Ambulance_List.class);
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
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ambulance_Add.this, Ambulance_List.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ambulance_Add.this, MainActivity.class);
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

