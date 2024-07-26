package com.example.gym_application;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileActivity extends AppCompatActivity {

    TextView tvName, tvMobile, tvDateOfBirth, tvGender, tvHeight, tvWeight, tvCity, tvMedicalCondition, tvTimings, tvPackages, tvCore, tvPersonalTrainer, tvZumba, tvLocker, tvSteamBath;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    final String yes = "Yes";
    final String no = "No";
    CircleImageView ivDashboardProfilePic;
    StorageReference storageReference;
    final long ONE_MB = 1024 * 1024;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        setupUI();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        getData();
    }

    private void setupUI() {
        tvName = findViewById(R.id.tvName);
        tvMobile = findViewById(R.id.tvMobile);
        tvDateOfBirth = findViewById(R.id.tvDateOfBirth);
        tvGender = findViewById(R.id.tvGender);
        tvHeight = findViewById(R.id.tvHeight);
        tvWeight = findViewById(R.id.tvWeight);
        tvCity = findViewById(R.id.tvCity);
        tvMedicalCondition = findViewById(R.id.tvMedicalCondition);
        tvTimings = findViewById(R.id.tvTimings);
        tvPackages = findViewById(R.id.tvPackages);
        tvCore = findViewById(R.id.tvCore);
        tvPersonalTrainer = findViewById(R.id.tvPersonalTrainer);
        tvZumba = findViewById(R.id.tvZumba);
        tvLocker = findViewById(R.id.tvLocker);
        tvSteamBath = findViewById(R.id.tvSteamBath);
        ivDashboardProfilePic = findViewById(R.id.ivDashboardProfilePic);
    }

    private void getData() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference usersRef = firebaseDatabase.getReference("user");

            // Show ProgressDialog when data fetching starts
            progressDialog.show();

            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterate through each child node
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Retrieve data and populate UI
                        String name = snapshot.child("name").getValue(String.class);
                        String city = snapshot.child("city").getValue(String.class);
                        String core = snapshot.child("core").getValue(String.class);
                        String date = snapshot.child("date").getValue(String.class);
                        String gender = snapshot.child("gender").getValue(String.class);
                        String height = snapshot.child("height").getValue(String.class);
                        String locker = snapshot.child("locker").getValue(String.class);
                        String medicalConditions = snapshot.child("medicalConditions").getValue(String.class);
                        String mobile = snapshot.child("mobile").getValue(String.class);
                        String packages = snapshot.child("packages").getValue(String.class);
                        String personalTrainer = snapshot.child("personalTrainer").getValue(String.class);
                        String steamBath = snapshot.child("steamBath").getValue(String.class);
                        String timings = snapshot.child("timings").getValue(String.class);
                        String weight = snapshot.child("weight").getValue(String.class);
                        String zumba = snapshot.child("zumba").getValue(String.class);

                        // Update UI with retrieved data
                        tvName.setText("Name: " + name);
                        tvCity.setText("City: " + city);
                        tvMobile.setText("Mobile: " + mobile);
                        tvDateOfBirth.setText("D.O.B: " + date);
                        tvGender.setText("Gender: " + gender);
                        tvHeight.setText(height + " cm");
                        tvWeight.setText(weight + " Kg");
                        tvMedicalCondition.setText("Medical Condition: " + medicalConditions);
                        tvTimings.setText("Timings: " + timings);
                        tvPackages.setText("Package: " + packages);
                        tvCore.setText("Core: " + core);
                        tvLocker.setText("Locker: " + locker);
                        tvZumba.setText("Zumba: " + zumba);
                        tvPersonalTrainer.setText("Personal Trainer: " + personalTrainer);
                        tvSteamBath.setText("Steam Bath: " + steamBath);
                    }
                    // Dismiss ProgressDialog once data is loaded
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                    Log.e("ViewProfileActivity", "Failed to read value.", databaseError.toException());
                    // Dismiss ProgressDialog on error
                    progressDialog.dismiss();
                }
            });
        } else {
            // User is not authenticated, redirect to login screen
            Intent intent = new Intent(ViewProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setOptionalFeaturesVisibility(UserProfile userProfile) {
        if (yes.equals(userProfile.getPersonalTrainer())) {
            tvPersonalTrainer.setText(R.string.personal_trainer);
        } else {
            tvPersonalTrainer.setVisibility(View.GONE);
        }

        if (yes.equals(userProfile.getZumba())) {
            tvZumba.setText(R.string.zumba);
        } else {
            tvZumba.setVisibility(View.GONE);
        }

        if (yes.equals(userProfile.getLocker())) {
            tvLocker.setText(R.string.locker);
        } else {
            tvLocker.setVisibility(View.GONE);
        }

        if (yes.equals(userProfile.getSteamBath())) {
            tvSteamBath.setText(R.string.steam_bath);
        } else {
            tvSteamBath.setVisibility(View.GONE);
        }
    }

//    private void loadProfilePicture(String uid) {
//        showLoadingPanel();
//        StorageReference imageRef = storageReference.child("Images/ProfilePic/" + uid);
//
//        imageRef.getBytes(ONE_MB)
//                .addOnSuccessListener(bytes -> {
//                    hideLoadingPanel();
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    ivDashboardProfilePic.setImageBitmap(bitmap);
//                })
//                .addOnFailureListener(exception -> {
//                    hideLoadingPanel();
//                    Toast.makeText(ViewProfileActivity.this, "Failed to load profile picture", Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, "Error loading profile picture", exception);
//                });
//    }
}
