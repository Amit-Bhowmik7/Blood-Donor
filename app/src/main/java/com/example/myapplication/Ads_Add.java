package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class Ads_Add extends AppCompatActivity implements View.OnClickListener {
    private AppCompatButton imageChooseButton, submitButton;
    private AppCompatImageView imageView;
    private TextInputLayout adsTitle;
    private ProgressBar progressBar;
    private Uri imageUri;
    private static final int imageRequest = 1;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_add);

        // Find all views by ID
        imageChooseButton = findViewById(R.id.chooseImageButton);
        adsTitle = findViewById(R.id.adsTitle);
        imageView = findViewById(R.id.adsImageView);
        submitButton = findViewById(R.id.adsImageSave);

        databaseReference = FirebaseDatabase.getInstance().getReference("AdvertiseList");
        storageReference = FirebaseStorage.getInstance().getReference("AdvertiseImage");

        imageChooseButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseImageButton:
                openFileChooser();
                break;

            case R.id.adsImageSave:
                saveImage();
                break;
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, imageRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == imageRequest && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageView);
        }
    }

    public String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    private void saveImage() {
        String advertiseTitle = adsTitle.getEditText().getText().toString().trim();

        if (!advertiseTitle.isEmpty()) {
            adsTitle.setError(null);
            adsTitle.setErrorEnabled(false);

            if (imageUri != null) {
                StorageReference imageUniqName = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

                imageUniqName.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                    imageUniqName.getDownloadUrl().addOnSuccessListener(uri -> {
                        Advertise_Add_Handler upload = new Advertise_Add_Handler(advertiseTitle, uri.toString());

                        String advertiseId = UUID.randomUUID().toString();
                        databaseReference.child(advertiseId).setValue(upload)
                                .addOnSuccessListener(unused -> Toast.makeText(getApplicationContext(), "Advertise successfully uploaded", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to save data in database", Toast.LENGTH_SHORT).show());
                    }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to retrieve download URL", Toast.LENGTH_SHORT).show());
                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Advertise upload failed", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        } else {
            adsTitle.setError("Please enter the advertise title");
        }
    }
}
