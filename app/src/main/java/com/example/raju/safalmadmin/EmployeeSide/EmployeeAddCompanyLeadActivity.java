package com.example.raju.safalmadmin.EmployeeSide;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

public class EmployeeAddCompanyLeadActivity extends AppCompatActivity {

    EditText editECLid,editECLname,editECLaddress,editECLmobile,editECLemail,editECLcompanyname,editECLdate,editECLtime;

    Button btnECLadd;

    String lid,lname, laddress, lmobile,lemail,lcompname, id,emp_id;
    // int mobile;
    SafalmAdmin sa;
    private String TAG = EmployeeAddCompanyLeadActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;

    int mDate, mMonth, mYear, mMinute, mHour;
    String date_time, dt, tm;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_add_company_lead_activity);

       // editECLid=findViewById(R.id.editECLid);
        editECLname=findViewById(R.id.editECLname);
        editECLaddress=findViewById(R.id.editECLaddress);
        editECLmobile=findViewById(R.id.editECLmobile);
        editECLemail=findViewById(R.id.editECLemail);
        editECLcompanyname=findViewById(R.id.editECLcompanyname);
        editECLdate=findViewById(R.id.editECLdate);
        editECLtime=findViewById(R.id.editECLtime);

        btnECLadd=findViewById(R.id.btnECLadd);

        sa=(SafalmAdmin) getApplicationContext();
        emp_id=sa.getempId();
        editECLdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDate = c.get(Calendar.DATE);
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EmployeeAddCompanyLeadActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        editECLdate.setText((year + "-" + (((month + 1) < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth)));

                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.show();
            }
        });
        editECLtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c1 = Calendar.getInstance();
                mHour = c1.get(Calendar.HOUR);
                mMinute = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(EmployeeAddCompanyLeadActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        editECLtime.setText((((hourOfDay < 10) ? "0" + hourOfDay : hourOfDay) + ":" + ((minute < 10) ? "0" + minute : minute) + ":00"));

                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        btnECLadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dt = editECLdate.getText().toString();
                tm = editECLtime.getText().toString();
                date_time = dt + " " + tm;

                try {
                    //startActivity(new Intent(AddSelfLeadActivity.this, SelfLeadDetailActivity.class));
                 //   lid = editECLid.getText().toString().trim();
                    date_time = java.net.URLEncoder.encode(date_time, "UTF-8");
                    lname = java.net.URLEncoder.encode(editECLname.getText().toString().trim(), "UTF-8");
                    laddress = java.net.URLEncoder.encode(editECLaddress.getText().toString().trim(), "UTF-8");
                    lmobile = editECLmobile.getText().toString().trim();
                    lemail=editECLemail.getText().toString().trim();
                    lcompname=java.net.URLEncoder.encode(editECLcompanyname.getText().toString().trim(), "UTF-8");
                    // mobile=Integer.valueOf(lmobile);
                } catch (UnsupportedEncodingException e) {

                }

                if (!lname.equals("") && !laddress.equals("") && !lmobile.equals("") && !lemail.equals("") && !lcompname.equals("")) {

                    if (lmobile.length() == 10 ) {

                        if (lemail.matches(emailPattern))
                        {
                            url = "http://10.0.2.2/safalm_admin/add_comp_lead.php?lname=" + lname + "&laddress=" + laddress + "&lmobile=" + lmobile + "&emp_id=" + emp_id + "&lemail=" + lemail + "&lcompany_name=" + lcompname + "&ldate=" + date_time;

                            new EmployeeAddCompanyLeadActivity.Employee().execute();
                        }else {
                            Toast.makeText(EmployeeAddCompanyLeadActivity.this, "Enter Valid Email Address...", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(EmployeeAddCompanyLeadActivity.this, "Enter valid Mobile Number..", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(EmployeeAddCompanyLeadActivity.this, "All field are required.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeAddCompanyLeadActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

            if (contacts.get("success").toString().equals("1")) {

                Toast.makeText(EmployeeAddCompanyLeadActivity.this, "Lead added successfully...", Toast.LENGTH_SHORT).show();
//                editECLid.setText("");
                editECLemail.setText("");
                editECLname.setText("");
                editECLaddress.setText("");
                editECLmobile.setText("");
                editECLcompanyname.setText("");
                editECLdate.setText("");
                editECLtime.setText("");
            }
            pDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //  Log.e("hardik", url);
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Log.e("SAFALM1=", jsonObj.toString());

                    String success = jsonObj.getString("success");
                    String message = jsonObj.getString("message");
                    Log.e("SAFALM3=", success);
                    Log.e("SAFALM4=", message);

                    // tmp hash map for single contact
                    contacts = new HashMap<>();

                    // adding each child node to HashMap key => value
                    contacts.put("success", success);
                    contacts.put("message", message);

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
