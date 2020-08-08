package com.example.compostingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
    Integer userID;
    String urlTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_donor);

        urlTemp = getString(R.string.localhostURL);
        baseUrl = urlTemp + "RegisterClient/";


        //receive intent and parse values
        Intent receiveRegister = getIntent();


        txtUsernameReg = findViewById(R.id.txtUsernameReg);
        txtPasswordReg = findViewById(R.id.txtPasswordReg);
        txtPasswordConfirm = findViewById(R.id.txtPasswordConfirm);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtEmail);


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
                                    String final_url = baseUrl + strRegisterUsername + "/" + strRegisterPassword + "/" +
                                            strEmail + "/" + strFirstName + "/" +
                                            strLastName;

                                    new RegisterActivityDonor.myAsyncTaskRegister().execute(final_url);

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

        private Response.Listener<JSONObject> listener(){
            return new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String result = response.getString("Message");

                        if(result.equals("SUCCESS")) {
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
            return new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivityDonor.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            };
        }
    }
}