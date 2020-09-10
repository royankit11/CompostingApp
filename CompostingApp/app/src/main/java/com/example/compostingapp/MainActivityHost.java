package com.example.compostingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivityHost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_host);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void logout(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("IsHostLoggedIn", false).apply(); // islogin is a boolean value of your login status

        Intent goToLogin = new Intent(this, LoginActivity.class);
        startActivity(goToLogin);
    }

    public void goToInbox(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Integer hostID = prefs.getInt("HostID", 0); // islogin is a boolean value of your login status

        Intent goToInbox = new Intent(this, ViewRequestsActivity.class);
        goToInbox.putExtra("HostID", hostID);
        startActivity(goToInbox);

    }

    public void goToRegisterCompost(View view) {
        Intent goToRegister = new Intent(this, RegisterActivityCompost.class);
        goToRegister.putExtra("UpdateProfile", true);
        startActivity(goToRegister);
    }

    public void goToInfoPage(View view) {
        Intent goToInfoPage = new Intent(this, InfoPageActivity.class);
        startActivity(goToInfoPage);
    }
}