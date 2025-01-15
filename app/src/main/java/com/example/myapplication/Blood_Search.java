package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputLayout;

public class Blood_Search extends AppCompatActivity{
    String[] districts,bloodGroups;
    private TextInputLayout bloodGroupEditText, districtEditText;
    private ImageButton backButton, homeButton;
    private Button search_list, search_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_search);


        // District List
        districts = getResources().getStringArray(R.array.districtnamestring);
        AutoCompleteTextView districtDropdown = findViewById(R.id.district_dropdown);
        // ArrayAdapter
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, districts);
        // Set Adapter
        districtDropdown.setAdapter(adapter1);
        districtDropdown.setFocusable(false);
        // Show dropdown on click
        districtDropdown.setOnClickListener(v -> districtDropdown.showDropDown());
        // Handle Selection
        districtDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDistrict = (String) parent.getItemAtPosition(position);
            //Toast.makeText(registration_1st_page.this, "Selected: " + selectedDistrict, Toast.LENGTH_SHORT).show();
        });

        // bloodGroup List
        bloodGroups = getResources().getStringArray(R.array.bloodgroupnamestring);
        AutoCompleteTextView bloodGroupDropdown = findViewById(R.id.bloodGroup_dropdown);
        // ArrayAdapter
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, bloodGroups);
        // Set Adapter
        bloodGroupDropdown.setAdapter(adapter2);
        bloodGroupDropdown.setFocusable(false);
        // Show dropdown on click
        bloodGroupDropdown.setOnClickListener(v -> bloodGroupDropdown.showDropDown());
        // Handle Selection
        districtDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedBloodGroup = (String) parent.getItemAtPosition(position);
            //Toast.makeText(registration_1st_page.this, "Selected: " + selectedBloodGroup, Toast.LENGTH_SHORT).show();
        });

        bloodGroupEditText = findViewById(R.id.bloodGroup_input);
        districtEditText = findViewById(R.id.district_input);
        backButton = findViewById(R.id.back_button);
        search_list = findViewById(R.id.search_list);
        //search_location = findViewById(R.id.search_loction);

        homeButton = findViewById(R.id.home);
        // To hide
        homeButton.setVisibility(View.GONE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        search_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Blood_Search_Result_List.class);
                intent.putExtra("bloodGroup", bloodGroupEditText.getEditText().getText().toString().trim());
                intent.putExtra("district", districtEditText.getEditText().getText().toString().trim());
                startActivity(intent);
            }
        });
    }
}