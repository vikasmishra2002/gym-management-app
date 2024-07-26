package com.example.gym_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    TextView tvForgotPassword, tvRegister;
    Button btLogin;
    private FirebaseAuth fbAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fbAuth = FirebaseAuth.getInstance();
        setupUI();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String pass = etPassword.getText().toString();
                if(email.matches("hello@admin.com")&&pass.matches("123456789")){
                    startActivity(new Intent(LoginActivity.this, AdminProfileActivity.class));
                }else{loginUserAccount();}

            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void setupUI() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);
        btLogin = findViewById(R.id.btLogin);
    }
    private void loginUserAccount()
    {

        // show the visibility of progress bar to show loading
//        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // signin existing user
        fbAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                                    "Login successful!!",
                                                    Toast.LENGTH_LONG)
                                            .show();
                                    // Initialize SharedPreferences
                                    SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);


                                 String value=sharedPreferences.getString("key_string","");
                                    if (value.matches("Login_")){
                                        startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                                        finish();
                                    }else {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("key_string", "Login_");
                                        editor.apply();
                                        startActivity(new Intent(LoginActivity.this,AddProfileActivity.class));
                                        finish();
                                    }

                                }

                                else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                                    "Login failed!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
//                                    progressbar.setVisibility(View.GONE);
                                }
                            }
                        });
    }

    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Can't go back", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, LoginActivity.class));
        super.onBackPressed();
    }
}