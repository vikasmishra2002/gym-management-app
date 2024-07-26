package com.example.gym_application;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;



public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

public static String value=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        value=sharedPreferences.getString("key_string","");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start your new activity here
                if (value.matches("Login_")){
                    startActivity(new Intent(SplashScreenActivity.this,DashboardActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                }
            }
        }, 2000);

    }


    }


