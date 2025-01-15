package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_helpLIne_List extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Admin_HelpLIne_List_Adapter adapter;
    private List<Help_Line_Add_Handler> requestList;
    private DatabaseReference requestDatabase;
    private ImageButton backButton, homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_help_line_list);

        // Initialize buttons
        backButton = findViewById(R.id.back_button);
        homeButton = findViewById(R.id.home);

        //hide home button
        homeButton.setVisibility(View.GONE);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.helpLine_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize request list and adapter
        requestList = new ArrayList<>();
        adapter = new Admin_HelpLIne_List_Adapter(requestList);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Database Reference
        requestDatabase = FirebaseDatabase.getInstance().getReference("HelpLineList");

        // Intent for back page (mainActivity)
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Admin_HelpLine_Menu.class);
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
                    Help_Line_Add_Handler request = requestSnapshot.getValue(Help_Line_Add_Handler.class);
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