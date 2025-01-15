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

public class Donation_Person_List extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Donation_Person_Adapter adapter;
    private List<Donation_Person_Entry_Form_Handler> requestList;
    private DatabaseReference databaseReference;
    private Spinner districtSpinner;
    private ImageButton backButton,homeButton;
    private AppCompatButton entry_form_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_person_list);

        recyclerView = findViewById(R.id.contributor_recycler_view);
        districtSpinner = findViewById(R.id.districtDropdown);
        entry_form_button = findViewById(R.id.entryForm_button);
        backButton = findViewById(R.id.back_button);
        homeButton =findViewById(R.id.home);

        homeButton.setVisibility(View.GONE);

        databaseReference = FirebaseDatabase.getInstance().getReference("ContributorsList");

        requestList = new ArrayList<>();
        adapter = new Donation_Person_Adapter(requestList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        backButton.setOnClickListener(v -> backButton());

        entry_form_button.setOnClickListener(v -> entryFormOpen());

        setupDistrictSpinner();

        fetchRequests();
    }

    private void backButton() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void entryFormOpen() {
        Intent intent = new Intent(getApplicationContext(), Donation_Person_Entry_Form.class);
        startActivity(intent);
    }

    private void setupDistrictSpinner() {
        String[] districts = getResources().getStringArray(R.array.districtnamestring);

        List<String> districtList = new ArrayList<>();
        districtList.add("Select District");
        districtList.addAll(Arrays.asList(districts));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdownsampleview, R.id.dropdownview, districtList);
        districtSpinner.setAdapter(adapter);

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Donation_Person_Entry_Form_Handler handler = dataSnapshot.getValue(Donation_Person_Entry_Form_Handler.class);
                    if (handler != null) {
                        requestList.add(handler);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Donation_Person_List.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRequestsByDistrict(String district) {
        databaseReference.orderByChild("district").equalTo(district).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Donation_Person_Entry_Form_Handler handler = dataSnapshot.getValue(Donation_Person_Entry_Form_Handler.class);
                    if (handler != null) {
                        requestList.add(handler);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Donation_Person_List.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
