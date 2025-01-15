package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class Contact_Form extends AppCompatActivity {

    private TextInputLayout nameEditTExt, mobileNumberEditText, messageEditText;
    private Button sendButton;
    private ImageButton backButton, homeButton;
    private static final String PREFERENCES = "UserPrefs";
    private ImageButton back_button;
    private Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);

        // Initialize input fields and button
        nameEditTExt = findViewById(R.id.contact_Name_input);
        mobileNumberEditText = findViewById(R.id.contact_mobileNumber_input);
        messageEditText = findViewById(R.id.contact_message_input);
        backButton = findViewById(R.id.back_button);
        homeButton = findViewById(R.id.home);
        sendButton = findViewById(R.id.contact_send);

        //disable home button
        homeButton.setVisibility(View.GONE);

        //intent for go to back page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Contact_Form.this, MainActivity.class);
                startActivity(intent);
            }
        });

        displayFeedbackData();

        // Set OnClickListener for the send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void displayFeedbackData() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);

        String name = sharedPreferences.getString("name", "N/A");
        String phoneNumber = sharedPreferences.getString("mobileNumber", "N/A");

        nameEditTExt.getEditText().setText(name);
        mobileNumberEditText.getEditText().setText(phoneNumber);
    }

    private void sendEmail() {
        // Extract values from input fields
        String name = nameEditTExt.getEditText().getText().toString().trim();
        String mobileNumber = mobileNumberEditText.getEditText().getText().toString().trim();
        String message = messageEditText.getEditText().getText().toString().trim();

        // Validate inputs
        if(!name.isEmpty()){
            nameEditTExt.setError(null);
            nameEditTExt.setErrorEnabled(false);
            if(!mobileNumber.isEmpty() && isValidBangladeshiPhoneNumber(mobileNumber)){
                mobileNumberEditText.setError(null);
                mobileNumberEditText.setErrorEnabled(false);
                if(!message.isEmpty()){
                    messageEditText.setError(null);
                    messageEditText.setErrorEnabled(false);

                    // Prepare email content
                    String subject = name+" (New Contact Person)";
                    String emailMessage = "Name: " + name + "\n" +
                            "Mobile Number: " + mobileNumber + "\n" +
                            "Message: " + message;

                    // Send email via Intent
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("message/rfc822"); // Restrict to email apps
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"amitct7@gmail.com"});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailMessage);

                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send Email"));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(this, "No email client found", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    messageEditText.setError("Please enter your message");
                }
            }else {
                mobileNumberEditText.setError("Please enter your valid mobile number");
            }
        }else {
            nameEditTExt.setError("Please enter your full name");
        }
    }

    //Bangladeshi number pattern for input number
    private boolean isValidBangladeshiPhoneNumber(String phoneNumber) {
        // Check if the phone number is 11 digits long and starts with "01"
        return phoneNumber.matches("^01[3-9]\\d{8}$");
    }
}