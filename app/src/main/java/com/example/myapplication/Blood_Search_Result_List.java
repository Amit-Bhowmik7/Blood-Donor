package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Blood_Search_Result_List extends AppCompatActivity {
    private ImageButton backButton, homeButton;
    private RecyclerView recyclerView;
    private Blood_Search_Adapter adapter;
    private List<User> userList;
    private String selectedBloodGroup, selectedDistrict;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_search_result_list);

        // Initialize UI components
        recyclerView = findViewById(R.id.searchList_recycler_view);
        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.back_button);
        homeButton = findViewById(R.id.home);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();

        // Get the selected blood group and district from intent
        selectedBloodGroup = getIntent().getStringExtra("bloodGroup");
        selectedDistrict = getIntent().getStringExtra("district");

        // Fetch data from Firebase
        fetchDataFromFirebase();

        // Back button implementation
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Blood_Search_Result_List.this, Blood_Search.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        // Home button implementation
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(Blood_Search_Result_List.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void fetchDataFromFirebase() {
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UsersList");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                userList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if (user != null && matchesSearchCriteria(user)) {
                        userList.add(user);
                    }
                }

                if (userList.isEmpty()) {
                    Toast.makeText(Blood_Search_Result_List.this, "No matches found.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Blood_Search_Result_List.this, userList.size() + " matches found.", Toast.LENGTH_SHORT).show();
                }

                adapter = new Blood_Search_Adapter(userList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Blood_Search_Result_List.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper method to check if a user matches the search criteria
    private boolean matchesSearchCriteria(User user) {
        boolean bloodGroupMatches = selectedBloodGroup == null || selectedBloodGroup.isEmpty() || user.getBloodGroup().equalsIgnoreCase(selectedBloodGroup);
        boolean districtMatches = selectedDistrict == null || selectedDistrict.isEmpty() || user.getDistrict().equalsIgnoreCase(selectedDistrict);
        return bloodGroupMatches && districtMatches;
    }
}
