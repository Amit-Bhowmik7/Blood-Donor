package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Donation_Person_Entry_List extends AppCompatActivity {
    String[] districts;
    private RecyclerView recyclerView;
    private Spinner districtsSpinner;
    private Donation_person_Entry_Form_Adapter adapter;
    private List<Donation_Person_Entry_Form_Handler> requestList;
    private DatabaseReference requestDatabase;
    private ImageButton backButton, searchContributor_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_person_entry_list);

        // Initialize buttons
        backButton = findViewById(R.id.back_button);
        districtsSpinner = findViewById(R.id.districtDropdown);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.contributor_published_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize request list and adapter
        requestList = new ArrayList<>();
        adapter = new Donation_person_Entry_Form_Adapter(requestList);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Database Reference
        requestDatabase = FirebaseDatabase.getInstance().getReference("Contributor_Entry_Form_List");

        // DropDown list for District
        districts = getResources().getStringArray(R.array.districtnamestring);

        // Add "Select District" as the first item dynamically
        List<String> districtsList = new ArrayList<>();
        districtsList.add("Select District"); // Placeholder
        districtsList.addAll(Arrays.asList(districts));


        // Custom Adapter
        ArrayAdapter<String> districtSelectAdapter = new ArrayAdapter<>(this, R.layout.dropdownsampleview, R.id.dropdownview, districtsList);
        districtsSpinner.setAdapter(districtSelectAdapter);

        // Listener to handle selection
        districtsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // No action needed on item selection for now
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle case where no item is selected
            }
        });

        // Intent for back page (mainActivity)
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Admin_Menu.class);
                startActivity(intent);
            }
        });

        setupDistrictSpinner();

        // Fetch all data initially
        fetchRequests();
    }

    private void setupDistrictSpinner() {
        String[] districts = getResources().getStringArray(R.array.districtnamestring);

        List<String> districtList = new ArrayList<>();
        districtList.add("Select District");
        districtList.addAll(Arrays.asList(districts));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdownsampleview, R.id.dropdownview, districtList);
        districtsSpinner.setAdapter(adapter);

        districtsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                if (position > 0) {
                    fetchRequestsByDistrict(districtList.get(position));
                } else {
                    fetchRequests();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    private void fetchRequests() {
        requestDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    Donation_Person_Entry_Form_Handler request = requestSnapshot.getValue(Donation_Person_Entry_Form_Handler.class);
                    requestList.add(0, request);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    private void fetchRequestsByDistrict(String district) {
        requestDatabase.orderByChild("district").equalTo(district).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    Donation_Person_Entry_Form_Handler request = requestSnapshot.getValue(Donation_Person_Entry_Form_Handler.class);
                    requestList.add(0, request);
                }
                adapter.notifyDataSetChanged();

                if (requestList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No contributor found for " + district, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Found " + requestList.size() + " contributor for " + district, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }
}