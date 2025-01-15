package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class Admin_HelpLine_Menu extends AppCompatActivity {
    private AppCompatButton helpLineAddButton, helpiLineListButton;
    private ImageButton backButton, homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_help_line_menu);

        helpLineAddButton = findViewById(R.id.helpLineAddButton);
        helpLineAddButton.setOnClickListener(v -> helpLINeAdd());

        helpiLineListButton = findViewById(R.id.helpLineListButton);
        helpiLineListButton.setOnClickListener(v -> helpLIneLIst());

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> back());

        homeButton = findViewById(R.id.home);
        homeButton.setOnClickListener(v -> home());
    }

    private void helpLIneLIst() {
        Intent intent =new Intent(getApplicationContext(), Admin_helpLIne_List.class);
        startActivity(intent);
    }

    private void helpLINeAdd() {
        Intent intent =new Intent(getApplicationContext(), Help_Line_Add.class);
        startActivity(intent);
    }

    private void home() {
        Intent intent =new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void back() {
        Intent intent =new Intent(getApplicationContext(), Admin_Menu.class);
        startActivity(intent);
    }

}