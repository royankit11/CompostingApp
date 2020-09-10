package com.example.compostingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class BeginningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginning);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean IsClientlogin = prefs.getBoolean("IsClientLoggedIn", false);
        boolean isHostLogin = prefs.getBoolean("IsHostLoggedIn", false);// get value of last login status


        if(IsClientlogin) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent goToMain = new Intent(BeginningActivity.this, MainActivity.class);
                    startActivity(goToMain);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }, 2000);
        } else if (isHostLogin) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent goToMain = new Intent(BeginningActivity.this, MainActivityHost.class);
                    startActivity(goToMain);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }, 2000);
        } else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent goToMain = new Intent(BeginningActivity.this, LoginActivity.class);
                    startActivity(goToMain);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }, 2000);
        }
    }

}