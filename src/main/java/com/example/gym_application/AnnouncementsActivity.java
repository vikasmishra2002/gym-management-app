package com.example.gym_application;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementsActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    DynamicAnnouncements dynamicAnnouncements;
    Context context;
    LinearLayout llAnnouncements;
    int count=0;
    RecyclerView recyclerView;
    private AnnouncementsAdapter adapter;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.recyclerView);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notice");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Announcement> announcements = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String heading = snapshot.child("heading").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    Announcement announcement = new Announcement(heading, description);
                    announcements.add(announcement);
                }

                adapter = new AnnouncementsAdapter(announcements,getApplicationContext());
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

}