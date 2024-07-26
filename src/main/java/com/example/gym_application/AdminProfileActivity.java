package com.example.gym_application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class AdminProfileActivity extends AppCompatActivity {

    FirebaseAuth fbAuth;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        fbAuth = FirebaseAuth.getInstance();
        setupUI();
    }

    private void setupUI() {
    }

    public void dashboardLogOut(View view) {
        fbAuth.signOut();
        Toast.makeText(AdminProfileActivity.this, "Logout Successful!", Toast.LENGTH_SHORT).show();
        CacheUtils.clearAllSharedPreferences(context);
        logout();
        finish();
    }

    public void viewProfile(View view) {
        Toast.makeText(this, "View Profile", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Admin_view_profile.class));
    }


    public void viewGallery(View view) {

        Toast.makeText(this, "Gallery", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, admin_gallery.class));
    }

    public void viewAnnouncements(View view) {
        Toast.makeText(this, "Notice", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(AdminProfileActivity.this, AdminAnnouncementsActivity.class));
    }

    public void logout() {

        SplashScreenActivity.value = null;
        Intent intent = new Intent(AdminProfileActivity.this, LoginActivity.class);

        // Clear the activity stack to prevent going back to the Dashboard
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        // Finish the current activity
        finish();

        // Display a logout message using a Toast
        Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();

    }
}