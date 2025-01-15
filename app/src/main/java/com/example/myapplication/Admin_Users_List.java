package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

public class Admin_Users_List extends AppCompatActivity {
    String[] districts;
    private Spinner districtsSpinner;
    private RecyclerView recyclerView;
    private Admin_Users_List_Adapter adapter;
    private List<New_User_Registration_Handler> requestList;
    private ImageButton backButton,homeButton;
    private DatabaseReference registration_database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users_list);

        // Initialize buttons
        homeButton = findViewById(R.id.home);
        backButton = findViewById(R.id.back_button);
        districtsSpinner = findViewById(R.id.districtDropdown);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.searchList_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize request list and adapter
        requestList = new ArrayList<>();
        adapter = new Admin_Users_List_Adapter(requestList);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Database Reference
        registration_database = FirebaseDatabase.getInstance().getReference("UsersList");


        // Intent for back page (mainActivity)
        backButton.setOnClickListener(v -> backButton());
        homeButton.setOnClickListener(v -> home());

        setupDistrictSpinner();
        // Fetch all data initially
        fetchRequests();
    }

    private void home() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void backButton() {
        Intent intent = new Intent(getApplicationContext(), Admin_Menu.class);
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
        registration_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    New_User_Registration_Handler handler = dataSnapshot.getValue(New_User_Registration_Handler.class);
                    if (handler != null) {
                        requestList.add(0,handler);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Admin_Users_List.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRequestsByDistrict(String district) {
        registration_database.orderByChild("district").equalTo(district).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    New_User_Registration_Handler handler = dataSnapshot.getValue(New_User_Registration_Handler.class);
                    if (handler != null) {
                        requestList.add(0,handler);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Admin_Users_List.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}