package com.example.gym_application;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.File;

public class  CacheUtils

    {
        public static void clearAllSharedPreferences(Context context) {
            // List of all SharedPreferences files used in your app
            String[] sharedPreferencesNames = {"my_prefs"}; // Replace with your SharedPreferences file names

            for (String sharedPreferencesName : sharedPreferencesNames) {
                clearSharedPreferences(context, sharedPreferencesName);
            }
        }

        private static void clearSharedPreferences(Context context, String sharedPreferencesName) {
            SharedPreferences.Editor editor = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE).edit();
            editor.clear().apply();
        }
    }


