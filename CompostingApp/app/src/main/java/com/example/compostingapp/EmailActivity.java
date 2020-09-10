package com.example.compostingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.PasswordAuthentication;
import java.util.Calendar;
import java.util.Properties;

public class EmailActivity extends AppCompatActivity {

    EditText recipientEmail;
    EditText subject;
    EditText message;
    String strRecipient;
    String userEmail;
    String userPassword;
    String urlTemp;
    RequestQueue requestQueue;
    Integer recipientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        Bundle emails = getIntent().getExtras();

        userEmail = emails.getString("UserEmail");
        strRecipient = emails.getString("RecipientEmail");
        recipientID = emails.getInt("RecipientID");

        recipientEmail = findViewById(R.id.recipientEmail);
        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);

        recipientEmail.setText(strRecipient);

        urlTemp = getString(R.string.localhostURL);
        requestQueue = Volley.newRequestQueue(this);

    }



    public void sendEmail(View view) {

        SendMail sm = new SendMail(EmailActivity.this, "subhraroy26@gmail.com", String.valueOf(subject.getText()),
                String.valueOf(message.getText()), userEmail);
        sm.execute();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Integer senderID = prefs.getInt("ClientID", 0);

        String full_api_url = urlTemp + "logEmail/" + senderID.toString() + "/" +
                recipientID.toString() + "/" + subject.getText();
        new myAsyncTaskLogEmail().execute(full_api_url);

        finish();
    }

    public void backToMap(View view) {
        finish();
    }


    public class myAsyncTaskLogEmail extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            JsonObjectRequest arrReq = new JsonObjectRequest(Request.Method.GET, url[0], (JSONObject) null, listener(), errorListener());
            requestQueue.add(arrReq);

            return "";
        }

        protected void onPostExecute(String done) {
        }

        private Response.Listener<JSONObject> listener() {
            return new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(EmailActivity.this, "Success", Toast.LENGTH_LONG).show();
                }
            };
        }

        private Response.ErrorListener errorListener() {
            return new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(EmailActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            };
        }
    }
}