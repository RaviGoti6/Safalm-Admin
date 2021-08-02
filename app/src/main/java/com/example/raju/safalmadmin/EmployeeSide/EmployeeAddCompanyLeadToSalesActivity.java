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
import android.widget.TextView;
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

public class EmployeeAddCompanyLeadToSalesActivity extends AppCompatActivity {

    SafalmAdmin se;
    private String TAG = EmployeeAddCompanyLeadToSalesActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;

    ArrayList<HashMap<String, String>> productList;

    private static final String TAG_PID = "pid";
    private static final String TAG_PNAME = "pname";
    private static final String TAG_PPRICE = "pprice";
    private static final String TAG_PDETAIL = "pdetail";

    EditText edtProductName, editACLTSproductprice, editACLTSproductquantity, editACLTStime, editACLTSdate, editACLTSdecision, editACLTSproductamount;
    ImageView imgProductList;

    TextView txtACLTSemail, txtACLTScompanyname;

    String pid, pname, pquantity, pmobile, emp_id, lid, lname, laddress, lmobile, pprice, pdetail, lemail, lcname, amount;
    double price, quantity;
    int mDate, mMonth, mYear, mMinute, mHour;
    String date_time, dt, tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_add_company_lead_to_sales_activity);

        TextView txtACLTSname = findViewById(R.id.txtACLTSname);
        TextView txtACLTSaddress = findViewById(R.id.txtACLTSaddress);
        TextView txtACLTSmobile = findViewById(R.id.txtACLTSmobile);
        imgProductList = findViewById(R.id.imgProductList);
        productList = new ArrayList<>();

        edtProductName = findViewById(R.id.edtProductName);
        editACLTSproductprice = findViewById(R.id.editACLTSproductprice);
        editACLTSproductquantity = findViewById(R.id.editACLTSproductquantity);
        editACLTSdate = findViewById(R.id.editACLTSdate);
        editACLTStime = findViewById(R.id.editACLTStime);
        editACLTSdecision = findViewById(R.id.editACLTSdecision);
        editACLTSproductamount = findViewById(R.id.editACLTSproductamount);
        txtACLTScompanyname = findViewById(R.id.txtACLTScompanyname);
        txtACLTSemail = findViewById(R.id.txtACLTSemail);


        Button btnACLTSsales = findViewById(R.id.btnACLTSsales);

        imgProductList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (imgProductList.hasFocus()) {
                    Intent i = new Intent(EmployeeAddCompanyLeadToSalesActivity.this, EmployeeProductListActivity.class);
                    startActivityForResult(i, 1);
                }
            }
        });

        editACLTSdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDate = c.get(Calendar.DATE);
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);


                DatePickerDialog datePickerDialog = new DatePickerDialog(EmployeeAddCompanyLeadToSalesActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        editACLTSdate.setText((year + "-" + (((month + 1) < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth)));

                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.show();

            }
        });

        editACLTStime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar c1 = Calendar.getInstance();
                mHour = c1.get(Calendar.HOUR);
                mMinute = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(EmployeeAddCompanyLeadToSalesActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // editASLTVSdatetime.setText(date_time + (" "+hourOfDay + ":" + minute));
                        editACLTStime.setText((((hourOfDay < 10) ? "0" + hourOfDay : hourOfDay) + ":" + ((minute < 10) ? "0" + minute : minute) + ":00"));
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        // date_time=dt+" "+tm;

        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            lid = (String) b.get("id");
            lname = (String) b.get("name");
            laddress = (String) b.get("address");
            lmobile = (String) b.get("mobile");
            lemail = (String) b.get("email");
            lcname = (String) b.get("cmpName");
            //txtSLDname.setText(lead_id);
        }

        txtACLTSname.setText(lname);
        txtACLTSaddress.setText(laddress);
        txtACLTSmobile.setText(lmobile);
        txtACLTSemail.setText(lemail);
        txtACLTScompanyname.setText(lcname);


        se = (SafalmAdmin) getApplicationContext();
        //emp_id = "1";
        emp_id = se.getempId();

        btnACLTSsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dt = editACLTSdate.getText().toString();
                tm = editACLTStime.getText().toString();
                date_time = dt + " " + tm;
                pquantity = editACLTSproductquantity.getText().toString();
                pprice = editACLTSproductprice.getText().toString();
                try {
                    date_time = java.net.URLEncoder.encode(date_time, "UTF-8");
                    pdetail = java.net.URLEncoder.encode(editACLTSdecision.getText().toString().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if ((!date_time.equals("") && !pquantity.equals("") && !pprice.equals("") && !pdetail.equals(""))) {

                    url = "http://10.0.2.2/safalm_admin/employee_add_company_lead_to_sales.php?p_id=" + pid + "&p_quantity=" + quantity + "&p_rate=" + price + "&decision=" + pdetail + "&date_time=" + date_time + "&lead_sid=" + lid + "&emp_id=" + emp_id + "&p_amount=" + amount;
                    //new AddSelfLeadToVisitedAndSalesActivity.Employee().execute();

                    //url = "http://100.93.150.250/safalm/add_self_to_sales.php?p_id=" + pid + "&p_quantity=" + quantity + "&p_rate=" + price + "&date_time=" + date_time + "&lead_vid=" + lid + "&emp_id=" + emp_id + "&p_amount=" + amount;
                    new EmployeeAddCompanyLeadToSalesActivity.Employee().execute();
                } else {
                    Toast.makeText(EmployeeAddCompanyLeadToSalesActivity.this, "All fields are required!!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        imgProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(EmployeeAddCompanyLeadToSalesActivity.this, EmployeeProductListActivity.class);
                startActivityForResult(i, 1);

            }
        });

        editACLTSproductquantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!editACLTSproductprice.getText().toString().equals("") && !editACLTSproductquantity.getText().toString().equals("")) {


                    price = Double.parseDouble(editACLTSproductprice.getText().toString().trim());
                    quantity = Double.parseDouble(editACLTSproductquantity.getText().toString().trim());

                    amount = String.valueOf(price * quantity);
                    editACLTSproductamount.setText(amount);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editACLTSproductprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!editACLTSproductprice.getText().toString().equals("") && !editACLTSproductquantity.getText().toString().equals("")) {

                    price = Double.parseDouble(editACLTSproductprice.getText().toString().trim());
                    quantity = Double.parseDouble(editACLTSproductquantity.getText().toString().trim());

                    amount = String.valueOf(price * quantity);
                    editACLTSproductamount.setText(amount);
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
            pprice = data.getExtras().getString(TAG_PPRICE);
            pdetail = data.getExtras().getString(TAG_PDETAIL);

            edtProductName.setText(pname);
            editACLTSproductprice.setText(pprice);
            //Toast.makeText(AddSelfLeadToVisitedAndSalesActivity.this, "id=" + pid, Toast.LENGTH_SHORT).show();

            //Toast.makeText(AddSelfLeadToVisitedAndSalesActivity.this, "p_id=" + p_id, Toast.LENGTH_SHORT).show();
        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeAddCompanyLeadToSalesActivity.this);
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
                edtProductName.setText("");
                editACLTSproductprice.setText("");
                editACLTSdecision.setText("");
                editACLTSproductamount.setText("");
                editACLTSproductquantity.setText("");
                editACLTSdate.setText("");
                editACLTStime.setText("");

                Toast.makeText(EmployeeAddCompanyLeadToSalesActivity.this, "Lead added successfully...", Toast.LENGTH_SHORT).show();

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
