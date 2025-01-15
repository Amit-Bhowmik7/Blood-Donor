package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class Admin_Facts_Menu extends AppCompatActivity {
    private AppCompatButton factsAddButton, factsListButton;
    private ImageButton backButton, homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_facts_menu);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> back());

        homeButton = findViewById(R.id.home);
        homeButton.setOnClickListener(v -> home());

        factsAddButton = findViewById(R.id.factsAddButton);
        factsAddButton.setOnClickListener(v -> factsAdd());

        factsListButton =findViewById(R.id.factsListButton);
        factsListButton.setOnClickListener(v -> factsList());
    }

    private void home() {
        Intent intent =new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void back() {
        Intent intent =new Intent(getApplicationContext(), Admin_Menu.class);
        startActivity(intent);
    }

    private void factsList() {
        Intent intent =new Intent(getApplicationContext(), Admin_Facts_List.class);
        startActivity(intent);
    }

    private void factsAdd() {
        Intent intent =new Intent(getApplicationContext(), Facts_Add.class);
        startActivity(intent);
    }
}