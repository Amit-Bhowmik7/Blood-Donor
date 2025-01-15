package com.example.myapplication;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class Donation_Person_Entry_Form extends AppCompatActivity {
    String[] districts;
    private Button contributorSave_button;
    private ImageButton back_button;
    private ImageButton home;
    private TextInputLayout contributorNameEditText, contributorDistrictEditText, contributorMobileEditText, contributorAmountEditText, contributorTnxEditText;
    DatabaseReference contributorDatabase;
    private static final String contributorPreferences ="contributorPreferences";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_person_entry_form);

        //find all input field id
        contributorNameEditText = findViewById(R.id.senderName_input);
        contributorDistrictEditText = findViewById(R.id.senderDistrict_input);
        contributorMobileEditText = findViewById(R.id.senderMobileNumber_input);
        contributorAmountEditText = findViewById(R.id.senderAmount_input);
        contributorTnxEditText = findViewById(R.id.senderTnx_input);

        //button find
        contributorSave_button = findViewById(R.id.contributorRegistration_Save);
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
        contributorSave_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contributorName = contributorNameEditText.getEditText().getText().toString().trim();
                String contributorDistrict = contributorDistrictEditText.getEditText().getText().toString().trim();
                String contributorMobileNumber = contributorMobileEditText.getEditText().getText().toString().trim();
                String contributorAmount = contributorAmountEditText.getEditText().getText().toString().trim();
                String contributorTnxId = contributorTnxEditText.getEditText().getText().toString().trim();
                String contributorId = UUID.randomUUID().toString();

                //validation check
                if (!contributorName.isEmpty()){
                    contributorNameEditText.setError(null);
                    contributorNameEditText.setErrorEnabled(false);
                    if (!contributorDistrict.isEmpty()){
                        contributorDistrictEditText.setError(null);
                        contributorDistrictEditText.setErrorEnabled(false);
                                if (!contributorMobileNumber.isEmpty() && isValidBangladeshiPhoneNumber(contributorMobileNumber)){
                                    contributorMobileEditText.setError(null);
                                    contributorMobileEditText.setErrorEnabled(false);
                                    if (!contributorAmount.isEmpty()){
                                        contributorAmountEditText.setError(null);
                                        contributorAmountEditText.setErrorEnabled(false);
                                        if (!contributorTnxId.isEmpty()){
                                            contributorTnxEditText.setError(null);
                                            contributorTnxEditText.setErrorEnabled(false);

                                            contributorDatabase = FirebaseDatabase.getInstance().getReference("Contributor_Entry_Form_List");
                                            Donation_Person_Entry_Form_Handler newContributor = new Donation_Person_Entry_Form_Handler(contributorName, contributorDistrict, contributorMobileNumber, contributorAmount, contributorTnxId, contributorId);
                                            contributorDatabase.child(contributorId).setValue(newContributor);

                                            SharedPreferences contributor = getSharedPreferences(contributorPreferences, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = contributor.edit();

                                            editor.putString("contributor_Name",contributorName);
                                            editor.putString("contributor_district",contributorDistrict);
                                            editor.putString("contributor_Id",contributorId);
                                            editor.apply();

                                            Toast.makeText(getApplicationContext(), "Registration Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Donation_Person_Entry_Form.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            contributorAmountEditText.setError("Please enter transection id");
                                        }
                                    }else {
                                        contributorAmountEditText.setError("Please enter Transactional Amount");
                                    }
                                }else {
                                    contributorMobileEditText.setError("Please enter your valid mobile number");
                                }
                    }else {
                        contributorDistrictEditText.setError("Please select your district");
                    }
                }else {
                    contributorNameEditText.setError("Please enter your organization name");
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Donation_Person_List.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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