package com.example.raju.safalmadmin.EmployeeSide;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.raju.safalmadmin.HttpHandler;
import com.example.raju.safalmadmin.R;
import com.example.raju.safalmadmin.SafalmAdmin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class EmployeeEditVisitedLeadActivity extends AppCompatActivity {

    SafalmAdmin se;
    private String TAG = EmployeeEditVisitedLeadActivity.class.getSimpleName();


    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;

    ArrayList<HashMap<String, String>> productList;


    String pid,pname, lid, name, address, mobile,email,cmpName,cmp_edit,date,time,prate,pamount,pquantity,decision,emp_id;

    Double rate,quantity;

    EditText editEVLproductname,editEVLproductprice,editEVLdate,editEVLtime,editEVLproductquantity,editEVLproductamount,editEVLname,editEVLaddress,editEVLmobile,editEVLdecision,editEVLemail,editEVLcompanyname;

    private static final String TAG_PID = "pid";
    private static final String TAG_PNAME = "pname";
    private static final String TAG_PPRICE = "pprice";
    private static final String TAG_PDETAIL = "pdetail";

    int mDate, mMonth, mYear, mMinute, mHour;

    String date_time, dt, tm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_edit_visited_lead_activity);

        editEVLname = findViewById(R.id.editEVLname);
        editEVLaddress = findViewById(R.id.editEVLaddress);
        editEVLmobile = findViewById(R.id.editEVLmobile);
        editEVLproductname = findViewById(R.id.editEVLproductname);
        editEVLproductquantity = findViewById(R.id.editEVLproductquantity);
        editEVLproductprice = findViewById(R.id.editEVLproductprice);
        editEVLproductamount = findViewById(R.id.editEVLproductamount);
        editEVLdate = findViewById(R.id.editEVLdate);
        editEVLtime = findViewById(R.id.editEVLtime);
        editEVLdecision = findViewById(R.id.editEVLdecision);
        editEVLemail = findViewById(R.id.editEVLemail);
        editEVLcompanyname = findViewById(R.id.editEVLcompanyname);

        ImageView imgProductList=findViewById(R.id.imgProductList);


        Button btnEVLadd = findViewById(R.id.btnEVLadd);

        Intent ii = getIntent();
        Bundle b = ii.getExtras();

        if (b != null) {
            lid = (String) b.get("id");
            name = (String) b.get("name");
            pid = (String) b.get("pid");
            address = (String) b.get("address");
            mobile = (String) b.get("mobile");
            email=(String) b.get("email");
            cmpName=(String)b.get("cname");
            date=(String)b.get("date");
            time=(String)b.get("time");
            pname=(String)b.get("pname");
            prate=(String)b.get("prate");
            pamount=(String)b.get("pamount");
            pquantity=(String)b.get("pquantity");
            decision=(String)b.get("decision");
            cmp_edit=(String)b.get("cmp_edit");
            //txtSLDname.setText(lead_id);
        }

        editEVLname.setText(name);
        editEVLaddress.setText(address);
        editEVLmobile.setText(mobile);
        editEVLemail.setText(email);
        editEVLcompanyname.setText(cmpName);
        editEVLdate.setText(date);
        editEVLtime.setText(time);
        editEVLproductname.setText(pname);
        editEVLproductprice.setText(prate);
        editEVLproductamount.setText(pamount);
        editEVLdecision.setText(decision);
        editEVLproductquantity.setText(pquantity);

        se = (SafalmAdmin) getApplicationContext();
        //emp_id = "1";
        emp_id = se.getempId();


        //Toast.makeText(EmployeeEditVisitedLeadActivity.this, pid+pquantity, Toast.LENGTH_SHORT).show();

        btnEVLadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dt = editEVLdate.getText().toString();
                tm = editEVLtime.getText().toString();
                date_time = dt + " " + tm;
                pquantity = editEVLproductquantity.getText().toString();
                prate = editEVLproductprice.getText().toString();


                try {
                    date_time = java.net.URLEncoder.encode(date_time, "UTF-8");
                    decision = java.net.URLEncoder.encode(editEVLdecision.getText().toString().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if ((!date_time.equals("") && !pquantity.equals("") && !prate.equals("") && !decision.equals(""))) {
                    if (cmp_edit.equals("1")) {
                        url = "http://10.0.2.2/safalm_admin/employee_edit_company_visited_lead.php?vid=" + lid + "&p_id=" + pid + "&p_quantity=" + pquantity + "&p_rate=" + prate + "&decision=" + decision + "&date_time=" + date_time + "&lead_sid=" + lid + "&emp_id=" + emp_id + "&p_amount=" + pamount;
                        new EmployeeEditVisitedLeadActivity.Employee().execute();
                    }else {
                        url = "http://10.0.2.2/safalm_admin/employee_edit_self_visited_lead.php?vid=" + lid + "&p_id=" + pid + "&p_quantity=" + pquantity + "&p_rate=" + prate + "&decision=" + decision + "&date_time=" + date_time + "&lead_sid=" + lid + "&emp_id=" + emp_id + "&p_amount=" + pamount;
                        new EmployeeEditVisitedLeadActivity.Employee().execute();
                    }

                } else {
                    Toast.makeText(EmployeeEditVisitedLeadActivity.this, "All fields are required!!!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        imgProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(EmployeeEditVisitedLeadActivity.this, EmployeeProductListActivity.class);
                startActivityForResult(i, 1);


            }
        });

        editEVLdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDate = c.get(Calendar.DATE);
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);


                DatePickerDialog datePickerDialog = new DatePickerDialog(EmployeeEditVisitedLeadActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //   String mnth, day;
//
                        editEVLdate.setText((year + "-" + (((month + 1) < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth)));
//                        timePicker();

                        // editASLTVSdatetime.setText(date_time);
                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.show();
            }
        });
        editEVLtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c1 = Calendar.getInstance();
                mHour = c1.get(Calendar.HOUR);
                mMinute = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(EmployeeEditVisitedLeadActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // editASLTVSdatetime.setText(date_time + (" "+hourOfDay + ":" + minute));
                        editEVLtime.setText((((hourOfDay < 10) ? "0" + hourOfDay : hourOfDay) + ":" + ((minute < 10) ? "0" + minute : minute) + ":00"));
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        editEVLproductquantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!editEVLproductprice.getText().toString().equals("") && !editEVLproductquantity.getText().toString().equals("")) {


                    rate = Double.parseDouble(editEVLproductprice.getText().toString().trim());
                    quantity = Double.parseDouble(editEVLproductquantity.getText().toString().trim());

                    pamount = String.valueOf(rate * quantity);
                    editEVLproductamount.setText(pamount);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editEVLproductprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!editEVLproductprice.getText().toString().equals("") && !editEVLproductquantity.getText().toString().equals("")) {


                    rate = Double.parseDouble(editEVLproductprice.getText().toString().trim());
                    quantity = Double.parseDouble(editEVLproductquantity.getText().toString().trim());

                    pamount = String.valueOf(rate * quantity);
                    editEVLproductamount.setText(pamount);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data.getExtras().containsKey(TAG_PID)) {

            pid = data.getExtras().getString(TAG_PID);
            pname = data.getExtras().getString(TAG_PNAME);
            prate = data.getExtras().getString(TAG_PPRICE);

            editEVLproductname.setText(pname);
            editEVLproductprice.setText(prate);

    }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeEditVisitedLeadActivity.this);
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
//                startActivity(i);
//                finish();
//                editEVLaddress.setText("");
//                editEVLcompanyname.setText("");
//                editEVLdate.setText("");
//                editEVLdecision.setText("");
//                editEVLemail.setText("");
//                editEVLmobile.setText("");
//                editEVLname.setText("");
//                editEVLproductamount.setText("");
//                editEVLproductprice.setText("");
//                editEVLproductquantity.setText("");
//                editEVLtime.setText("");

               Toast.makeText(EmployeeEditVisitedLeadActivity.this, "Data Updated successfully...", Toast.LENGTH_SHORT).show();
               finish();
//                Intent i=(new Intent(EmployeeEditVisitedLeadActivity.this,EmployeeSelfVisitedLeadDetailActivity.class));
//                i.putExtra("lead_id",lid);
//                startActivity(i);
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
