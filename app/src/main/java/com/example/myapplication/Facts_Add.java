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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class Facts_Add extends AppCompatActivity {
    private Button factsSave_button;
    private ImageButton back_button;
    private ImageButton home;
    private TextInputLayout questionEditText, answerEditText;
    DatabaseReference factsDatabase;
    private static final String factsList = "factsList";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts_add);

        //find all input field id
        questionEditText = findViewById(R.id.question_input);
        answerEditText = findViewById(R.id.answer_input);

        //button find
        factsSave_button = findViewById(R.id.facts_Save);
        home = findViewById(R.id.home);
        back_button = findViewById(R.id.back_button);

        //data stored and get from database
        factsSave_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String questiion = questionEditText.getEditText().getText().toString().trim();
                String answer = answerEditText.getEditText().getText().toString().trim();
                String requestId = UUID.randomUUID().toString();

                //validation check
                if (!questiion.isEmpty()){
                    questionEditText.setError(null);
                    questionEditText.setErrorEnabled(false);
                    if (!answer.isEmpty()){
                        answerEditText.setError(null);
                        answerEditText.setErrorEnabled(false);

                            factsDatabase = FirebaseDatabase.getInstance().getReference("FactsList");
                            Facts_Add_Handler newContributor = new Facts_Add_Handler(questiion, answer, requestId);
                            factsDatabase.child(requestId).setValue(newContributor);

                            SharedPreferences volunteerPreferences = getSharedPreferences(factsList, MODE_PRIVATE);
                            SharedPreferences.Editor editor = volunteerPreferences.edit();

                            editor.putString("f_question",questiion);
                            editor.putString("f_answer",answer);
                            editor.putString("f_requestId",requestId);
                            editor.apply();

                            Toast.makeText(getApplicationContext(), "Submit Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Facts_Add.class);
                            startActivity(intent);
                            finish();

                    }else {
                        answerEditText.setError("Please enter the answer");
                    }
                }else {
                    questionEditText.setError("Please enter the question?");
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Admin_Facts_Menu.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}