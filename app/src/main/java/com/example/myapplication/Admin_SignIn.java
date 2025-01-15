package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Admin_SignIn extends AppCompatActivity {
    private TextInputLayout idLayout, passwordLayout;
    private TextInputEditText idEditText, passwordEditText;
    private ImageButton backButton;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_in);

        // Find views by ID
        idLayout = findViewById(R.id.adminIdNumber_input);
        passwordLayout = findViewById(R.id.adminPassword_input);
        signInButton = findViewById(R.id.adminSigninButton);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> backButton());

        // Access the inner EditText views
        idEditText = (TextInputEditText) idLayout.getEditText();
        passwordEditText = (TextInputEditText) passwordLayout.getEditText();


        // Set onClickListener for Sign In button
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve entered ID and password
                String enteredId = idEditText != null ? idEditText.getText().toString().trim() : "";
                String enteredPassword = passwordEditText != null ? passwordEditText.getText().toString().trim() : "";

                // Define valid credentials
                String validId = "admin";
                String validPassword = "admin";

                // Validate inputs
                if (enteredId.isEmpty() || enteredPassword.isEmpty()) {
                    idLayout.setError(enteredId.isEmpty() ? "ID is required" : null);
                    passwordLayout.setError(enteredPassword.isEmpty() ? "Password is required" : null);
                } else if (!enteredId.equals(validId) || !enteredPassword.equals(validPassword)) {
                    // Show error if credentials do not match
                    idLayout.setError(!enteredId.equals(validId) ? "Invalid ID" : null);
                    passwordLayout.setError(!enteredPassword.equals(validPassword) ? "Invalid Password" : null);
                } else {
                    // Clear any previous errors
                    idLayout.setError(null);
                    passwordLayout.setError(null);

                    // Intent to navigate to the next activity
                    Intent intent = new Intent(Admin_SignIn.this, Admin_Menu.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void backButton() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
