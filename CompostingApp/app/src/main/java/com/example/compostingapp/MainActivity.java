package com.example.compostingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void goToMap(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String userEmail = prefs.getString("UserEmail", null); // get value of last login status

        Intent goToMaps = new Intent(this, MapsActivity.class);
        goToMaps.putExtra("UserEmail", userEmail);
        startActivity(goToMaps);
    }

    public void logout(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("IsClientLoggedIn", false).apply(); // islogin is a boolean value of your login status

        Intent goToLogin = new Intent(this, LoginActivity.class);
        startActivity(goToLogin);
    }

    public void goToInfoPage(View view) {
        Intent goToInfoPage = new Intent(this, InfoPageActivity.class);
        startActivity(goToInfoPage);
    }

    public void goToRegisterClient(View view) {
        Intent goToRegister = new Intent(this, RegisterActivityDonor.class);
        goToRegister.putExtra("UpdateProfile", true);
        startActivity(goToRegister);
    }
}