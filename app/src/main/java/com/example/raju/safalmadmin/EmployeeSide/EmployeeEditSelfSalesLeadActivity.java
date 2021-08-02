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

public class EmployeeEditSelfSalesLeadActivity extends AppCompatActivity {

    SafalmAdmin se;
    private String TAG = EmployeeEditSelfSalesLeadActivity.class.getSimpleName();


    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;

    ArrayList<HashMap<String, String>> productList;


    String pid,pname, lid,slid, name, address, mobile,email,cmpName,cmp_edit,date,time,prate,pamount,pquantity,decision,emp_id;

    Double rate,quantity;

    EditText editESSLproductname,editESSLproductprice,editESSLdate,editESSLtime,editESSLproductquantity,editESSLproductamount,editESSLname,editESSLaddress,editESSLmobile,editESSLemail,editESSLcompanyname;

    private static final String TAG_PID = "pid";
    private static final String TAG_PNAME = "pname";
    private static final String TAG_PPRICE = "pprice";
    private static final String TAG_PDETAIL = "pdetail";

    int mDate, mMonth, mYear, mMinute, mHour;

    String date_time, dt, tm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_edit_self_sales_lead_activty);

        editESSLname = findViewById(R.id.editESSLname);
        editESSLaddress = findViewById(R.id.editESSLaddress);
        editESSLmobile = findViewById(R.id.editESSLmobile);
        editESSLproductname = findViewById(R.id.editESSLproductname);
        editESSLproductquantity = findViewById(R.id.editESSLproductquantity);
        editESSLproductprice = findViewById(R.id.editESSLproductprice);
        editESSLproductamount = findViewById(R.id.editESSLproductamount);
        editESSLdate = findViewById(R.id.editESSLdate);
        editESSLtime = findViewById(R.id.editESSLtime);
       // editESSLdecision = findViewById(R.id.editESSLdecision);
        editESSLemail = findViewById(R.id.editESSLemail);
        editESSLcompanyname = findViewById(R.id.editESSLcompanyname);

        ImageView imgProductList=findViewById(R.id.imgProductList);


        Button btnESSLadd = findViewById(R.id.btnESSLadd);

        Intent ii = getIntent();
        Bundle b = ii.getExtras();

        if (b != null) {
            lid = (String) b.get("id");
            slid = (String) b.get("slid");
            name = (String) b.get("name");
            pid = (String) b.get("pid");
            address = (String) b.get("address");
            mobile = (String) b.get("mobile");
            email=(String) b.get("email");
            cmpName=(String)b.get("cname");
            date=(String)b.get("date");
            time=(String)b.get("time");
            pname=(String)b.get("pname");
            pid=(String)b.get("pid");
            prate=(String)b.get("prate");
            pamount=(String)b.get("pamount");
            pquantity=(String)b.get("pquantity");
           // decision=(String)b.get("decision");
            cmp_edit=(String)b.get("cmp_edit");
            //txtSLDname.setText(lead_id);
        }

        editESSLname.setText(name);
        editESSLaddress.setText(address);
        editESSLmobile.setText(mobile);
        editESSLemail.setText(email);
        editESSLcompanyname.setText(cmpName);
        editESSLdate.setText(date);
        editESSLtime.setText(time);
        editESSLproductname.setText(pname);
        editESSLproductprice.setText(prate);
        editESSLproductamount.setText(pamount);
       // editESSLdecision.setText(decision);
        editESSLproductquantity.setText(pquantity);

        se = (SafalmAdmin) getApplicationContext();
        //emp_id = "1";
        emp_id = se.getempId();



        btnESSLadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dt = editESSLdate.getText().toString();
                tm = editESSLtime.getText().toString();
                date_time = dt + " " + tm;
                pquantity = editESSLproductquantity.getText().toString();
                prate = editESSLproductprice.getText().toString();
                try {
                    date_time = java.net.URLEncoder.encode(date_time, "UTF-8");
                   // decision = java.net.URLEncoder.encode(editESSLdecision.getText().toString().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if ((!date_time.equals("") && !pquantity.equals("") && !prate.equals(""))) {
                    if (cmp_edit.equals("1")) {
                        url = "http://10.0.2.2/safalm_admin/employee_edit_company_sales_lead.php?vid=" + lid + "&p_id=" + pid + "&p_quantity=" + pquantity + "&p_rate=" + prate +  "&date_time=" + date_time +  "&emp_id=" + emp_id + "&p_amount=" + pamount;
                        new EmployeeEditSelfSalesLeadActivity.Employee().execute();
                    }else {
                        url = "http://10.0.2.2/safalm_admin/employee_edit_self_sales_lead.php?vid=" + lid + "&p_id=" + pid + "&p_quantity=" + pquantity + "&p_rate=" + prate +  "&date_time=" + date_time +  "&emp_id=" + emp_id + "&p_amount=" + pamount;
                        new EmployeeEditSelfSalesLeadActivity.Employee().execute();
                    }

                } else {
                    Toast.makeText(EmployeeEditSelfSalesLeadActivity.this, "All fields are required!!!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        imgProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(EmployeeEditSelfSalesLeadActivity.this, EmployeeProductListActivity.class);
                startActivityForResult(i, 1);


            }
        });

        editESSLdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDate = c.get(Calendar.DATE);
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);


                DatePickerDialog datePickerDialog = new DatePickerDialog(EmployeeEditSelfSalesLeadActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //   String mnth, day;
//
                        editESSLdate.setText((year + "-" + (((month + 1) < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth)));
//                        timePicker();

                        // editASLTVSdatetime.setText(date_time);
                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.show();
            }
        });
        editESSLtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c1 = Calendar.getInstance();
                mHour = c1.get(Calendar.HOUR);
                mMinute = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(EmployeeEditSelfSalesLeadActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // editASLTVSdatetime.setText(date_time + (" "+hourOfDay + ":" + minute));
                        editESSLtime.setText((((hourOfDay < 10) ? "0" + hourOfDay : hourOfDay) + ":" + ((minute < 10) ? "0" + minute : minute) + ":00"));
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        editESSLproductquantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!editESSLproductprice.getText().toString().equals("") && !editESSLproductquantity.getText().toString().equals("")) {


                    rate = Double.parseDouble(editESSLproductprice.getText().toString().trim());
                    quantity = Double.parseDouble(editESSLproductquantity.getText().toString().trim());

                    pamount = String.valueOf(rate * quantity);
                    editESSLproductamount.setText(pamount);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editESSLproductprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!editESSLproductprice.getText().toString().equals("") && !editESSLproductquantity.getText().toString().equals("")) {


                    rate = Double.parseDouble(editESSLproductprice.getText().toString().trim());
                    quantity = Double.parseDouble(editESSLproductquantity.getText().toString().trim());

                    pamount = String.valueOf(rate * quantity);
                    editESSLproductamount.setText(pamount);
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

            editESSLproductname.setText(pname);
            editESSLproductprice.setText(prate);

        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeEditSelfSalesLeadActivity.this);
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
                editESSLaddress.setText("");
                editESSLcompanyname.setText("");
                editESSLdate.setText("");
                //editESSLdecision.setText("");
                editESSLemail.setText("");
                editESSLmobile.setText("");
                editESSLname.setText("");
                editESSLproductamount.setText("");
                editESSLproductprice.setText("");
                editESSLproductquantity.setText("");
                editESSLtime.setText("");

                Toast.makeText(EmployeeEditSelfSalesLeadActivity.this, "Lead added successfully...", Toast.LENGTH_SHORT).show();
                finish();
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
