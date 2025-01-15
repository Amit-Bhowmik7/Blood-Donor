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

public class Admin_HelpLIne_Post_Edit extends AppCompatActivity {
    private TextInputLayout titleEditText, contactEditText;
    private AppCompatButton updateButton, deleteBUtton;
    private ImageButton backButton, homeButton;
    DatabaseReference helLineDataBase;
    private static final String helpLineList = "helpLineList";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_help_line_post_edit);

        //find id
        titleEditText = findViewById(R.id.titel_input);
        contactEditText = findViewById(R.id.contactNumber_input);

        updateButton = findViewById(R.id.update_post);
        deleteBUtton = findViewById(R.id.delete_post);
        backButton = findViewById(R.id.back_button);
        homeButton = findViewById(R.id.home);

        displayRequestPost();

        //set on click listener
        backButton.setOnClickListener(v -> back());
        homeButton.setOnClickListener(v -> home());
        updateButton.setOnClickListener(v -> updatePost());
        deleteBUtton.setOnClickListener(v -> deletePost());

    }

    private void updatePost() {

        String title = titleEditText.getEditText().getText().toString().trim();
        String contact = contactEditText.getEditText().getText().toString().trim();

        SharedPreferences sharedPreferences = getSharedPreferences(helpLineList, MODE_PRIVATE);
        String requestId = sharedPreferences.getString("h_requestId","N/A");

        //validation check
        if (!title.isEmpty()){
            titleEditText.setError(null);
            titleEditText.setErrorEnabled(false);
            if (!contact.isEmpty()){
                contactEditText.setError(null);
                contactEditText.setErrorEnabled(false);

                helLineDataBase = FirebaseDatabase.getInstance().getReference("HelpLineList");

                if(requestId !=null) {
                    helLineDataBase.child(requestId).child("title").setValue(title);
                    helLineDataBase.child(requestId).child(" contactNumber").setValue(contact);
                }

                Help_Line_Add_Handler newHelpLine = new Help_Line_Add_Handler(title, contact, requestId);
                helLineDataBase.child(requestId).setValue(newHelpLine);


                Toast.makeText(getApplicationContext(), "Submit Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Admin_helpLIne_List.class);
                startActivity(intent);
                finish();

            }else {
                contactEditText.setError("Please enter the contact number");
            }
        }else {
            titleEditText.setError("Please enter the title");
        }
    }

    private void displayRequestPost() {
        SharedPreferences sharedPreferences = getSharedPreferences(helpLineList, MODE_PRIVATE);

        String title = sharedPreferences.getString("h_title", "N/A");
        String contact = sharedPreferences.getString("h_contactNumber", "N/A");

        titleEditText.getEditText().setText(title);
        contactEditText.getEditText().setText(contact);
    }

    private void home() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void back() {
        Intent intent = new Intent(getApplicationContext(), Admin_helpLIne_List.class);
        startActivity(intent);
    }

    private void deletePost() {
        Intent intent = new Intent(getApplicationContext(), Admin_HelpLIne_Post_Delete_Dialog.class);
        startActivity(intent);
    }
}