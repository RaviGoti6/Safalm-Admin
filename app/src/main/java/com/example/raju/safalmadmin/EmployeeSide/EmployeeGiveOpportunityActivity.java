package com.example.raju.safalmadmin.EmployeeSide;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.raju.safalmadmin.GiveTargetActivity;
import com.example.raju.safalmadmin.HttpHandler;
import com.example.raju.safalmadmin.R;
import com.example.raju.safalmadmin.SafalmAdmin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class EmployeeGiveOpportunityActivity extends AppCompatActivity {

    EditText editGOdetail, editGOtodate, editGOfromdate, editGOquantity;
    Button editGOadd;
    Spinner editGOtargettype;
    SafalmAdmin se;
    ImageView imgcalender1, imgcalender2;
    String quantity, oppo_type, date_from, date_to, p_id, emp_id, pname,detail;

    AsyncTask at;
    int flag = 0;

    private String TAG = GiveTargetActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;
    LinearLayout ic1;

    ArrayList<HashMap<String, String>> leadList;

    String[] target = new String[]{"Target", "Lead"};

    private static final String TAG_PID = "pid";
    private static final String TAG_PNAME = "pname";
    private static final String TAG_PPRICE = "pprice";
    private static final String TAG_PDETAIL = "pdetail";

    int mDate, mMonth, mYear, mMinute, mHour;
    String date_time, dt, tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_give_opportunity_activity);

        editGOquantity = findViewById(R.id.editGOquantity);
        editGOdetail = findViewById(R.id.editGOdetail);
        editGOfromdate = findViewById(R.id.editGOfromdate);
        editGOtodate = findViewById(R.id.editGOtodate);
        imgcalender1 = findViewById(R.id.imgcalender1);
        imgcalender2 = findViewById(R.id.imgcalender2);
        editGOadd = findViewById(R.id.editGOadd);
        ic1 = findViewById(R.id.ic1);

        editGOtargettype = findViewById(R.id.editGOtargettype);

        se = (SafalmAdmin) getApplicationContext();
        emp_id = se.getempId();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, target);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editGOtargettype.setAdapter(adapter);

        editGOtargettype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                oppo_type = editGOtargettype.getSelectedItem().toString();
                //Toast.makeText(EmployeeGiveOpportunityActivity.this, "target:" + oppo_type, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editGOadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quantity = editGOquantity.getText().toString().trim();
                date_from = editGOfromdate.getText().toString().trim();
                date_to = editGOtodate.getText().toString().trim();
                detail = editGOdetail.getText().toString().trim();

                if (oppo_type.equals("Target")) {

                    url = "http://10.0.2.2/safalm_admin/give_opportunity.php?oppo_quantity=" + quantity + "&oppo_type=" + oppo_type + "&f_date=" + date_from + "&extra_detail=" + detail + "&t_date=" + date_to + "&emp_id=" + emp_id;
                    new Employee().execute();
                }
                if (oppo_type.equals("Lead")) {

                    p_id = "0";
                    url = "http://10.0.2.2/safalm_admin/give_opportunity.php?oppo_quantity=" + quantity + "&oppo_type=" + oppo_type + "&f_date=" + date_from + "&extra_detail=" + detail + "&t_date=" + date_to + "&emp_id=" + emp_id;
                    new Employee().execute();
                }
            }
        });

        imgcalender1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDate = c.get(Calendar.DATE);
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);


                DatePickerDialog datePickerDialog = new DatePickerDialog(EmployeeGiveOpportunityActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String mnth, day;

                        editGOfromdate.setText((year + "-" + (((month + 1) < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth)));

                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.show();

            }
        });

        imgcalender2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDate = c.get(Calendar.DATE);
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);


                DatePickerDialog datePickerDialog = new DatePickerDialog(EmployeeGiveOpportunityActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String mnth, day;

                        editGOtodate.setText((year + "-" + (((month + 1) < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth)));

                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.show();

            }
        });
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeGiveOpportunityActivity.this);
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
                // Intent i = new Intent(AddSelfLeadActivity.this, MainActivity.class);
                //  startActivity(i);
                finish();
                Toast.makeText(EmployeeGiveOpportunityActivity.this, "Opportunity added successfully...", Toast.LENGTH_SHORT).show();
            }

            pDialog.dismiss();
            if (flag == 0) {
                flag++;
            }
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
//                    Log.e("SAFALM2=", contacts.toString());
                    // JSONArray contacts = new JSONArray(jsonStr);

                    // looping through All Contacts
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
