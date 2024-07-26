package com.example.gym_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText etAddName,etAddMobile,etAddDateOfBirth,etHeight,etWeight,etCity,etMedicalCondition;
    Button btAddProfile;
    Spinner spGender,spTimings,spPackages;
    CircleImageView ivProfilePic;
    String spGenderLabel,userName,userDob,userMobile,spTimingsLabel,spPackageLabel,userHeight,userWeight,userCity,userMedicalCondition,userCore,userPersonalTrainer="No", userZumba="No", userLocker="No", userSteamBath="No";
    FirebaseAuth fbAuth;
    CheckBox cbPersonalTrainer, cbZumba, cbLocker, cbSteamBath;
    RadioButton rbWeightTraining, rbCardioTraining;

    private static final int PICK_IMAGE = 123;
    Uri imagePath;

    private StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile);
        setupUI();
        fbAuth = FirebaseAuth.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        if(spGender!=null){
            spGender.setOnItemSelectedListener(this);
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(spGender!=null){
            spGender.setAdapter(adapter);
        }

        if(spTimings!=null){
            spTimings.setOnItemSelectedListener(this);
        }
        ArrayAdapter<CharSequence> adTimings = ArrayAdapter.createFromResource(this,R.array.timings,android.R.layout.simple_spinner_item);
        adTimings.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(spTimings!=null){
            spTimings.setAdapter(adTimings);
        }

        if(spPackages!=null){
            spPackages.setOnItemSelectedListener(this);
        }
        ArrayAdapter<CharSequence> adPackages = ArrayAdapter.createFromResource(this,R.array.packages,android.R.layout.simple_spinner_item);
        adPackages.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(spPackages!=null){
            spPackages.setAdapter(adPackages);
        }

        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });

        btAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = etAddName.getText().toString();
                userMobile = etAddMobile.getText().toString();
                userDob = etAddDateOfBirth.getText().toString();
                userHeight = etHeight.getText().toString();
                userWeight = etWeight.getText().toString();
                userCity = etCity.getText().toString();
                userMedicalCondition = etMedicalCondition.getText().toString();
                if(imagePath==null){
                    Toast.makeText(AddProfileActivity.this, "Add profile Pic!", Toast.LENGTH_SHORT).show();
                } else {
                    sendUserProfile();
                }
            }
        });
    }

    private void setupUI() {
        etAddName = findViewById(R.id.etAddName);
        etAddMobile = findViewById(R.id.etAddMobile);
        etAddDateOfBirth = findViewById(R.id.etAddDateOfBirth);
        btAddProfile = findViewById(R.id.btAddProfile);
        spGender = findViewById(R.id.spGender);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        rbWeightTraining = findViewById(R.id.rbWeightTraining);
        rbCardioTraining = findViewById(R.id.rbCardioTraining);
        cbPersonalTrainer = findViewById(R.id.cbPersonalTrainer);
        cbZumba = findViewById(R.id.cbZumba);
        cbLocker = findViewById(R.id.cbLocker);
        cbSteamBath = findViewById(R.id.cbSteamBath);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etCity = findViewById(R.id.etCity);
        etMedicalCondition = findViewById(R.id.etMedicalCondition);
        spTimings = findViewById(R.id.spTimings);
        spPackages = findViewById(R.id.spPackage);

        etAddDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spOne = (Spinner) parent;
        Spinner spTwo = (Spinner) parent;
        Spinner spThree = (Spinner) parent;
        if(spOne.getId()==R.id.spGender){
            spGenderLabel = parent.getItemAtPosition(position).toString();
        } else if(spTwo.getId()==R.id.spTimings){
            spTimingsLabel = parent.getItemAtPosition(position).toString();
        } else if(spThree.getId()==R.id.spPackage){
            spPackageLabel = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @SuppressLint("NonConstantResourceId")
    public void onCheckBoxClicked(View view) {
        Boolean checked = ((CheckBox) view).isChecked();
        if (view.getId() == R.id.cbPersonalTrainer) {
            if (checked) {
                userPersonalTrainer = "Yes";
            } else {
                userPersonalTrainer = "No";
            }
        } else if (view.getId() == R.id.cbZumba) {
            if (checked) {
                userZumba = "Yes";
            } else {
                userZumba = "No";
            }
        } else if (view.getId() == R.id.cbLocker) {
            if (checked) {
                userLocker = "Yes";
            } else {
                userLocker = "No";
            }
        } else if (view.getId() == R.id.cbSteamBath) {
            if (checked) {
                userSteamBath = "Yes";
            } else {
                userSteamBath = "No";
            }
        }
    }

    private void sendUserProfile(){
        FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = fbDatabase.getReference("user").push(); // Reference directly to "user" table
        updateUserProfilePic();
        UserProfile userProfile = new UserProfile(userName,userMobile,userDob,spGenderLabel,userHeight,userWeight,userCity,userMedicalCondition,spTimingsLabel,spPackageLabel,userCore,userPersonalTrainer,userZumba,userLocker,userSteamBath);
        myRef.setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddProfileActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void updateUserProfilePic(){
        StorageReference imageReference = storageReference.child("Images").child("ProfilePic").child(fbAuth.getUid());
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddProfileActivity.this, "File Upload Failed", Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddProfileActivity.this, "File Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddProfileActivity.this,DashboardActivity.class));
                finish();
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (view.getId() == R.id.rbWeightTraining) {
            if (checked) {
                userCore = getString(R.string.weight_training);
            }
        } else if (view.getId() == R.id.rbCardioTraining) {
            if (checked) {
                userCore = getString(R.string.cardio_training);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null)
            imagePath = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
            ivProfilePic.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void showDatePickerDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                etAddDateOfBirth.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

}