package com.example.gym_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AdminAnnouncementsActivity extends AppCompatActivity {

    EditText etAdminAnnouncementsDescription;
    EditText etAdminAnnouncementsHeading;
    Button btAdminAnnouncements;
    DatabaseReference databaseReference;
    ProgressDialog dialog;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_announcements);

        // Initialize Firebase database reference

        dialog=new ProgressDialog(context);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Notice");

        // Setup UI
        etAdminAnnouncementsDescription = findViewById(R.id.etAdminAnnouncementsDescription);
        etAdminAnnouncementsHeading = findViewById(R.id.etAdminAnnouncementsHeading);
        btAdminAnnouncements = findViewById(R.id.btAdminAnnouncements);

        // Button click listener to send announcement
        btAdminAnnouncements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAnnouncement();
                dialog.setCancelable(false);
                dialog.setMessage("Uploading...");
                dialog.show();
            }
        });
    }

    private void sendAnnouncement() {
        String heading = etAdminAnnouncementsHeading.getText().toString();
        String description = etAdminAnnouncementsDescription.getText().toString();

        // Check if both heading and description are not empty
        if (TextUtils.isEmpty(heading) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please enter both heading and description", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map to store announcement data
        Map<String, Object> announcementData = new HashMap<>();
        announcementData.put("heading", heading);
        announcementData.put("description", description);

        // Push the announcement data to Firebase database
        databaseReference.push().setValue(announcementData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminAnnouncementsActivity.this, "Announcement added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(AdminAnnouncementsActivity.this, "Failed to add announcement", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
    }
}
