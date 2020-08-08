package com.example.compostingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterActivityCompost extends AppCompatActivity {

    String baseUrl;
    String baseUrlUpdateProfile;
    RequestQueue requestQueue;
    EditText txtUsernameReg;
    EditText txtPasswordReg;
    EditText txtPasswordConfirm;
    EditText txtOrgName;
    EditText txtEmail;
    EditText txtAddress;
    EditText txtAddress2;
    EditText txtCity;
    Spinner typeOfService;
    Spinner state;
    Spinner country;
    String username;
    String baseUrlGetAwards;
    Boolean bringValues;
    ArrayAdapter<String> adapter;
    Integer userID;
    String urlTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_compost);

        urlTemp = getString(R.string.localhostURL);
        baseUrl = urlTemp + "RegisterComposter/";


        //receive intent and parse values
        Intent receiveRegister = getIntent();


        txtUsernameReg = findViewById(R.id.txtUsernameReg);
        txtPasswordReg = findViewById(R.id.txtPasswordReg);
        txtPasswordConfirm = findViewById(R.id.txtPasswordConfirm);
        txtOrgName = findViewById(R.id.txtOrgName);
        txtEmail = findViewById(R.id.txtEmail);
        txtAddress = findViewById(R.id.txtAddress);
        txtAddress2 = findViewById(R.id.txtAddress2);
        txtCity = findViewById(R.id.txtCity);
        state = findViewById(R.id.state);
        country = findViewById(R.id.country);
        typeOfService = findViewById(R.id.typeOfService);


        requestQueue = Volley.newRequestQueue(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList();
        categories.add("Please Select");
        categories.add("Food");
        categories.add("Manure");
        categories.add("Plants");
        categories.add("Other");

        adapter = new ArrayAdapter<>(RegisterActivityCompost.this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeOfService.setAdapter(adapter);

        List<String> categories2 = new ArrayList();
        categories2.add("State");
        categories2.add("AL");
        categories2.add("AK");
        categories2.add("AZ");
        categories2.add("AR");
        categories2.add("CA");
        categories2.add("CO");
        categories2.add("CT");
        categories2.add("DE");
        categories2.add("FL");
        categories2.add("GA");
        categories2.add("HI");
        categories2.add("ID");
        categories2.add("IL");
        categories2.add("IN");
        categories2.add("IA");
        categories2.add("CT");
        categories2.add("KS");
        categories2.add("KY");
        categories2.add("LA");
        categories2.add("ME");
        categories2.add("MD");
        categories2.add("MA");
        categories2.add("MI");
        categories2.add("MN");
        categories2.add("MS");
        categories2.add("MO");
        categories2.add("MT");
        categories2.add("NE");
        categories2.add("NV");
        categories2.add("NH");
        categories2.add("NJ");
        categories2.add("NM");
        categories2.add("NY");
        categories2.add("NC");
        categories2.add("ND");
        categories2.add("OH");
        categories2.add("MA");
        categories2.add("OK");
        categories2.add("OR");
        categories2.add("PA");
        categories2.add("RI");
        categories2.add("SC");
        categories2.add("SD");
        categories2.add("TN");
        categories2.add("TX");
        categories2.add("UT");
        categories2.add("VT");
        categories2.add("VA");
        categories2.add("WA");
        categories2.add("WV");
        categories2.add("WI");
        categories2.add("WY");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(RegisterActivityCompost.this,
                android.R.layout.simple_spinner_item, categories2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        state.setAdapter(adapter2);

        List<String> categories3 = new ArrayList();
        categories3.add("Country");
        categories3.add("USA");
        categories3.add("CAN");
        categories3.add("MEX");

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(RegisterActivityCompost.this,
                android.R.layout.simple_spinner_item, categories3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        country.setAdapter(adapter3);

    }

    public void registerNewUser(View view) {

        String strRegisterUsername = String.valueOf(txtUsernameReg.getText());
        String strRegisterPassword = String.valueOf(txtPasswordReg.getText());
        String strPasswordConfirm = String.valueOf(txtPasswordConfirm.getText());
        String strOrgName = String.valueOf(txtOrgName.getText());
        String strEmail = String.valueOf(txtEmail.getText());
        String strAddress = String.valueOf(txtAddress.getText());
        String strAddress2 = String.valueOf(txtAddress2.getText());
        String strCity = String.valueOf(txtCity.getText());
        String strState = state.getSelectedItem().toString();
        String strCountry = country.getSelectedItem().toString();
        String strTOS = typeOfService.getSelectedItem().toString();

        if(!strRegisterUsername.equals("")) {
            if(!strRegisterPassword.equals("")) {
                if(!strPasswordConfirm.equals("")) {
                    if(!strOrgName.equals("")) {
                        if(!strEmail.equals("")) {
                            if(!strAddress.equals("")) {
                                if(!strTOS.equals("Please Select")) {
                                    if(!strCity.equals("")) {
                                        if(!strState.equals("State")) {
                                            if(!strCountry.equals("Country")) {
                                                if(strRegisterPassword.equals(strPasswordConfirm)) {
                                                    if(strAddress2.equals("")) {
                                                        List<String> latLng = getLocationFromAddress(this, strAddress +
                                                                " " + strAddress2 + ", " + strCity + ", " + strState + ", "+ strCountry);
                                                        if(latLng.get(0).equals("Invalid Address")) {
                                                            return;
                                                        }
                                                        String strLatitude = latLng.get(0);
                                                        String strLongitude = latLng.get(1);

                                                        String final_url = baseUrl + strRegisterUsername + "/" + strRegisterPassword +
                                                                "/" + strAddress + "/null/" + strCity + "/" + strState + "/" + strCountry +
                                                                "/" + strLatitude + "/" + strLongitude +"/" +
                                                                strEmail + "/" + strTOS + "/" + strOrgName;

                                                        new myAsyncTaskRegister().execute(final_url);
                                                    } else {
                                                        List<String> latLng = getLocationFromAddress(this, strAddress +
                                                                ", " + strCity + ", " + strState + ", "+ strCountry);
                                                        String strLatitude = latLng.get(0);
                                                        String strLongitude = latLng.get(1);

                                                        String final_url = baseUrl + strRegisterUsername + "/" + strRegisterPassword +
                                                                "/" + strAddress + "/" + strAddress2  + "/" + strCity + "/" + strState + "/" + strCountry +
                                                                "/" + strLatitude + "/" + strLongitude +"/" +
                                                                strEmail + "/" + strTOS + "/" + strOrgName;

                                                        new myAsyncTaskRegister().execute(final_url);
                                                    }
                                                } else {
                                                    Toast.makeText(RegisterActivityCompost.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(RegisterActivityCompost.this, "Please enter a country", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(RegisterActivityCompost.this, "Please enter a state", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(RegisterActivityCompost.this, "Please enter a city", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(RegisterActivityCompost.this, "Please select a service type", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(RegisterActivityCompost.this, "Please enter an address", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivityCompost.this, "Please enter an email", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivityCompost.this, "Please enter an organization name", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterActivityCompost.this, "Please confirm your password", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(RegisterActivityCompost.this, "Please enter a password", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(RegisterActivityCompost.this, "Please enter a username", Toast.LENGTH_LONG).show();
        }
    }

    public List<String> getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        List<String> p1 = new ArrayList<>();

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1.add(String.valueOf(location.getLatitude()));
            p1.add(String.valueOf(location.getLongitude()));

        } catch (IOException ex) {
            Toast.makeText(RegisterActivityCompost.this, "Invalid Address", Toast.LENGTH_LONG).show();
            p1.add("Invalid Address");
        }

        return p1;
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
                            Toast.makeText(RegisterActivityCompost.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivityCompost.this, "Username already exists", Toast.LENGTH_LONG).show();
                        }


                    } catch (JSONException e) {
                        Toast.makeText(RegisterActivityCompost.this, "Unable to connect to database.", Toast.LENGTH_LONG).show();

                    }

                }
            };
        }

        private Response.ErrorListener errorListener() {
            return new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivityCompost.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            };
        }
    }
}