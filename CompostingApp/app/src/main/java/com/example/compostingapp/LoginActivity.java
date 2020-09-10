package com.example.compostingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText txtUsername;
    EditText txtPassword;
    String baseUrlDonor;
    String baseUrlHost;
    String strUsername;
    String strPassword;
    RequestQueue requestQueue;
    RadioGroup radioGroup;
    RadioButton radioButton;
    String recipientEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String urlTemp = getString(R.string.localhostURL);
        baseUrlHost = urlTemp + "getCompostUser/";
        baseUrlDonor = urlTemp + "getClientUser/";

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        requestQueue = Volley.newRequestQueue(this);

        radioGroup = findViewById(R.id.radioGroup);

        Intent receiveMap = getIntent();
    }


    public void login(View view) {
        strUsername = String.valueOf(txtUsername.getText());
        strPassword = String.valueOf(txtPassword.getText());

        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);

        if(!strUsername.equals("")) {
            if(!strPassword.equals("")) {
                if (radioGroup.getCheckedRadioButtonId() != -1) {
                    if(radioButton.getText().equals("I am a donor")) {
                        String full_api_url = baseUrlDonor + strUsername + "/" + strPassword + "/" + false;
                        new myAsyncTaskLogIn().execute(full_api_url);
                    } else {
                        String full_api_url = baseUrlHost + strUsername + "/" + strPassword + "/" + false;
                        new myAsyncTaskLogIn().execute(full_api_url);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Select a radio button", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Provide password", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Provide username", Toast.LENGTH_LONG).show();
        }

    }


    public void goToRegisterCompost(View view) {
        Intent goToRegister = new Intent(this, RegisterActivityCompost.class);
        goToRegister.putExtra("UpdateProfile", false);
        startActivity(goToRegister);

    }

    public void goToRegisterDonor(View view) {
        Intent goToRegister = new Intent(this, RegisterActivityDonor.class);
        goToRegister.putExtra("UpdateProfile", false);
        startActivity(goToRegister);
    }

    public class myAsyncTaskLogIn extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            JsonObjectRequest arrReq = new JsonObjectRequest(Request.Method.GET, url[0], (JSONObject) null, listener(), errorListener());
            requestQueue.add(arrReq);

            String done = "Logging In...";
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

                        //if both the username and password match, then parse the other user information values
                        //and go to bringToHomePage function
                        if (error.equals("")) {
                            if(radioButton.getText().equals("I am a donor")) {
                                String jsonUsername = jsonObject.get("Username").toString();
                                String email = jsonObject.get("Email").toString();
                                String firstName = jsonObject.get("FirstName").toString();
                                String lastName =jsonObject.get("LastName").toString();
                                Integer id = jsonObject.getInt("ID");

                                bringToDonorHomePage(jsonUsername, email, firstName, lastName, id);
                            } else {
                                String jsonUsername = jsonObject.get("Username").toString();
                                String address = jsonObject.get("Address1").toString();
                                String email = jsonObject.get("Email").toString();
                                String tos = jsonObject.get("TypeOfService").toString();
                                String orgName = jsonObject.get("OrgName").toString();
                                Integer id = jsonObject.getInt("ID");

                                bringToHostHomePage(jsonUsername, address, email, tos, orgName, id);
                            }


                        } else {
                            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                        }



                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this, "Unable to connect to database.", Toast.LENGTH_SHORT).show();

                    }

                }
            };
        }

        private Response.ErrorListener errorListener() {
            return new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "Unable to login.", Toast.LENGTH_LONG).show();
                }
            };
        }
        private void bringToDonorHomePage (String jsonUsername, String email, String firstName, String lastName, Integer id) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            prefs.edit().putString("ClientUsername", jsonUsername).apply();
            prefs.edit().putBoolean("IsClientLoggedIn", true).apply();
            prefs.edit().putString("UserEmail", email).apply();
            prefs.edit().putInt("ClientID", id).apply();

            Toast.makeText(LoginActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();

            Intent goToMain = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(goToMain);
        }

        private void bringToHostHomePage (String jsonUsername, String address, String email, String tos, String orgName, Integer id) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            prefs.edit().putString("HostUsername", jsonUsername).apply();
            prefs.edit().putBoolean("IsHostLoggedIn", true).apply();
            prefs.edit().putString("HostEmail", email).apply();
            prefs.edit().putInt("HostID", id).apply();

            Toast.makeText(LoginActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();

            Intent goToMain = new Intent(LoginActivity.this, MainActivityHost.class);
            startActivity(goToMain);
        }
    }
}