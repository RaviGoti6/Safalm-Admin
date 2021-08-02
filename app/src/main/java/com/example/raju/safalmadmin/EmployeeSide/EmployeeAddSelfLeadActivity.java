package com.example.raju.safalmadmin.EmployeeSide;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import com.example.raju.safalmadmin.SafalmAdmin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;


public class EmployeeAddSelfLeadActivity extends AppCompatActivity {

    EditText editSLname;
    EditText editSLaddress;
    EditText editSLmobile;
    EditText editSLemail;
    EditText editSLcompanyname;
    EditText editSLdate;
    EditText editSLtime;


    String lname, laddress, lmobile,lemail,lcompname, id;
   // int mobile;
    SafalmAdmin se;
    private String TAG = EmployeeAddSelfLeadActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;

    int mDate, mMonth, mYear, mMinute, mHour;
    String date_time, dt, tm;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_add_self_lead_activity);

        editSLname = findViewById(R.id.editSLname);
        editSLaddress = findViewById(R.id.editSLaddress);
        editSLmobile = findViewById(R.id.editSLmobile);
        editSLemail = findViewById(R.id.editSLemail);
        editSLcompanyname = findViewById(R.id.editSLcompanyname);
        editSLdate = findViewById(R.id.editSLdate);
        editSLtime = findViewById(R.id.editSLtime);

        se = (SafalmAdmin) getApplicationContext();
        id = se.getempId().toString();

        Button btnSLadd = findViewById(R.id.btnSLadd);

        editSLdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDate = c.get(Calendar.DATE);
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);


                DatePickerDialog datePickerDialog = new DatePickerDialog(EmployeeAddSelfLeadActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                     //   String mnth, day;
//
                        editSLdate.setText((year + "-" + (((month + 1) < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth)));
//                        timePicker();

                        // editASLTVSdatetime.setText(date_time);
                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.show();

            }
        });
        editSLtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c1 = Calendar.getInstance();
                mHour = c1.get(Calendar.HOUR);
                mMinute = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(EmployeeAddSelfLeadActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // editASLTVSdatetime.setText(date_time + (" "+hourOfDay + ":" + minute));
                        editSLtime.setText((((hourOfDay < 10) ? "0" + hourOfDay : hourOfDay) + ":" + ((minute < 10) ? "0" + minute : minute) + ":00"));
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        btnSLadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dt = editSLdate.getText().toString();
                tm = editSLtime.getText().toString();
                date_time = dt + " " + tm;

                try {
                    //startActivity(new Intent(AddSelfLeadActivity.this, SelfLeadDetailActivity.class));
                    date_time = java.net.URLEncoder.encode(date_time, "UTF-8");
                    lname = java.net.URLEncoder.encode(editSLname.getText().toString().trim(), "UTF-8");
                    laddress = java.net.URLEncoder.encode(editSLaddress.getText().toString().trim(), "UTF-8");
                    lmobile = editSLmobile.getText().toString().trim();
                    lemail=editSLemail.getText().toString().trim();
                    lcompname=java.net.URLEncoder.encode(editSLcompanyname.getText().toString().trim(), "UTF-8");
                   // mobile=Integer.valueOf(lmobile);
                } catch (UnsupportedEncodingException e) {

                }

                if (!lname.equals("") && !laddress.equals("") && !lmobile.equals("") && !lemail.equals("") && !lcompname.equals("")) {

                    if (lmobile.length() == 10 ) {

                        if (lemail.matches(emailPattern))
                        {
                            url = "http://10.0.2.2/safalm_admin/employee_add_self_lead.php?lname=" + lname + "&laddress=" + laddress + "&lmobile=" + lmobile + "&emp_id=" + id + "&lemail=" + lemail + "&lcompany_name=" + lcompname + "&ldate=" + date_time;

                        new EmployeeAddSelfLeadActivity.Employee().execute();
                        }else {
                            Toast.makeText(EmployeeAddSelfLeadActivity.this, "Enter Valid Email Address...", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(EmployeeAddSelfLeadActivity.this, "Enter valid Mobile Number..", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(EmployeeAddSelfLeadActivity.this, "All field are required.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeAddSelfLeadActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            //Log.e("SAFALM5=", contacts.get("success").toString() + contacts.get("message").toString());
            // Toast.makeText(getApplicationContext(), contacts.get("success").toString() + contacts.get("message").toString(), Toast.LENGTH_SHORT).show();
            //String empId=contacts.get("message").toString();
            //se.setempId(contacts.get("message").toString());
            //   String eid=se.getempId().toString();
            //    Log.e("Safalam 6:",eid);


            if (contacts.get("success").toString().equals("1")) {
//                Intent i = new Intent(AddSelfLeadActivity.this, MainActivity.class);
                //startActivity(i);
//                finish();
                Toast.makeText(EmployeeAddSelfLeadActivity.this, "Lead added successfully...", Toast.LENGTH_SHORT).show();
                editSLemail.setText("");
                editSLname.setText("");
                editSLaddress.setText("");
                editSLmobile.setText("");
                editSLcompanyname.setText("");
                editSLdate.setText("");
                editSLtime.setText("");

            }

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
//                    JSONArray contacts = jsonObj.getJSONArray();
//                    Log.e("SAFALM2=", contacts.toString());
                    //JSONArray contacts = new JSONArray(jsonStr);

                    // looping through All Contacts
                    //for (int i = 0; i < contacts.length(); i++) {
//                        JSONObject c = contacts.getJSONObject(i);

                    String success = jsonObj.getString("success");
                    String message = jsonObj.getString("message");
                    Log.e("SAFALM3=", success);
                    Log.e("SAFALM4=", message);
                    //Toast.makeText(getApplicationContext(), success + message, Toast.LENGTH_SHORT).show();
//                        String email = c.getString("email");
//                        String address = c.getString("address");
//                        String gender = c.getString("gender");

                    // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("phone");
//                        String mobile = phone.getString("mobile");
//                        String home = phone.getString("home");
//                        String office = phone.getString("office");

                    // tmp hash map for single contact
                    contacts = new HashMap<>();

                    // adding each child node to HashMap key => value
                    contacts.put("success", success);
                    contacts.put("message", message);
//                        contact.put("email", email);
//                        contact.put("mobile", mobile);

                    // adding contact to contact list
                    //contactList.add(contact);
                    // }
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
