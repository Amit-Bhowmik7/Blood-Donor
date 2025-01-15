package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class Help_Line_Add extends AppCompatActivity {

    private TextInputLayout titleEditText, contactEditText;
    private AppCompatButton submitButton;
    private ImageButton backButton, homeButton;
    DatabaseReference helLineDataBase;
    private static final String helpLineList = "helpLineList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_line_add);

        //find id
        titleEditText = findViewById(R.id.titel_input);
        contactEditText = findViewById(R.id.contactNumber_input);
        submitButton = findViewById(R.id.help_line_submitButton);
        backButton = findViewById(R.id.back_button);
        homeButton = findViewById(R.id.home);

        //set on click listener
        backButton.setOnClickListener(v -> backButton());
        homeButton.setOnClickListener(v -> homeButton());
        submitButton.setOnClickListener(v -> submitButton());
    }

    private void submitButton() {

        String title = titleEditText.getEditText().getText().toString().trim();
        String contact = contactEditText.getEditText().getText().toString().trim();
        String requestId = UUID.randomUUID().toString();

        //validation check
        if (!title.isEmpty()){
            titleEditText.setError(null);
            titleEditText.setErrorEnabled(false);
            if (!contact.isEmpty()){
                contactEditText.setError(null);
                contactEditText.setErrorEnabled(false);

                helLineDataBase = FirebaseDatabase.getInstance().getReference("HelpLineList");
                Help_Line_Add_Handler newHelpLine = new Help_Line_Add_Handler(title, contact, requestId);
                helLineDataBase.child(requestId).setValue(newHelpLine);

                SharedPreferences helpLinePreferences = getSharedPreferences(helpLineList, MODE_PRIVATE);
                SharedPreferences.Editor editor = helpLinePreferences.edit();

                editor.putString("h_title",title);
                editor.putString("h_contactNumber",contact);
                editor.putString("h_requestId",requestId);
                editor.apply();

                Toast.makeText(getApplicationContext(), "Submit Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Help_Line_Add.class);
                startActivity(intent);
                finish();

            }else {
                contactEditText.setError("Please enter the contact number");
            }
        }else {
            titleEditText.setError("Please enter the title");
        }

    }

    private void homeButton() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void backButton() {
        Intent intent = new Intent(getApplicationContext(), Admin_HelpLine_Menu.class);
        startActivity(intent);
    }
}