package com.example.gym_application;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText etUpdateName, etUpdateMobile, etUpdateDateOfBirth, etUpdateHeight, etUpdateWeight, etUpdateCity, etUpdateMedicalCondition;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String updateName, updateMobile, updateDateOfBirth, updateGender, updateHeight, updateWeight, updateCity, updateMedicalCondition, updateTimings, updatePackages, updateCore, updatePersonalTrainer = "No", updateZumba = "No", updateLocker = "No", updateSteamBath = "No";
    Button btUpdateProfile;
    Spinner spUpdateGender, spUpdateTimings, spUpdatePackages;
    CheckBox cbUpdatePersonalTrainer, cbUpdateZumba, cbUpdateLocker, cbUpdateSteamBath;
    RadioButton rbUpdateWeightTraining, rbUpdateCardioTraining;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        setupUI();
        if (spUpdateGender != null) {
            spUpdateGender.setOnItemSelectedListener(this);
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (spUpdateGender != null) {
            spUpdateGender.setAdapter(adapter);
        }

        if (spUpdateTimings != null) {
            spUpdateTimings.setOnItemSelectedListener(this);
        }
        ArrayAdapter<CharSequence> adTimings = ArrayAdapter.createFromResource(this, R.array.timings, android.R.layout.simple_spinner_item);
        adTimings.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (spUpdateTimings != null) {
            spUpdateTimings.setAdapter(adTimings);
        }

        if (spUpdatePackages != null) {
            spUpdatePackages.setOnItemSelectedListener(this);
        }
        ArrayAdapter<CharSequence> adPackages = ArrayAdapter.createFromResource(this, R.array.packages, android.R.layout.simple_spinner_item);
        adPackages.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (spUpdatePackages != null) {
            spUpdatePackages.setAdapter(adPackages);
        }

        showProgressDialog();

        btUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateButtonClick();
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Profile...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    private void setupUI() {
        etUpdateName = findViewById(R.id.etUpdateName);
        etUpdateMobile = findViewById(R.id.etUpdateMobile);
        etUpdateDateOfBirth = findViewById(R.id.etUpdateDateOfBirth);
        etUpdateHeight = findViewById(R.id.etUpdateHeight);
        etUpdateWeight = findViewById(R.id.etUpdateWeight);
        etUpdateCity = findViewById(R.id.etUpdateCity);
        etUpdateMedicalCondition = findViewById(R.id.etUpdateMedicalCondition);
        btUpdateProfile = findViewById(R.id.btUpdateProfile);
        spUpdateGender = findViewById(R.id.spUpdateGender);
        spUpdateTimings = findViewById(R.id.spUpdateTimings);
        spUpdatePackages = findViewById(R.id.spUpdatePackage);
        rbUpdateWeightTraining = findViewById(R.id.rbUpdateWeightTraining);
        rbUpdateCardioTraining = findViewById(R.id.rbUpdateCardioTraining);
        cbUpdatePersonalTrainer = findViewById(R.id.cbUpdatePersonalTrainer);
        cbUpdateZumba = findViewById(R.id.cbUpdateZumba);
        cbUpdateLocker = findViewById(R.id.cbUpdateLocker);
        cbUpdateSteamBath = findViewById(R.id.cbUpdateSteamBath);
        setProfile();
    }

    private void setProfile() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference usersRef = firebaseDatabase.getReference("user");
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

                        // Update UI with retrieved data (Example: Display in TextView)
                        etUpdateName.setText(name);
                        etUpdateMobile.setText(mobile);
                        etUpdateDateOfBirth.setText(date);
                        etUpdateHeight.setText(height);
                        etUpdateWeight.setText(weight);
                        etUpdateCity.setText(city);
                        etUpdateMedicalCondition.setText(medicalConditions);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                    Log.e("MainActivity", "Failed to read value.", databaseError.toException());
                }
            });
        }
    }

    private void onUpdateButtonClick() {
        updateName = etUpdateName.getText().toString();
        updateMobile = etUpdateMobile.getText().toString();
        updateDateOfBirth = etUpdateDateOfBirth.getText().toString();
        updateHeight = etUpdateHeight.getText().toString();
        updateWeight = etUpdateWeight.getText().toString();
        updateCity = etUpdateCity.getText().toString();
        updateMedicalCondition = etUpdateMedicalCondition.getText().toString();

        // Initialize and show the progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Updating profile...");
        progressDialog.show();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("user");
        DatabaseReference userRef = mDatabase.child(updateMobile);

        // data
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", updateName);
        userData.put("mobile", updateMobile);
        userData.put("date", updateDateOfBirth);
        userData.put("height", updateHeight);
        userData.put("weight", updateWeight);
        userData.put("city", updateCity);
        userData.put("core", updateCore);
        userData.put("gender", updateGender);
        userData.put("timings", updateTimings);
        userData.put("packages", updatePackages);
        userData.put("personalTrainer", updatePersonalTrainer);
        userData.put("zumba", updateZumba);
        userData.put("locker", updateLocker);
        userData.put("steamBath", updateSteamBath);
        userData.put("medicalConditions", etUpdateMedicalCondition.getText().toString());

        // Update the data in the database
        userRef.updateChildren(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data successfully updated
                        Log.d("MainActivity", "Data updated successfully.");
                        Toast.makeText(EditProfileActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss(); // Dismiss the progress dialog
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                        Log.e("MainActivity", "Failed to update data.", e);
                        Toast.makeText(EditProfileActivity.this, "Failed to update data", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss(); // Dismiss the progress dialog
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spOne = (Spinner) parent;
        Spinner spTwo = (Spinner) parent;
        Spinner spThree = (Spinner) parent;
        if (spOne.getId() == R.id.spUpdateGender) {
            updateGender = parent.getItemAtPosition(position).toString();
        } else if (spTwo.getId() == R.id.spUpdateTimings) {
            updateTimings = parent.getItemAtPosition(position).toString();
        } else if (spThree.getId() == R.id.spUpdatePackage) {
            updatePackages = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onUpdateCheckBoxClicked(View view) {
        Boolean checked = ((CheckBox) view).isChecked();
        if (view.getId() == R.id.cbUpdatePersonalTrainer) {
            if (checked) {
                updatePersonalTrainer = "Yes";
            } else {
                updatePersonalTrainer = "No";
            }
        } else if (view.getId() == R.id.cbUpdateZumba) {
            if (checked) {
                updateZumba = "Yes";
            } else {
                updateZumba = "No";
            }
        } else if (view.getId() == R.id.cbUpdateLocker) {
            if (checked) {
                updateLocker = "Yes";
            } else {
                updateLocker = "No";
            }
        } else if (view.getId() == R.id.cbUpdateSteamBath) {
            if (checked) {
                updateSteamBath = "Yes";
            } else {
                updateSteamBath = "No";
            }
        }
    }

    public void onUpdateRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (view.getId() == R.id.rbUpdateWeightTraining) {
            if (checked) {
                updateCore = getString(R.string.weight_training);
            }
        } else if (view.getId() == R.id.rbUpdateCardioTraining) {
            if (checked) {
                updateCore = getString(R.string.cardio_training);
            }
        }
    }
    public void showDatePickerDialog(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Update the EditText with the selected date
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                etUpdateDateOfBirth.setText(selectedDate);
            }
        };

        // Create a new DatePickerDialog instance
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                // Set default date to current date
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        // Show the DatePickerDialog
        datePickerDialog.show();
    }

}

