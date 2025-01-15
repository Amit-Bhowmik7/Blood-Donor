package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedBack_Form extends AppCompatActivity {

    private TextInputLayout nameEditTExt, mobileNumberEditText, emailEditText, messageEditText;
    private Button sendButton;
    private ImageButton backButton, homeButton;
    DatabaseReference feedBackRefrence;
    private static final String PREFERENCES = "UserPrefs";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_form);

        // Initialize input fields and button
        nameEditTExt = findViewById(R.id.feedBack_Name_input);
        mobileNumberEditText = findViewById(R.id.feedBack_mobileNumber_input);
        emailEditText = findViewById(R.id.feedBack_email_input);
        messageEditText = findViewById(R.id.feedBack_message_input);
        backButton = findViewById(R.id.back_button);
        homeButton = findViewById(R.id.home);
        sendButton = findViewById(R.id.feedBack_send);

        //disable home button
        homeButton.setVisibility(View.GONE);

        //intent for go to back page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedBack_Form.this, MainActivity.class);
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
        String email = emailEditText.getEditText().getText().toString().trim();
        String message = messageEditText.getEditText().getText().toString().trim();

        // Validate inputs
        if(!name.isEmpty()){
            nameEditTExt.setError(null);
            nameEditTExt.setErrorEnabled(false);
            if(!mobileNumber.isEmpty() && isValidBangladeshiPhoneNumber(mobileNumber)){
                mobileNumberEditText.setError(null);
                mobileNumberEditText.setErrorEnabled(false);
                if(!email.isEmpty()){
                    emailEditText.setError(null);
                    emailEditText.setErrorEnabled(false);
                    if(!message.isEmpty()){
                        messageEditText.setError(null);
                        messageEditText.setErrorEnabled(false);

                        feedBackRefrence = FirebaseDatabase.getInstance().getReference("FeedBackList");
                        feedBackRefrence.child(mobileNumber).child("name").setValue(name);
                        feedBackRefrence.child(mobileNumber).child("mobileNumber").setValue(mobileNumber);
                        feedBackRefrence.child(mobileNumber).child("email").setValue(email);
                        feedBackRefrence.child(mobileNumber).child("message").setValue(message);

                        Toast.makeText(getApplicationContext(),"message sent successfully",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();



                    }else {
                        messageEditText.setError("Please enter your message");
                    }
                }else {
                    emailEditText.setError("Please enter your valid email address");
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
