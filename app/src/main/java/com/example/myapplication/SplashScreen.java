package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressbar;
    private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressbar = findViewById(R.id.progressbarid);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                dowork();
                mainactivity();
            }
        });
        thread.start();
    }

    public void dowork(){
        for (progress=20; progress<=100; progress = progress+20 ){
            try {
                Thread.sleep(1000);
                progressbar.setProgress(progress);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }

    public void mainactivity(){
        Intent intent = new Intent(SplashScreen.this, LogIn.class);
        startActivity(intent);
        finish();
    }
}