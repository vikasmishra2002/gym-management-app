package com.example.gym_application;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    ArrayList<String> imagelist;
    RecyclerView recyclerView;
    StorageReference root;
    ImageAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gallery);

        recyclerView = findViewById(R.id.Reyclerview_Image_list);
        imagelist = new ArrayList<>();
        adapter = new ImageAdapter(imagelist, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize and show the progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading images...");
        progressDialog.show();

        StorageReference listRef = FirebaseStorage.getInstance().getReference().child("gym_images");
        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference file : listResult.getItems()) {
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imagelist.add(uri.toString());
                            Log.e("Itemvalue", uri.toString());
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
                // Hide the progress dialog after loading is complete
                progressDialog.dismiss();
                recyclerView.setAdapter(adapter);
            }
        }).addOnFailureListener(e -> {
            // Hide the progress dialog in case of failure
            progressDialog.dismiss();
            Log.e("GalleryActivity", "Error fetching images", e);
        });
    }
}
