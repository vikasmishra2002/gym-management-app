package com.example.gym_application;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_view_profile extends AppCompatActivity {

    private DatabaseReference databaseReference;
    RecyclerView recyclerView;
    private AnnouncementsAdapter adapter;
    ProgressDialog dialog;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_profile);
        recyclerView = findViewById(R.id.recyclerView_admin);
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");
        dialog.show();
        // Get a reference to the Firebase Realtime Database

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Announcement> announcements = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String heading = snapshot.child("name").getValue(String.class);
                    String description = snapshot.child("mobile").getValue(String.class);
                    Announcement announcement = new Announcement(heading, description);
                    announcements.add(announcement);
                }

                adapter = new AnnouncementsAdapter(announcements, getApplicationContext());
                recyclerView.setAdapter(adapter);
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to read value.", databaseError.toException());
                dialog.dismiss();
            }
        });
    }
}
