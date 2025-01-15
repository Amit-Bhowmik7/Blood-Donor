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
import android.widget.LinearLayout;
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

public class Facts_List extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayout searchLayout;
    private Facts_List_Adapter adapter;
    private List<Facts_Add_Handler> requestList;
    private DatabaseReference requestDatabase;
    private ImageButton backButton,homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts_list);

        // Initialize buttons
        backButton = findViewById(R.id.back_button);
        searchLayout = findViewById(R.id.searchLayout);
        homeButton =findViewById(R.id.home);

        homeButton.setVisibility(View.GONE);

        //disable search layout
        searchLayout.setVisibility(View.GONE);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.facts_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize request list and adapter
        requestList = new ArrayList<>();
        adapter = new Facts_List_Adapter(requestList);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Database Reference
        requestDatabase = FirebaseDatabase.getInstance().getReference("FactsList");

        // Intent for back page (mainActivity)
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // Fetch all data initially
        fetchRequests();
    }

    private void fetchRequests() {
        requestDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    Facts_Add_Handler request = requestSnapshot.getValue(Facts_Add_Handler.class);
                    requestList.add(0,request);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }
}