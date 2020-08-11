package com.example.compostingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
    }


    public void login(View view) {
        strUsername = String.valueOf(txtUsername.getText());
        strPassword = String.valueOf(txtPassword.getText());

        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);

        if(!strUsername.equals("")) {
            if(!strPassword.equals("")) {
                if(radioButton.getText().equals("I am a donor")) {
                    String full_api_url = baseUrlDonor + strUsername + "/" + strPassword;
                    new myAsyncTaskLogIn().execute(full_api_url);
                } else {
                    String full_api_url = baseUrlHost + strUsername + "/" + strPassword;
                    new myAsyncTaskLogIn().execute(full_api_url);
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
        startActivity(goToRegister);

    }

    public void goToRegisterDonor(View view) {
        Intent goToRegister = new Intent(this, RegisterActivityDonor.class);
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
            Toast.makeText(LoginActivity.this, done, Toast.LENGTH_LONG).show();

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

                                bringToDonorHomePage(jsonUsername, email, firstName, lastName);
                            } else {
                                String jsonUsername = jsonObject.get("Username").toString();
                                String address = jsonObject.get("Address1").toString();
                                String email = jsonObject.get("Email").toString();
                                String tos = jsonObject.get("TypeOfService").toString();
                                String orgName = jsonObject.get("OrgName").toString();

                                bringToHostHomePage(jsonUsername, address, email, tos, orgName);
                            }


                        } else {
                            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                        }



                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this, "Unable to connect to database.", Toast.LENGTH_LONG).show();

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
        private void bringToDonorHomePage (String jsonUsername, String email, String firstName, String lastName) {
            Toast.makeText(LoginActivity.this, "Successfully logged in!", Toast.LENGTH_LONG).show();

            Intent goToMaps = new Intent(LoginActivity.this, EmailActivity.class);
            startActivity(goToMaps);
        }

        private void bringToHostHomePage (String jsonUsername, String address, String email, String tos, String orgName) {
            Toast.makeText(LoginActivity.this, "Successfully logged in!", Toast.LENGTH_LONG).show();

            Intent goToMaps = new Intent(LoginActivity.this, MapsActivity.class);
            startActivity(goToMaps);
        }
    }
}