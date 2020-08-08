package com.example.compostingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToLogin(View view) {
        Intent goToLogin = new Intent(this, LoginActivity.class);
        startActivity(goToLogin);
    }

    public void goToMap(View view) {
        Intent goToMaps = new Intent(this, MapsActivity.class);
        startActivity(goToMaps);
    }
}