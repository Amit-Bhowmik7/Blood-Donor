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

public class Thalassemia_Patient_List extends AppCompatActivity {
    String[] districts;
    private Spinner districtsSpinner;
    private RecyclerView recyclerView;
    private Thalassemia_patient_List_Adapter adapter;
    private List<Thalassemia_Patient_Handler> requestList;
    private DatabaseReference requestDatabase;
    private AppCompatButton thalassemia_patient_add_button;
    private ImageButton backButton,homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thalassemia_patient_list);

        // Initialize buttons
        backButton = findViewById(R.id.back_button);
        thalassemia_patient_add_button = findViewById(R.id.thalassemia_patient_add_button);
        districtsSpinner = findViewById(R.id.districtDropdown);
        homeButton =findViewById(R.id.home);

        homeButton.setVisibility(View.GONE);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.thalassemia_patient_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize request list and adapter
        requestList = new ArrayList<>();
        adapter = new Thalassemia_patient_List_Adapter(requestList);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Database Reference
        requestDatabase = FirebaseDatabase.getInstance().getReference("ThalassemiaPatientList");

        // Intent for next page (ambulance add)
        thalassemia_patient_add_button.setOnClickListener(v -> thalassemiaPatientAdd());

        // Intent for back page (mainActivity)
        backButton.setOnClickListener(v -> backButton());

        setupDistrictSpinner();
        // Fetch all data initially
        fetchRequests();
    }

    private void backButton() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void thalassemiaPatientAdd() {
        Intent intent = new Intent(getApplicationContext(), Thalassemia_Patient_Add.class);
        startActivity(intent);
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
                requestList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Thalassemia_Patient_Handler handler = dataSnapshot.getValue(Thalassemia_Patient_Handler.class);
                    if (handler != null) {
                        requestList.add(handler);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Thalassemia_Patient_List.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRequestsByDistrict(String district) {
        requestDatabase.orderByChild("district").equalTo(district).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Thalassemia_Patient_Handler handler = dataSnapshot.getValue(Thalassemia_Patient_Handler.class);
                    if (handler != null) {
                        requestList.add(handler);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Thalassemia_Patient_List.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}