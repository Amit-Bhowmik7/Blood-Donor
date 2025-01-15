package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class Admin_Facts_Post_Edit extends AppCompatActivity {
    private Button updateButton, deleteButton;
    private ImageButton back_button;
    private ImageButton home;
    private TextInputLayout questionEditText, answerEditText;
    DatabaseReference factsDatabase;
    private static final String factsList = "factsList";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_facts_post_edit);

        //find all input field id
        questionEditText = findViewById(R.id.question_input);
        answerEditText = findViewById(R.id.answer_input);

        //button find
        updateButton = findViewById(R.id.update_post);
        deleteButton = findViewById(R.id.delete_post);
        home = findViewById(R.id.home);
        back_button = findViewById(R.id.back_button);

        displayRequestPost();

        home.setOnClickListener(v -> home());
        back_button.setOnClickListener(v -> back());

        updateButton.setOnClickListener(v -> updatePost());
        deleteButton.setOnClickListener(v -> deletePost());
    }

    private void home() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void back() {
        Intent intent = new Intent(getApplicationContext(), Admin_Facts_List.class);
        startActivity(intent);
    }

    private void deletePost() {
        Intent intent = new Intent(getApplicationContext(), Admin_Facts_Post_Delete_Dialog.class);
        startActivity(intent);
    }

    private void updatePost() {

        String questiion = questionEditText.getEditText().getText().toString().trim();
        String answer = answerEditText.getEditText().getText().toString().trim();

        SharedPreferences sharedPreferences = getSharedPreferences(factsList, MODE_PRIVATE);
        String requestId = sharedPreferences.getString("f_requestId","N/A");

        //validation check
        if (!questiion.isEmpty()){
            questionEditText.setError(null);
            questionEditText.setErrorEnabled(false);
            if (!answer.isEmpty()){
                answerEditText.setError(null);
                answerEditText.setErrorEnabled(false);

                factsDatabase = FirebaseDatabase.getInstance().getReference("FactsList");

                if(requestId !=null) {
                    factsDatabase.child(requestId).child("questiion").setValue(questiion);
                    factsDatabase.child(requestId).child("answer").setValue(answer);
                }

                Facts_Add_Handler newContributor = new Facts_Add_Handler(questiion, answer, requestId);
                factsDatabase.child(requestId).setValue(newContributor);


                Toast.makeText(getApplicationContext(), "Submit Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Admin_Facts_List.class);
                startActivity(intent);
                finish();

            }else {
                answerEditText.setError("Please enter the answer");
            }
        }else {
            questionEditText.setError("Please enter the question?");
        }
    }

    private void displayRequestPost() {
        SharedPreferences sharedPreferences = getSharedPreferences(factsList, MODE_PRIVATE);

        String question = sharedPreferences.getString("f_question", "N/A");
        String answer = sharedPreferences.getString("f_answer", "N/A");

        questionEditText.getEditText().setText(question);
        answerEditText.getEditText().setText(answer);
    }
}