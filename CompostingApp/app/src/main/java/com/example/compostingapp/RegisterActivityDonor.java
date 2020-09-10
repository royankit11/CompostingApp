package com.example.compostingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterActivityDonor extends AppCompatActivity {

    String baseUrl;
    String baseUrlUpdateProfile;
    RequestQueue requestQueue;
    EditText txtUsernameReg;
    EditText txtPasswordReg;
    EditText txtPasswordConfirm;
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtEmail;
    String username;
    String urlTemp;
    String id;
    boolean updateProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_donor);

        urlTemp = getString(R.string.localhostURL);
        baseUrl = urlTemp + "RegisterClient/";
        baseUrlUpdateProfile = urlTemp + "updateProfileClient/";

        //receive intent and parse values
        Intent receiveRegister = getIntent();

        Bundle bndlUpdateProfile = receiveRegister.getExtras();

        updateProfile = bndlUpdateProfile.getBoolean("UpdateProfile");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        username = prefs.getString("ClientUsername", null);
        id = String.valueOf(prefs.getInt("ClientID", 0));


        txtUsernameReg = findViewById(R.id.txtUsernameReg);
        txtPasswordReg = findViewById(R.id.txtPasswordReg);
        txtPasswordConfirm = findViewById(R.id.txtPasswordConfirm);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtEmail);

        if(updateProfile) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String final_url = urlTemp + "getClientUser/" + username + "/NA/" + true;
                    new myAsyncTaskGetUserInfo().execute(final_url);
                }
            }, 200);
        }


        requestQueue = Volley.newRequestQueue(this);


    }

    public void registerNewUser(View view) {

        String strRegisterUsername = String.valueOf(txtUsernameReg.getText());
        String strRegisterPassword = String.valueOf(txtPasswordReg.getText());
        String strPasswordConfirm = String.valueOf(txtPasswordConfirm.getText());
        String strFirstName = String.valueOf(txtFirstName.getText());
        String strLastName = String.valueOf(txtLastName.getText());
        String strEmail = String.valueOf(txtEmail.getText());

        if(!strRegisterUsername.equals("")) {
            if(!strRegisterPassword.equals("")) {
                if(!strPasswordConfirm.equals("")) {
                    if(!strFirstName.equals("")) {
                        if(!strEmail.equals("")) {
                            if(!strLastName.equals("")) {
                                if(strRegisterPassword.equals(strPasswordConfirm)) {
                                    if(updateProfile) {
                                        String final_url = baseUrlUpdateProfile + strRegisterUsername + "/" + strRegisterPassword + "/" +
                                                strEmail + "/" + strFirstName + "/" + strLastName + "/" + id;

                                        new myAsyncTaskUpdateProfile().execute(final_url);

                                    } else {
                                        String final_url = baseUrl + strRegisterUsername + "/" + strRegisterPassword + "/" +
                                                strEmail + "/" + strFirstName + "/" +
                                                strLastName;

                                        new myAsyncTaskRegister().execute(final_url);
                                    }
                                } else {
                                    Toast.makeText(RegisterActivityDonor.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(RegisterActivityDonor.this, "Please enter your last name", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivityDonor.this, "Please enter an email", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivityDonor.this, "Please enter your first name", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterActivityDonor.this, "Please confirm your password", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(RegisterActivityDonor.this, "Please enter a password", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(RegisterActivityDonor.this, "Please enter a username", Toast.LENGTH_LONG).show();
        }
    }

    public void backToLogin(View view) { finish(); }

    public class myAsyncTaskRegister extends AsyncTask<String, Void, String> {
        //this calls API to register user
        @Override
        protected String doInBackground(String... url) {
            JsonObjectRequest arrReq = new JsonObjectRequest(Request.Method.GET, url[0], (JSONObject) null, listener(), errorListener());

            requestQueue.add(arrReq);

            String done = "";
            return done;
        }

        protected void onPostExecute(String done) {

        }

        private Response.Listener<JSONObject> listener() {
            return new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String result = response.getString("Message");

                        if (result.equals("SUCCESS")) {
                            finish();
                            Toast.makeText(RegisterActivityDonor.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivityDonor.this, "Username already exists", Toast.LENGTH_LONG).show();
                        }


                    } catch (JSONException e) {
                        Toast.makeText(RegisterActivityDonor.this, "Unable to connect to database.", Toast.LENGTH_LONG).show();

                    }

                }
            };
        }

        private Response.ErrorListener errorListener() {
            return new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivityDonor.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            };
        }
    }

    public class myAsyncTaskUpdateProfile extends AsyncTask<String, Void, String> {
        //this api is ONLY for updating the user
        @Override
        protected String doInBackground(String... url) {
            JsonObjectRequest arrReq = new JsonObjectRequest(Request.Method.GET, url[0], (JSONObject) null, listener(), errorListener());
            requestQueue.add(arrReq);

            String done = "";
            return done;
        }
        protected void onPostExecute(String done) {

        }

        private Response.Listener<JSONObject> listener(){
            return new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String result = response.getString("Message");

                        if(result.equals("SUCCESS")) {
                            finish();
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RegisterActivityDonor.this);
                            prefs.edit().putString("ClientUsername", response.getString("Username")).apply();
                            Toast.makeText(RegisterActivityDonor.this, "Profile has been updated successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivityDonor.this, "Username already exists", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(RegisterActivityDonor.this, "Unable to connect to database.", Toast.LENGTH_LONG).show();

                    }

                }
            };
        }

        private Response.ErrorListener errorListener() {
            return new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivityDonor.this, "Unable to register.", Toast.LENGTH_LONG).show();
                }
            };
        }
    }

    public class myAsyncTaskGetUserInfo extends AsyncTask<String, Void, String> {

        //getting previous values if necessary
        @Override
        protected String doInBackground(String... url) {
            JsonObjectRequest arrReq = new JsonObjectRequest(Request.Method.GET, url[0], (JSONObject) null, listener(), errorListener());
            requestQueue.add(arrReq);

            String done = "";
            return done;
        }

        protected void onPostExecute(String done){
        }

        private Response.Listener<JSONObject> listener(){
            return new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonObject = response;
                        String error = jsonObject.get("Error").toString();

                        if (error.equals("")) {
                            String jsonUsername = jsonObject.get("Username").toString();
                            String password = jsonObject.get("Password").toString();
                            String email = jsonObject.get("Email").toString();
                            String firstName = jsonObject.get("FirstName").toString();
                            String lastName = jsonObject.get("LastName").toString();


                            setEditTexts(jsonUsername, password, email, firstName, lastName);
                        } else {
                            Toast.makeText(RegisterActivityDonor.this, error, Toast.LENGTH_LONG).show();
                        }



                    } catch (Exception e) {
                        Toast.makeText(RegisterActivityDonor.this, "Unable to connect to database.", Toast.LENGTH_LONG).show();

                    }

                }
            };
        }

        private Response.ErrorListener errorListener() {
            return new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivityDonor.this, "Unable to get data.", Toast.LENGTH_LONG).show();
                }
            };
        }
        private void setEditTexts (String jsonUsername, String password, String email, String firstName, String lastName) {

            txtUsernameReg.setText(jsonUsername);
            txtPasswordReg.setText(password);
            txtPasswordConfirm.setText(password);
            txtEmail.setText(email);
            txtFirstName.setText(firstName);
            txtLastName.setText(lastName);
        }
    }
}