package com.example.compostingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class
ViewRequestsActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
    int padding = 85;
    Integer intHostID;
    TableLayout tv;
    Integer textViewSize = 20;
    String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        String urlTemp = getString(R.string.localhostURL);
        baseUrl = urlTemp + "getRecentEmails/";

        Bundle hostID = getIntent().getExtras();
        intHostID = hostID.getInt("HostID");

        requestQueue = Volley.newRequestQueue(this);
        tv = findViewById(R.id.tableLayout);
        tv.removeAllViewsInLayout();

        tv.removeAllViews();

        TableRow tr = new TableRow(ViewRequestsActivity.this);

        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        

        TextView b1 = new TextView(ViewRequestsActivity.this);
        b1.setPadding(padding, 0, 0, 0);
        b1.setText("Sender");
        b1.setTextColor(Color.BLACK);
        b1.setBackgroundColor(Color.rgb(135, 206, 250));
        b1.setTextSize(textViewSize);
        b1.setTypeface(boldTypeface);
        tr.addView(b1);

        TextView b2 = new TextView(ViewRequestsActivity.this);
        b2.setPadding(padding, 0, 0, 0);
        b2.setText("Subject");
        b2.setTextColor(Color.BLACK);
        b2.setBackgroundColor(Color.rgb(135, 206, 250));
        b2.setTextSize(textViewSize);
        b2.setTypeface(boldTypeface);
        tr.addView(b2);

        TextView b3 = new TextView(ViewRequestsActivity.this);
        b3.setPadding(padding, 0, 0, 0);
        b3.setText("Date");
        b3.setTextColor(Color.BLACK);
        b3.setBackgroundColor(Color.rgb(135, 206, 250));
        b3.setTextSize(textViewSize);
        b3.setTypeface(boldTypeface);
        tr.addView(b3);

        tv.addView(tr);

        final View vline = new View(ViewRequestsActivity.this);
        vline.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 4));
        vline.setBackgroundColor(Color.BLUE);
        tv.addView(vline); // add line below heading


        String finalUrl = baseUrl + intHostID.toString();

        new myAsyncTaskGetEmails().execute(finalUrl);

    }

    public void backToMain(View view) {
        finish();
    }

    public class myAsyncTaskGetEmails extends AsyncTask<String, Void, String> {

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
                                String senderEmail = record.getString("SenderEmail");
                                String subject = record.getString("Subject");
                                String datetime = record.getString("CreateDateTime");

                                

                                makeNewRow(senderEmail, subject, datetime);
                            } else {
                                Toast.makeText(ViewRequestsActivity.this, error, Toast.LENGTH_LONG).show();
                                return;
                            }

                        }

                    } catch (JSONException e) {
                        Toast.makeText(ViewRequestsActivity.this, "Unable to connect to database.", Toast.LENGTH_LONG).show();

                    }

                }
            };
        }

        private Response.ErrorListener errorListener() {
            return new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ViewRequestsActivity.this, "Unable to get data.", Toast.LENGTH_LONG).show();
                }
            };
        }

        public void makeNewRow(String sender, String subject, String datetime) {
            TableRow tr = new TableRow(ViewRequestsActivity.this);

            tr.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));


            TextView txtSender = new TextView(ViewRequestsActivity.this);
            txtSender.setPadding(padding, 0, 0, 0);
            txtSender.setText(sender);
            txtSender.setGravity(Gravity.CENTER);
            txtSender.setTextColor(Color.RED);
            txtSender.setBackgroundColor(Color.WHITE);
            txtSender.setTextSize(textViewSize);
            tr.addView(txtSender);

            TextView txtSubject = new TextView(ViewRequestsActivity.this);
            txtSubject.setPadding(padding, 0, 0, 0);
            txtSubject.setText(subject);
            txtSubject.setTextColor(Color.BLUE);
            txtSubject.setBackgroundColor(Color.WHITE);
            txtSubject.setTextSize(textViewSize);
            tr.addView(txtSubject);

            TextView txtDatetime = new TextView(ViewRequestsActivity.this);
            txtDatetime.setPadding(padding, 0, 50, 0);
            txtDatetime.setText(datetime);
            txtDatetime.setTextColor(Color.RED);
            txtDatetime.setBackgroundColor(Color.WHITE);
            txtDatetime.setTextSize(textViewSize);
            tr.addView(txtDatetime);




            //add table row to table layout
            if (tr != null) {
                tv.addView(tr);
            }


            final View vline1 = new View(ViewRequestsActivity.this);
            vline1.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 3));
            vline1.setBackgroundColor(Color.BLACK);
            tv.addView(vline1);  // add line below each row

        }
    }
}