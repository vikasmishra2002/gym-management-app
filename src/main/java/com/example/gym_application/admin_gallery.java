package com.example.gym_application;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class admin_gallery extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 100;

    private ImageView gymImageView;
    private Button chooseImageButton;
    private Button uploadImageButton;

    private Uri selectedImageUri;
    Context context = this;
    ProgressDialog dialog;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_gallery);

        gymImageView = findViewById(R.id.gymImageView);
        chooseImageButton = findViewById(R.id.chooseImageButton);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        dialog = new ProgressDialog(context);

        storageReference = FirebaseStorage.getInstance().getReference();

        chooseImageButton.setOnClickListener(view -> openGallery()
        );

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                dialog.setCancelable(false);
                dialog.setMessage("Uploading...");
                dialog.show();
            }
        });
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    private void uploadImage() {
        if (selectedImageUri != null) {
            StorageReference imageRef = storageReference.child("gym_images/" + System.currentTimeMillis() + ".jpg");
            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        startActivity(new Intent(admin_gallery.this,AdminProfileActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
        } else {
            Toast.makeText(this, "Please select an image to upload", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            gymImageView.setImageURI(selectedImageUri);

        }
    }

}
