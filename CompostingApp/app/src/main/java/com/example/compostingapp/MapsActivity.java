package com.example.compostingapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    Double currentLat;
    Double currentLng;
    RequestQueue requestQueue;
    String apiKey;
    String baseUrl;
    Marker compostLocation;
    String userEmail;
    HashMap<String, Integer> emailToID = new HashMap<>();
    String state;
    Map<String, String> states = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationClient = getFusedLocationProviderClient(this);

        apiKey = getString(R.string.google_maps_key);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent receiveLogin = getIntent();

        Bundle email = receiveLogin.getExtras();

        userEmail = email.getString("UserEmail");

        requestQueue = Volley.newRequestQueue(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        states.put("Alabama","AL");
        states.put("Alaska","AK");
        states.put("Alberta","AB");
        states.put("American Samoa","AS");
        states.put("Arizona","AZ");
        states.put("Arkansas","AR");
        states.put("Armed Forces (AE)","AE");
        states.put("Armed Forces Americas","AA");
        states.put("Armed Forces Pacific","AP");
        states.put("British Columbia","BC");
        states.put("California","CA");
        states.put("Colorado","CO");
        states.put("Connecticut","CT");
        states.put("Delaware","DE");
        states.put("District Of Columbia","DC");
        states.put("Florida","FL");
        states.put("Georgia","GA");
        states.put("Guam","GU");
        states.put("Hawaii","HI");
        states.put("Idaho","ID");
        states.put("Illinois","IL");
        states.put("Indiana","IN");
        states.put("Iowa","IA");
        states.put("Kansas","KS");
        states.put("Kentucky","KY");
        states.put("Louisiana","LA");
        states.put("Maine","ME");
        states.put("Manitoba","MB");
        states.put("Maryland","MD");
        states.put("Massachusetts","MA");
        states.put("Michigan","MI");
        states.put("Minnesota","MN");
        states.put("Mississippi","MS");
        states.put("Missouri","MO");
        states.put("Montana","MT");
        states.put("Nebraska","NE");
        states.put("Nevada","NV");
        states.put("New Brunswick","NB");
        states.put("New Hampshire","NH");
        states.put("New Jersey","NJ");
        states.put("New Mexico","NM");
        states.put("New York","NY");
        states.put("Newfoundland","NF");
        states.put("North Carolina","NC");
        states.put("North Dakota","ND");
        states.put("Northwest Territories","NT");
        states.put("Nova Scotia","NS");
        states.put("Nunavut","NU");
        states.put("Ohio","OH");
        states.put("Oklahoma","OK");
        states.put("Ontario","ON");
        states.put("Oregon","OR");
        states.put("Pennsylvania","PA");
        states.put("Prince Edward Island","PE");
        states.put("Puerto Rico","PR");
        states.put("Quebec","QC");
        states.put("Rhode Island","RI");
        states.put("Saskatchewan","SK");
        states.put("South Carolina","SC");
        states.put("South Dakota","SD");
        states.put("Tennessee","TN");
        states.put("Texas","TX");
        states.put("Utah","UT");
        states.put("Vermont","VT");
        states.put("Virgin Islands","VI");
        states.put("Virginia","VA");
        states.put("Washington","WA");
        states.put("West Virginia","WV");
        states.put("Wisconsin","WI");
        states.put("Wyoming","WY");
        states.put("Yukon Territory","YT");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);

                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                String stateFull = addresses.get(0).getAdminArea();

                                state = states.get(stateFull);

                                String urlTemp = getString(R.string.localhostURL);
                                baseUrl = urlTemp + "getCompostLocations/" + state;

                                new myAsyncTaskGetLocations().execute(baseUrl);

                                mMap.setOnMarkerClickListener(MapsActivity.this);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });

    }

    public void onLocationChanged(Location location) {

        BitmapDrawable bitmapdraw=(BitmapDrawable)getDrawable(R.drawable.youarehere);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 194, 194, false);

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).title("You are here"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10.0f));

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        PlacesClient placesClient = Places.createClient(this);

    }

    public LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        if(!marker.getTitle().equals("You are here")) {
            new AlertDialog.Builder(this)
                    .setTitle(marker.getTitle())
                    .setMessage(marker.getSnippet())

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("Send a message", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Integer recipientID = emailToID.get(marker.getTag());

                            Intent goToEmail = new Intent(MapsActivity.this, EmailActivity.class);
                            goToEmail.putExtra("RecipientEmail", marker.getTag().toString());
                            goToEmail.putExtra("UserEmail", userEmail);
                            goToEmail.putExtra("RecipientID", recipientID);
                            startActivity(goToEmail);
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("Back", null)
                    .show();
        }
        return false;
    }

    public class myAsyncTaskGetLocations extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            JsonArrayRequest arrReq = new JsonArrayRequest(url[0], listener(), errorListener());
            requestQueue.add(arrReq);

            String done = "";
            return done;
        }

        protected void onPostExecute(String done) {
        }

        private Response.Listener<JSONArray> listener() {
            return new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject record = response.getJSONObject(i);

                            String error = record.getString("Error");

                            if (error.equals("")) {

                                Double lat = record.getDouble("Latitude");
                                Double lng = record.getDouble("Longitude");
                                String orgName = record.getString("OrgName");
                                String address1 = record.getString("Address1");
                                String town = record.getString("Town");
                                String state = record.getString("State");
                                String email = record.getString("Email");
                                String tos = record.getString("TypeOfService");
                                Integer id = record.getInt("ID");

                                String fullAddress = address1 + ", " + town + ", " + state;

                                LatLng latLng = new LatLng(lat, lng);


                                compostLocation = mMap.addMarker(new MarkerOptions().position(latLng).title(orgName)
                                        .snippet(fullAddress + "\nService Provided:" + tos));
                                compostLocation.setTag(email);

                                emailToID.put(email, id);




                            } else {
                                Toast.makeText(MapsActivity.this, error, Toast.LENGTH_LONG).show();
                                return;
                            }

                        }


                    } catch (JSONException e) {
                        Toast.makeText(MapsActivity.this, "Unable to connect to database.", Toast.LENGTH_LONG).show();

                    }

                }
            };
        }

        private Response.ErrorListener errorListener() {
            return new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MapsActivity.this, "Unable to get data.", Toast.LENGTH_LONG).show();
                }
            };
        }
    }

}