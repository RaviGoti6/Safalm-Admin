package com.example.raju.safalmadmin.EmployeeSide;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.raju.safalmadmin.HttpHandler;
import com.example.raju.safalmadmin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class EmployeeEditSelfLeadActivity extends AppCompatActivity {

    private String TAG = EmployeeEditSelfLeadActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";

    EditText editESLname, editESLaddress, editESLmobile, editESLemail, editESLcompanyname, editESLdate, editESLtime;


    String pid, pname, paddress, pmobile, id, name, address, mobile,email,cmpName,date,time;

    int mDate, mMonth, mYear, mMinute, mHour;

    String date_time, dt, tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_edit_self_lead_activity);

        editESLname = findViewById(R.id.editESLname);
        editESLaddress = findViewById(R.id.editESLaddress);
        editESLmobile = findViewById(R.id.editESLmobile);
        editESLemail = findViewById(R.id.editESLemail);
        editESLcompanyname = findViewById(R.id.editESLcompanyname);
        editESLdate = findViewById(R.id.editESLdate);
        editESLtime = findViewById(R.id.editESLtime);

        Button btnESLedit = findViewById(R.id.btnESLedit);

        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            pid = (String) b.get("id");
            pname = (String) b.get("name");
            paddress = (String) b.get("address");
            pmobile = (String) b.get("mobile");
            email=(String) b.get("email");
            cmpName=(String)b.get("cmpName");
            date=(String)b.get("date");
            time=(String)b.get("time");
            //txtSLDname.setText(lead_id);
        }
        //Toast.makeText(EmployeeEditSelfLeadActivity.this, "id="+pid, Toast.LENGTH_SHORT).show();
        editESLname.setText(pname);
        editESLmobile.setText(pmobile);
        editESLaddress.setText(paddress);
        editESLemail.setText(email);
        editESLcompanyname.setText(cmpName);
        editESLdate.setText(date);
        editESLtime.setText(time);

        editESLdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDate = c.get(Calendar.DATE);
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);


                DatePickerDialog datePickerDialog = new DatePickerDialog(EmployeeEditSelfLeadActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //   String mnth, day;
//
                        editESLdate.setText((year + "-" + (((month + 1) < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth)));
//                        timePicker();

                        // editASLTVSdatetime.setText(date_time);
                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.show();
            }
        });
        editESLtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c1 = Calendar.getInstance();
                mHour = c1.get(Calendar.HOUR);
                mMinute = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(EmployeeEditSelfLeadActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // editASLTVSdatetime.setText(date_time + (" "+hourOfDay + ":" + minute));
                        editESLtime.setText((((hourOfDay < 10) ? "0" + hourOfDay : hourOfDay) + ":" + ((minute < 10) ? "0" + minute : minute) + ":00"));
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        btnESLedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                name=editESLname.getText().toString().trim();
//                address=editESLaddress.getText().toString().trim();
//                mobile=editESLmobile.getText().toString().trim();
                dt = editESLdate.getText().toString();
                tm = editESLtime.getText().toString();
                date_time = dt + " " + tm;
                email=editESLemail.getText().toString();

                try {
                    //startActivity(new Intent(AddSelfLeadActivity.this, SelfLeadDetailActivity.class));
                    name = java.net.URLEncoder.encode(editESLname.getText().toString().trim(), "UTF-8");
                    address = java.net.URLEncoder.encode(editESLaddress.getText().toString().trim(), "UTF-8");
                    date_time = java.net.URLEncoder.encode(date_time, "UTF-8");
                    cmpName=java.net.URLEncoder.encode(editESLcompanyname.getText().toString().trim(), "UTF-8");
                    mobile = editESLmobile.getText().toString().trim();
                    // mobile=Integer.valueOf(lmobile);
                } catch (UnsupportedEncodingException e) {

                }

                url = "http://10.0.2.2/safalm_admin/employee_edit_self_lead.php?lid=" + pid + "&lname=" + name + "&laddress=" + address + "&lmobile=" + mobile+"&lemail="+email+"&lcompany_name="+cmpName+"&ldate="+date_time;

                new Employee().execute();
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        try {
//            //startActivity(new Intent(AddSelfLeadActivity.this, SelfLeadDetailActivity.class));
//            name = java.net.URLEncoder.encode(editESLname.getText().toString().trim(), "UTF-8");
//            address = java.net.URLEncoder.encode(editESLaddress.getText().toString().trim(), "UTF-8");
//            mobile = editESLmobile.getText().toString().trim();
//            // mobile=Integer.valueOf(lmobile);
//        } catch (UnsupportedEncodingException e) {
//
//        }
//
//        url = "http://100.93.150.250/safalm/edit_self_lead.php?id=" + pid + "&name=" + name + "&address=" + address + "&mobile=" + mobile;
//
//        new Employee().execute();
//    }


    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeEditSelfLeadActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

            // Intent i = new Intent(EditSelfLeadActivity.this, SelfLeadDetailActivity.class);
            // i.putExtra("lead_id", pid);
            // startActivity(i);
            Toast.makeText(EmployeeEditSelfLeadActivity.this, "Data Updated Successfully.", Toast.LENGTH_SHORT).show();
            finish();
            pDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Log.e("hardik", url);
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Log.e("SAFALM1=", jsonObj.toString());

                    // Getting JSON Array node
                    //JSONArray self_lead = jsonObj.getJSONArray("message");
                    //Log.e("SAFALM2=", self_lead.toString());
                    // JSONArray contacts = new JSONArray(jsonStr);

                    // looping through All Contacts
                    // for (int i = 0; i < self_lead.length(); i++) {
                    //JSONObject c = self_lead.getJSONObject(0);

                    String success = jsonObj.getString("success");
                    String message = jsonObj.getString("message");
                    // Log.e("SAFALM3=", success);
                    //Log.e("SAFALM4=", message);
                    //Toast.makeText(getApplicationContext(), success + message, Toast.LENGTH_SHORT).show();
                    // HashMap<String, String> map = new HashMap<>();

//                        map.put(TAG_SLID, id);
//                        map.put(TAG_SLNAME, name);
//                        map.put(TAG_SLADDRESS, address);
//                        map.put(TAG_SLCONTACT, contact);
//
//                        leadList.add(map);

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }
    }
}
